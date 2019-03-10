import json, os, traceback,  logging, sys, os, csv,  hashlib

from service import app
from time import sleep
import networkx as nx
#importa la clase que tiene toda la logica
from StreammingRecomender import StreammingRecomender



def _create_relationship_in_graph(tx, query ):
    result = tx.run(query)



def job1(a, b):
    app.logger.debug("job1")
    app.logger.debug(str(a) + ' ' + str(b))


def stream():
    app.logger.debug("######################################################################")
    app.logger.debug("begin -- stream")
    app.logger.debug("##   app.streaming_in_progress :  "+str( app.streaming_in_progress))
    if not app.streaming_in_progress :
        try:
            load_stream_data()
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            app.logger.error( tbinfo )

    app.logger.debug("end -- stream")
    app.logger.debug("######################################################################")


def load_stream_data():
    try:
        app.streaming_in_progress  = True
        app.logger.debug("######################################################################")
        app.logger.debug("begin -- load_stream_data")
        app.logger.debug("######################################################################")

        query = "select * from filtered_ratings_per_day  where stream_date is  null   order by id asc limit 1 "
        #app.logger.debug(query)
        results  = app.query_db(query)
        start_id = results[0]["start_id"]
        end_id = results[0]["end_id"]
        id = str(results[0]["id"])
        app.logger.debug(id)
        query = 'update  filtered_ratings_per_day set stream_date = current_timestamp    where id =  {} and stream_date is null  '.format(id )
        #app.logger.debug(query)
        app.update_db(query)
        app.logger.debug("######################################################################")
        # ventana de ultimos  30 dias para el modelo de page rank personalizado
        num_months_bootstrap_events = '30 day'
        query = '''
            with f2 as (
            	select day, end_id ,
            	to_char( to_date(day, 'YYYY-MM-DD') - INTERVAL '{}', 'YYYY-MM-DD') as start_date
                from filtered_ratings_per_day where stream_date is not null
                order by id desc limit 1
    		),  r as (
            	select f1.day as begin_day, f1.start_id, f2.day as end_day, f2.end_id
            	from filtered_ratings_per_day as f1 inner join f2 on (f1.day = f2.start_date)
    		)
    		select id, userid, movieid, imdb_tconst,  rating,  timestamp
            from filtered_ratings f, r
            where id between r.start_id and r.end_id and imdb_tconst is not null
            order by id asc
        '''.format(num_months_bootstrap_events)
        events = app.query_db( query  )
        app.logger.debug("# of events last 30 days :  "+str(len(events)))

        try:
            app.logger.debug("######################################################################")
            app.logger.debug("######################################################################")
            app.logger.debug("loading -- networkx")
            # https://networkx.github.io/documentation/networkx-1.9.1/reference/algorithms.bipartite.html?highlight=bipartite#module-networkx.algorithms.bipartite
            bipartite_graph = nx.Graph()
            bipartite_graph.add_nodes_from([], bipartite=0) # Add the node attribute "bipartite"
            bipartite_graph.add_nodes_from([], bipartite=1)

            for row in events:
                #app.logger.debug(row['userid'])
                attribs = { "rating" : row['rating'], "timestamp" : row['timestamp'],  "id" : row['id'] }
                bipartite_graph.add_edge(str(row['userid']), row['imdb_tconst'],  attr_dict=attribs  )

            app.model_networkx_loading = False
            app.logger.debug("loading ok -- networkx")
            app.logger.debug("number_of_nodes: "+str(bipartite_graph.number_of_nodes()))
            app.logger.debug("number_of_edges: "+str(bipartite_graph.number_of_edges()))
            app.bipartite_graph =  bipartite_graph
            app.logger.debug("end -- bootstrap_model_networkx")
            app.logger.debug("######################################################################")
            app.logger.debug("######################################################################")
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            app.logger.error( tbinfo )

        # nuevos elementos last day, modelo streaming flurs y grafo history de neo4j
        num_months_bootstrap_events = '1 day'
        query = '''
            with f2 as (
            	select day, end_id ,
            	day  as start_date
                from filtered_ratings_per_day where stream_date is not null
                order by id desc limit 1
    		),  r as (
            	select f1.day as begin_day, f1.start_id, f2.day as end_day, f2.end_id
            	from filtered_ratings_per_day as f1 inner join f2 on (f1.day = f2.start_date)
    		)
    		select id, userid, movieid, imdb_tconst,  rating,  timestamp
            from filtered_ratings f, r
            where id between r.start_id and r.end_id and imdb_tconst is not null
            order by id asc
        '''
        events = app.query_db( query  )
        app.logger.debug("# of events last day :  "+str(len(events)))


        try:
            app.logger.debug("######################################################################")
            app.logger.debug("######################################################################")
            app.logger.debug("##  updateStreamByArray flurs ")
            if app.streaming_recommender:
                flurs_events = []
                for row in events:
                    flurs_events.append([   int(row['userid']),   int( row['movieid'] ) ,  int(  row['timestamp'] ) ])
                app.logger.debug("flurs_events to update: "+str(len(flurs_events)))
                app.streaming_recommender.updateStreamByArray(flurs_events)
                app.logger.debug("######################################################################")
                app.logger.debug("##  updateStreamByArray  END")
                app.logger.debug("######################################################################")
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            app.logger.error( tbinfo )


        try:
            app.logger.debug("######################################################################")
            app.logger.debug("######################################################################")
            app.logger.debug("##  updating neo4j ")
            data = app.count_rated_relationships()
            for row in events:
                try:
                    #app.logger.debug(row)
                    query =    ' MATCH (user:User {userid: toInteger('+str(row['userid'])+')}) MATCH (movie:Movie {imdbid: \''+row['imdb_tconst']+'\' }) '
                    query +=     ' MERGE (user)-[pu:RATED {id: toInteger(' + str(row['id'])
                    query +=   '),  rating: toFloat('+str(row['rating'])+'),   on: toInteger('+str(row['timestamp'])+') }]->(movie); '
                    #app.logger.debug(query)
                    app.neo4j_graph.run(query)
                except Exception as e:
                    app.logger.error("######################################################################")
                    tb = sys.exc_info()[2]
                    tbinfo = traceback.format_tb(tb)[0]
                    app.logger.error( tbinfo )
                    app.logger.error( row )
                    app.logger.error("######################################################################")
            app.logger.debug("######################################################################")
            app.count_rated_relationships()
            app.logger.debug("######################################################################")
            app.logger.debug("##  updating neo4j - END ")
            app.logger.debug("######################################################################")
            app.logger.debug("######################################################################")
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            app.logger.error( tbinfo )

        app.streaming_in_progress  = False
    except Exception as e:
        app.streaming_in_progress  = False
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )




def init_models():
    try:
        app.logger.debug("######################################################################")
        app.logger.debug("######################################################################")
        app.logger.debug("begin -- init_models")
        app.logger.debug("######################################################################")
        app.logger.debug("######################################################################")
        app.logger.debug("init -- networkx")
        # https://networkx.github.io/documentation/networkx-1.9.1/reference/algorithms.bipartite.html?highlight=bipartite#module-networkx.algorithms.bipartite
        bipartite_graph = nx.Graph()
        bipartite_graph.add_nodes_from([], bipartite=0) # Add the node attribute "bipartite"
        bipartite_graph.add_nodes_from([], bipartite=1)
        app.bipartite_graph =  bipartite_graph
        app.logger.debug("END init -- networkx")
        app.logger.debug("######################################################################")
        app.logger.debug("######################################################################")
        app.logger.debug("init -- flurs")
        app.streaming_recommender  = StreammingRecomender()
        app.logger.debug("END init -- flurs")
        app.logger.debug("######################################################################")
        app.logger.debug("######################################################################")
        app.apscheduler.scheduler.remove_job('init_models')
        app.logger.debug("end -- init_models")
        app.logger.debug("######################################################################")
        app.logger.debug("######################################################################")
        load_stream_data()
        app.logger.debug("######################################################################")
        app.logger.debug("######################################################################")
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
