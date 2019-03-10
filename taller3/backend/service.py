#-*- coding: UTF-8 -*-
import json, os, traceback,  logging, sys, os, csv,  hashlib
import psycopg2
import requests
import networkx as nx
from networkx.algorithms import bipartite
from flask import Flask, url_for, abort
from flask import request
from flask import jsonify
from elasticsearch import Elasticsearch
from flask.ext.cors import CORS, cross_origin
from flask_apscheduler import APScheduler
from neo4j.v1 import GraphDatabase
from json import dumps
from neo4j.util import watch
from sys import stdout
from py2neo import Graph
######################################################################
neo4j_user = "neo4j"
neo4j_password = "neo4jneo4j"
number_of_recs = 10
######################################################################
db_port = str(os.environ.get('DB_PORT', "5432"))
db_user = str(os.environ.get('DB_USER', "aplicacion"))
db_pwd = str(os.environ.get('DB_PWD', "aplicacion"))
app = Flask(__name__)
cors = CORS(app, max_age=3800)
######################################################################
######################################################################
def db(database_name=''):
    return psycopg2.connect(dbname="mine3", user=db_user, password=db_pwd, host="localhost", port=db_port)
######################################################################
######################################################################
'''
ejecuta un sql en la bd pg
'''
def query_db(query, args=(), one=False):
    app.logger.debug( query )
    cur = db().cursor()
    cur.execute(query, args)
    r = [dict((cur.description[i][0], value) \
               for i, value in enumerate(row)) for row in cur.fetchall()]
    cur.connection.close()
    return (r[0] if r else None) if one else r

'''
consultas que retornan múltiples columnas
'''
def query_db_return_array(query, args=(), one=False):
    app.logger.debug( query )
    cur = db().cursor()
    cur.execute(query, args)
    r = []
    for row in cur.fetchall():
        arr_row = []
        for i, value in enumerate(row):
            arr_row.append(value)
        r.append(arr_row)
    cur.connection.close()
    return  r

'''
consultas que retornan una sola columna
'''
def query_db_return_simple_array(query, args=(), one=False):
    app.logger.debug( query )
    cur = db().cursor()
    cur.execute(query, args)
    r = []
    for row in cur.fetchall():
        for i, value in enumerate(row):
            r.append(value)

    cur.connection.close()
    return  r

def update_db(query, args=(), one=False):
    app.logger.debug( query )
    conn = db()
    cur = conn.cursor()
    cur.execute(query, args)
    conn.commit()


def bad_request(message):
    response = jsonify({'message': message})
    response.status_code = 400
    return response
######################################################################
######################################################################

######################################################################
class Config(object):
    '''
    {
        'id': 'job1',
        'func': 'jobs:job1',
        'args': (1, 2),
        'trigger': 'interval',
        'seconds': 60
    },

    '''
    JOBS = [
        {
            'id': 'init_models',
            'func': 'jobs:init_models',
            'trigger': 'interval',
            'seconds': 10
        },
        {
            'id': 'stream',
            'func': 'jobs:stream',
            'trigger': 'interval',
            'seconds': 120
        }
    ]

    SCHEDULER_API_ENABLED = True



def count_rated_relationships():
    query =  '''    MATCH (n)-[r:RATED]->()  RETURN COUNT(r) as total_rated_movies    '''
    #app.logger.debug(query)
    data = app.neo4j_graph.run(query)
    for d in data:
        app.logger.debug((d))
    return d

######################################################################
######################################################################
def init():
    logFormat = '%(asctime)-15s %(name)-12s %(levelname)-8s %(message)s'
    logfile = "server.log"
    logging.basicConfig(level=logging.DEBUG, format=logFormat, filename=logfile,  filemode='w', encoding = "UTF-8" )
    logging.getLogger('flask_cors').level = logging.DEBUG
    logging.getLogger('neo4j.bolt').level = logging.ERROR
    logging.getLogger("neo4j.bolt").setLevel(logging.WARNING)
    #ch = logging.StreamHandler()
    #ch.setLevel(logging.DEBUG)
    #app.logger.addHandler(ch)
    app.logger.setLevel(logging.DEBUG)
    app.logger.debug("######################################################################")
    app.logger.debug("Start")
    app.logger.debug(db_port)
    app.bipartite_graph = None
    app.streaming_in_progress = False
    app.streaming_recommender =  None
    app.query_db = query_db
    app.update_db = update_db
    app.count_rated_relationships = count_rated_relationships
    app.logger.debug("######################################################################")
    watch("neo4j.bolt", logging.WARNING, stdout)
    app.neo4j_driver = GraphDatabase.driver("bolt://localhost:7687", auth=(neo4j_user,neo4j_password))
    app.neo4j_graph =  Graph(password=neo4j_password)
    app.logger.debug("## neo4j # of rated relationships: ")
    app.count_rated_relationships()
    app.logger.debug("######################################################################")
    app.logger.debug("## networkx version")
    app.logger.debug("######################################################################")
    app.logger.debug (nx.__version__)
    #app.users = query_db_return_simple_array("select userid from filtered_users order by userid  ")
    #app.movies = query_db_return_simple_array("select imdb_tconst from ml_imdb_movies  ")
    #app.logger.debug(app.users)
    app.logger.debug("######################################################################")
    app.logger.debug("## scheduler")
    app.logger.debug("######################################################################")
    # https://github.com/viniciuschiele/flask-apscheduler/blob/master/examples/jobs.py
    app.config.from_object(Config())
    app.apscheduler = APScheduler()
    # it is also possible to enable the API directly
    # scheduler.api_enabled = True
    app.apscheduler.init_app(app)
    app.apscheduler.start()
    app.logger.debug("######################################################################")



######################################################################
@app.after_request
def apply_caching(response):
    response.headers["X-APP"] = "taller3"
    return response
######################################################################
@app.teardown_appcontext
def on_context_close(error):
    app.logger.debug('on_context_close')



########################################################################################################
##  index
########################################################################################################
@app.route('/', methods=['GET', 'POST'])
def api_root():
    app.logger.debug('api_root')
    return 'Welcome'
########################################################################################################

########################################################################################################
##  prueba conexión bd
########################################################################################################

@app.route('/testdb')
def api_testdb():
    app.logger.debug("######################################################################")
    app.logger.debug('api_testdb')
    app.logger.debug("######################################################################")
    try:
        obj = {}
        my_query = query_db("select count(*) from ml_imdb_movies ")
        obj["total_rated_movies"] = my_query[0]
        obj["total_graph_ratings"] = count_rated_relationships()

        if app.bipartite_graph:
            obj["bipartite_graph_number_of_nodes"] = app.bipartite_graph.number_of_nodes()
            obj["bipartite_graph_number_of_edges"] = app.bipartite_graph.number_of_edges()

            examples = []
            for e in app.bipartite_graph.edges():
                examples.append(e)
                if(len(examples)>10):
                    break

            obj["bipartite_graph_edges_examples"] = examples

        obj["flurs_userids"]  = []
        obj["flurs_number_of_events"]  = 0
        if app.streaming_recommender:
            app.logger.debug( " app.streaming_recommender ")
            #app.logger.debug(   app.streaming_recommender.user_ids)
            user_ids = app.streaming_recommender.user_ids
            user_ids = user_ids[-200:]
            obj["flurs_userids"] = user_ids
            obj["flurs_number_of_events"]  = app.streaming_recommender.number_of_events
        else:
            app.logger.debug( "not app.streaming_recommender ")
        json_output = json.dumps(obj)
        app.logger.debug("######################################################################")
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        app.logger.error( sys.exc_type )

        raise e
    return json_output

########################################################################################################
##  obtener usuarios con màs reviews top 100
########################################################################################################

@app.route('/user/popular')
def api_users_popular():
    app.logger.debug('api_users_popular')
    try:
        query = '''  select userid, total_ratings from filtered_users order by total_ratings desc limit 100'''
        biz_obj = query_db(query)
        json_output = json.dumps(biz_obj)
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        raise e
    return json_output


@app.route('/streams/recent')
def api_streams_recent():
    app.logger.debug('api_users_popular')
    try:
        query = '''  select id, day, total, start_id, end_id, to_char(stream_date, 'YYYY-MM-DD HH24:MI:SS') as stream_date
        from filtered_ratings_per_day
      where stream_date is not null order by id desc limit 20  '''
        biz_obj = query_db(query)
        json_output = json.dumps(biz_obj)
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )

        raise e
    return json_output

########################################################################################################
##  obtener detalles del negocio
########################################################################################################

@app.route('/movie', methods = ['GET'])
def api_movie():
    app.logger.debug('api_movie')
    try:
        id_param = request.args['id']
        full_param = request.args.get("full", True, type=bool)
        biz_obj = get_item_data(id_param,full_param )
        json_output = json.dumps(biz_obj)
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )

        raise e
    return json_output

########################################################################################################
##  obtener detalles del negocio desde las diferentes entidades de la bd
########################################################################################################
def get_item_data(id_param,full_param ):
    query = ''' select id  , movieid  , title  ,
    imdb_tconst  ,    title_type  ,    primary_title  ,    original_title  ,    is_adult  ,
    start_year  ,    end_year  ,    runtime_minutes  ,    release_decade  ,
    tmdb_id  ,    tmdb_overview  ,    tmdb_backdrop_path ,
    tmdb_poster_path , to_char(tmdb_release_date, 'YYYY-MM-DD') as tmdb_release_date
    from ml_imdb_movies where movieid = {}
    '''.format(id_param)
    item_obj = query_db(query)
    app.logger.debug( item_obj )

    imdb_tconst = item_obj[0]["imdb_tconst"]

    # genres
    query = ''' select   genre , code  from ml_imdb_movies_genres  where movieid = {}   '''.format(id_param)
    results = query_db(query)
    item_obj[0]["genres"] = results

    # tags
    query = ''' select   tag , count(*) as total
     from filtered_tags  where movieid = {} group by tag order by count(*) desc limit 10   '''.format(id_param)
    results = query_db(query)
    item_obj[0]["tags"] = results

    if imdb_tconst is not None:
        app.logger.debug( "full" )

        query = '''
        select r.name, n.primary_name, n.birth_year, n.death_year
        from filtered_people_role as r  inner join filtered_names n on (n.nconst =  r.name)
        where r.imdb_tconst = '{}'  and r.role = 'director'
        '''.format(imdb_tconst)
        results = query_db(query)
        item_obj[0]["directors"] = results

        query = '''
        select r.name, n.primary_name, n.birth_year, n.death_year
        from filtered_people_role as r  inner join filtered_names n on (n.nconst =  r.name)
        where r.imdb_tconst = '{}'  and r.role = 'writer'
        '''.format(imdb_tconst)
        results = query_db(query)
        item_obj[0]["writers"] = results

        query = '''
        select r.name, n.primary_name, n.birth_year, n.death_year
        from filtered_people_role as r  inner join filtered_names n on (n.nconst =  r.name)
        where r.imdb_tconst = '{}'  and r.role = 'principal'
        '''.format(imdb_tconst)
        results = query_db(query)
        item_obj[0]["principals"] = results

    return item_obj[0]



########################################################################################################
########################################################################################################

########################################################################################################
########################################################################################################



########################################################################################################
##  obtener detalles del usuario
########################################################################################################

@app.route('/user/recs/flurs')
def api_user_recs():
    try:
        app.logger.debug("######################################################################")
        app.logger.debug('api_user_recs flurs')
        app.logger.debug("######################################################################")
        if not app.streaming_recommender:
            return bad_request('flurs model not ready for processing')


        id_param = int(request.args['id'])
        user_obj = {"id": id_param }
        recommendations = app.streaming_recommender.recommend(id_param,10)
        recs = []
        for item in recommendations["fm"]:
            movie_data = get_item_data( item[1], True )
            rec = {"ranking" : item[0] , "id": item[1],  "score":item[2] , "movie_data": movie_data   }
            recs.append(rec)
        user_obj["fm"] = recs

        recs = []
        for item in recommendations["mixed"]:
            movie_data = get_item_data( item[1], True )
            rec = {"ranking" : item[0] , "id": item[1],  "score":item[2] , "movie_data": movie_data   }
            recs.append(rec)
        user_obj["mixed"] = recs

        recs = []
        for item in recommendations["svd"]:
            movie_data = get_item_data( item[1], True )
            rec = {"ranking" : item[0] , "id": item[1],  "score":item[2],  "movie_data": movie_data   }
            recs.append(rec)
        user_obj["svd"] = recs
        ########################################################################################################
        ########################################################################################################
        json_output = json.dumps(user_obj)
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        return bad_request(str(e))
    return json_output


@app.route('/user/recs/pprank')
def api_recs_pprank():
    app.logger.debug("######################################################################")
    app.logger.debug('api_recs_pprank')
    try:
        if app.bipartite_graph is None or app.model_networkx_loading:
            return bad_request('nx model not ready for processing')

        id_param = str(request.args['id'])
        user_obj = {"id": id_param }

        #app.logger.debug(app.bipartite_graph.has_node(id_param))
        neighbors = app.bipartite_graph.neighbors(id_param)
        rated_movies = []
        for n in neighbors:
            #app.logger.debug(n)
            rated_movies.append(n)

        #app.logger.debug('rated_movies : '+str(rated_movies))

        app.logger.debug('calculando pprank')
        ppr1 = nx.pagerank(app.bipartite_graph, alpha=0.9, personalization={id_param:1})
        app.logger.debug('calculando pprank END')
        ppr1_sorted  = sorted(ppr1.items(), key=lambda kv: kv[1], reverse=True)
        app.logger.debug("num items ppr1 : "+str(len(ppr1_sorted)))
        recs = []
        for row in  ppr1_sorted:
            id = row[0]
            score = row[1]

            if id == id_param:
                continue

            if not id.startswith('tt'):
                continue

            if id in rated_movies:
                app.logger.debug("previously seen item")
                continue

            movielens_id = query_db_return_simple_array("select movieid from ml_imdb_movies where imdb_tconst = '{}'  ".format(id))
            movie_data = get_item_data( movielens_id[0], True )
            #movie_data = {}

            app.logger.debug("id: "+id)

            rec = { "score": score, "id": id, "movie_data": movie_data, "ranking" : len(recs)+1  }
            recs.append(rec)

            if len(recs) > number_of_recs:
                break

        user_obj["recs"] = recs
        #app.logger.debug("user_obj:"+str(user_obj))
        json_output = json.dumps(user_obj)
        app.logger.debug("######################################################################")
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )

        raise e
    return json_output


@app.route('/user/recs/explain')
def api_user_recs_explain():
    try:
        app.logger.debug("######################################################################")
        app.logger.debug('api_user_recs_explain')
        userid = request.args['userid']
        movieid = request.args['movieid']

        user_obj = {"userid": userid, "movieid": movieid }

        ########################################################################################################
        ########################################################################################################
        query = "MATCH (u:User{userid : "+userid+"}) , (m:Movie {imdbid : '"+movieid+"'})   MATCH path = shortestPath( (u)-[*..4]-(m) )    RETURN path "
        app.logger.debug(query)
        with app.neo4j_driver.session() as session:
            results = session.run(query)
            path = results.single()["path"];
            #p =  [record['path'] for record in result]
            #app.logger.debug(path)
            nodes  = path.nodes
            relationships = path.relationships

            message = []
            for r in relationships:
                start = r.start
                end = r.end
                type = r.type
                item_start = [item for item in nodes if item.id == start]
                item_end= [item for item in nodes if item.id == end]

                label_start = list(item_start[0].labels)[0]
                message.append(label_start)
                #message.append(item_start[0].properties )
                message.append(get_label_attribute(label_start, item_start[0].properties ) )

                message.append(type)

                label_end= list(item_end[0].labels)[0]
                message.append(label_end)
                #message.append(item_end[0].properties )
                message.append(get_label_attribute(label_end, item_end[0].properties ) )

                message.append("AND")

            for m in message:
                app.logger.debug(m)
            #app.logger.debug(path.relationships)
            #for i in path:
            #    app.logger.debug(i)


            user_obj["explanation"] =  message[:-1]


        ########################################################################################################
        ########################################################################################################
        json_output = json.dumps(user_obj)
        app.logger.debug("######################################################################")
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )

        raise e
    return json_output




@app.route('/user/recs/graph/directors')
def api_user_recs_graph_directors():
    try:
        app.logger.debug("######################################################################")
        app.logger.debug('api_user_recs_graph_directors')
        id_param = str(request.args['id'])
        user_obj = {"id": id_param }

        ########################################################################################################
        ########################################################################################################
        query = '''
        MATCH (u:User)-[:RATED]->(movie)-[:DIRECTED_BY]->(p)
        WHERE u.userid = {}
        with p, count(p) as total , collect(movie) as seen
        order by total desc  limit 3
        match (neighbour)-[r:RATED]->(m2)-[:DIRECTED_BY]->(p)
        where not m2 in seen
        return m2.imdbid as imdbid , count(m2) as total_traversals order by total_traversals desc limit 10
        '''.format(id_param)

        app.logger.debug(query)
        recs  =  []
        with app.neo4j_driver.session() as session:
            results = session.run(query)
            app.logger.debug(results)
            for record in results:
                app.logger.debug(record)
                score = record["total_traversals"]
                imdbid = record["imdbid"]
                movielens_id = query_db_return_simple_array("select movieid from ml_imdb_movies where imdb_tconst = '{}'  ".format(imdbid))
                movie_data = get_item_data( movielens_id[0], True )

                rec = { "score": score,
                    "id": movielens_id,  "movie_data": movie_data, "ranking" : len(recs)+1  }
                recs.append(rec)
        user_obj["recs"] =  recs
        ########################################################################################################
        ########################################################################################################
        json_output = json.dumps(user_obj)
        app.logger.debug("######################################################################")
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        raise e
    return json_output

@app.route('/user/recs/graph/actors')
def api_user_recs_graph_actors():
    try:
        app.logger.debug("######################################################################")
        app.logger.debug('api_user_recs_graph_actors')
        id_param = str(request.args['id'])
        user_obj = {"id": id_param }

        ########################################################################################################
        ########################################################################################################
        query = '''
        MATCH (u:User)-[:RATED]->(movie),
        (p)-[:ACTED_IN]->(movie)
        WHERE u.userid =  {}
        with  p, count(p) as total , collect(movie) as seen
        order by total desc limit 5
        match (neighbour)-[r:RATED]->(m2)<-[:ACTED_IN]-(p)
        where not m2 in seen
        return m2.imdbid as imdbid , count(m2) as total_traversals order by total_traversals desc limit 10
        '''.format(id_param)

        app.logger.debug(query)
        recs  =  []
        with app.neo4j_driver.session() as session:
            results = session.run(query)
            app.logger.debug(results)
            for record in results:
                app.logger.debug(record)
                score = record["total_traversals"]
                imdbid = record["imdbid"]
                movielens_id = query_db_return_simple_array("select movieid from ml_imdb_movies where imdb_tconst = '{}'  ".format(imdbid))
                movie_data = get_item_data( movielens_id[0], True )

                rec = { "score": score,
                    "id": movielens_id,  "movie_data": movie_data, "ranking" : len(recs)+1  }
                recs.append(rec)
        user_obj["recs"] =  recs
        ########################################################################################################
        ########################################################################################################
        json_output = json.dumps(user_obj)
        app.logger.debug("######################################################################")
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        raise e
    return json_output


'''
muy demorado
Started streaming 10 records after 215285 ms and completed after 215285 ms.
'''
@app.route('/user/recs/graph/genres')
def api_user_recs_graph_genres():
    try:
        app.logger.debug("######################################################################")
        app.logger.debug('api_user_recs_graph_genres')
        id_param = str(request.args['id'])
        user_obj = {"id": id_param }

        ########################################################################################################
        ########################################################################################################
        query = '''
        MATCH (u:User)-[:RATED]->(movie)-[:BELONGS_TO]->(category)
        WHERE u.userid =  {}
        with category, count(category) as total , collect(movie) as seen
        order by total limit 3
        match (neighbour)-[r:RATED]->(m2)-[:BELONGS_TO]->(category),
        (neighbour)-[:RATED]->(m3)
        where not m3 in seen
        return m3.imdbid as imdbid, count(m3) as total_traversals order by total_traversals desc limit 10
        '''.format(id_param)

        app.logger.debug(query)
        recs  =  []
        with app.neo4j_driver.session() as session:
            results = session.run(query)
            app.logger.debug(results)
            for record in results:
                app.logger.debug(record)
                score = record["total_traversals"]
                imdbid = record["imdbid"]
                movielens_id = query_db_return_simple_array("select movieid from ml_imdb_movies where imdb_tconst = '{}'  ".format(imdbid))
                movie_data = get_item_data( movielens_id[0], True )

                rec = { "score": score,
                    "id": movielens_id,  "movie_data": movie_data, "ranking" : len(recs)+1  }
                recs.append(rec)
        user_obj["recs"] =  recs
        ########################################################################################################
        ########################################################################################################
        json_output = json.dumps(user_obj)
        app.logger.debug("######################################################################")
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        raise e
    return json_output


def get_label_attribute(class_name, properties):
    if(class_name == 'User'):
        return properties["userid"]
    if(class_name == 'Movie'):
        return properties["primary_title"]
    if(class_name == 'Genre'):
        return properties["genre"]
    if(class_name == 'Person'):
        return properties["primary_name"]
    else:
        return properties
