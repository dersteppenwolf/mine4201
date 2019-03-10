#-*- coding: UTF-8 -*-
import json, os
import psycopg2
import logging, json
import traceback,  logging, sys, os, csv,  hashlib
import requests
from flask import Flask, url_for
from flask import request
from flask import jsonify
from elasticsearch import Elasticsearch
from flask.ext.cors import CORS, cross_origin
######################################################################
ES_HOST="https://mine:mine@localhost/"
ES_PORT=80
ES_INDEX="mine4201_yelp_business"
ES_TYPE="yelp_business"
######################################################################
db_port = str(os.environ.get('DB_PORT', "5432"))
db_user = str(os.environ.get('DB_USER', "mine"))
db_pwd = str(os.environ.get('DB_PWD', "mine"))
app = Flask(__name__)
cors = CORS(app, max_age=3800,  resources=r'/*')
es = Elasticsearch([ES_HOST],port=ES_PORT)
######################################################################
def init():
    logFormat = '%(asctime)-15s %(name)-12s %(levelname)-8s %(message)s'
    logfile = "server.log"
    logging.basicConfig(level=logging.DEBUG, format=logFormat, filename=logfile,  filemode='w', encoding = "UTF-8" )
    logging.getLogger('flask_cors').level = logging.DEBUG
    ch = logging.StreamHandler()
    ch.setLevel(logging.DEBUG)
    app.logger.addHandler(ch)
    app.logger.setLevel(logging.DEBUG)
    app.logger.debug("######################################################################")
    app.logger.debug("Start")
    app.logger.debug(ES_HOST)
    app.logger.debug(ES_PORT)
    app.logger.debug(db_port)
    app.logger.debug("######################################################################")

######################################################################
@app.after_request
def apply_caching(response):
    response.headers["X-APP"] = "taller2"
    return response
######################################################################
def db(database_name='pepe'):
    return psycopg2.connect(dbname="mine2", user=db_user, password=db_pwd, host="localhost", port=db_port)

def query_db(query, args=(), one=False):
    app.logger.debug( query )
    cur = db().cursor()
    cur.execute(query, args)
    r = [dict((cur.description[i][0], value) \
               for i, value in enumerate(row)) for row in cur.fetchall()]
    cur.connection.close()
    return (r[0] if r else None) if one else r
######################################################################
@app.route('/', methods=['GET', 'POST'])
def api_root():
    return 'Welcome'

######################################################################
@app.route('/testdb')
def api_testdb():
    try:
        my_query = query_db("select id, title from import.categories")
        json_output = json.dumps(my_query)
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        app.logger.error( sys.exc_type )
        app.logger.error( sys.exc_value )
        raise e
    return json_output

########################################################################################################
##  retorna las 100 categorias más populares del dataset
########################################################################################################
@app.route('/categories')
def api_categories():
    try:
        query = '''
        select c.id, c.title, count(b.id) as total
        from import.categories c inner join import.business_categories_st b
        on (c.id = b.id_category)
        group by c.id, c.title
        order by count(b.id) desc, title
        limit 100
        '''
        my_query = query_db(query)
        json_output = json.dumps(my_query)
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        app.logger.error( sys.exc_value )
        raise e
    return json_output



########################################################################################################
##  retorna las ciudades del dataset ordenadas por la cantidad de businesses
########################################################################################################

@app.route('/places')
def api_places():
    try:
        query = '''
        select c.id, c.country, c.state, c.city, c.total_businesses,
        ST_X(c.geom) as longitude, ST_Y(c.geom) as latitude
        from import.places c
        where state in ('NV', 'AZ')
        order by total_businesses desc, city
        '''
        my_query = query_db(query)
        json_output = json.dumps(my_query)
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        app.logger.error( sys.exc_value )
        raise e
    return json_output

########################################################################################################
##  obtener detalles del negocio
########################################################################################################

@app.route('/business', methods = ['GET'])
def api_business():
    app.logger.debug('api_hello')
    try:
        id_param = request.args['id']
        full_param = request.args.get("full", False, type=bool)
        biz_obj = get_biz_data(id_param,full_param )
        json_output = json.dumps(biz_obj)
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        app.logger.error( sys.exc_value )
        raise e
    return json_output

########################################################################################################
##  obtener detalles del negocio desde las diferentes entidades de la bd
########################################################################################################
def get_biz_data(id_param,full_param ):

    query = '''
    select id, business_id, name, id_place, category_count, review_count, stars, country, state, city,
    ST_X(geom) as longitude, ST_Y(geom) as latitude,
    total_checkins ,
    total_tips, data->'attributes' as attributes
    from import.business_st  where id = {}
    '''.format(id_param)
    biz_obj = query_db(query)
    app.logger.debug( biz_obj )

    # categories
    query = '''
    select id, id_category, category from import.business_categories_st  where id_business = {}
    '''.format(id_param)
    results = query_db(query)
    biz_obj[0]["categories"] = results

    # similar items by FM
    query = '''
    select  id_business_similar, rank from import.recs_fm_similar_items  where id_business = {}
    order by rank
    '''.format(id_param)
    results = query_db(query)
    biz_obj[0]["similar_by_fm"] = results

    # checkins by checkin_by_part_of_day_st
    query = '''
    select day,  part_of_day, total from import.checkin_by_part_of_day_st  where id_business = {}
    order by total desc
    '''.format(id_param)
    results = query_db(query)
    biz_obj[0]["checkins_by_partofday"] = results

    if full_param:
        app.logger.debug( "full" )

        query = '''
        select id_user, stars::double precision,useful, to_char(date, 'YYYY-MM-DD') as date, text from import.review_filtered_st  where id_business = {}
        order by date desc
        '''.format(id_param)
        results = query_db(query)
        biz_obj[0]["reviews"] = results

        query = '''
        select id_user, likes::double precision, to_char(date, 'YYYY-MM-DD') as date, text from import.tip_st  where id_business = {}
        order by date desc
        '''.format(id_param)
        results = query_db(query)
        biz_obj[0]["tips"] = results


    return biz_obj[0]

########################################################################################################
##  obtener usuarios con màs reviews top 100
########################################################################################################

@app.route('/user/popular')
def api_users_popular():
    try:
        query = ''' select id, trim(from name) as name, total_reviews
        from import.users_st_popular
        '''
        biz_obj = query_db(query)
        json_output = json.dumps(biz_obj)
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        app.logger.error( sys.exc_value )
        raise e
    return json_output


########################################################################################################
##  obtener detalles del usuario
########################################################################################################

@app.route('/user')
def api_users():
    try:
        id_param = request.args['id']
        lat_param = request.args['lat']
        lon_param = request.args['lon']

        query = '''
        select id, user_id, name,years_elite, fans,  to_char(yelping_since, 'YYYY-MM-DD') as yelping_since
        from import.users_st   where id = {}
        '''.format(id_param)
        user_obj = query_db(query)


        ########################################################################################################
        ########################################################################################################
        query = '''
        select r.id_business, b.name, ST_Y(b.geom) as latitude,   ST_X(b.geom) as longitude,  r.score::double precision, r.rank
        from import.recs_fm  r inner join import.business_st b on (r.id_business = b.id)
        where id_user = {}
        order by rank asc limit 20
        '''.format(id_param)
        results = query_db(query)
        user_obj[0]["recs_by_fm"] = results

        # reajuste con recíproco de la distancia pow 1
        query = '''
        with user_geom as (
         	select ST_SetSRID(ST_MakePoint({}, {}),4326) as geom )
         , user_recs as (
             select r.id_user,r.id_business, b.name,  r.score::double precision, r.rank ,    ST_Y(b.geom) as latitude,   ST_X(b.geom) as longitude,
             st_distance(b.geom::geography, u.geom::geography ) / 1000 as distance
             from import.recs_fm  r inner join import.business_st b on (r.id_business = b.id), user_geom u
             where r.id_user = {}  )
         , decay as (
             select *, round(  1/( distance   )::numeric , 4) as decay_factor
             from user_recs
         ), rerank  as (
         select *, rank*decay_factor as score_adjusted ,  ROW_NUMBER()
         over (partition by id_user order by (rank*decay_factor) desc)
         from decay
         )
         select id_business,name, latitude, longitude, score, rank, score_adjusted::double precision,row_number as rank_adjusted, (rank - row_number) as rank_change
         from rerank
         order by row_number asc limit 20
        '''.format(lon_param, lat_param, id_param)
        results = query_db(query)
        user_obj[0]["recs_by_fm_with_context"] = results
        ########################################################################################################
        ########################################################################################################
        query = '''
        select r.id_business,b.name, ST_Y(b.geom) as latitude,   ST_X(b.geom) as longitude,  r.score::double precision, r.rank
        from import.recs_svd  r inner join import.business_st b on (r.id_business = b.id)
        where id_user = {}
        order by rank asc limit 20
        '''.format(id_param)
        results = query_db(query)
        user_obj[0]["recs_by_svd"] = results


        # reajuste con recíproco de la distancia pow 1
        query = '''
        with user_geom as (
         	select ST_SetSRID(ST_MakePoint({}, {}),4326) as geom )
         , user_recs as (
             select r.id_user,r.id_business, b.name, r.score::double precision, r.rank ,    ST_Y(b.geom) as latitude,   ST_X(b.geom) as longitude,
             st_distance(b.geom::geography, u.geom::geography ) / 1000 as distance
             from import.recs_svd  r inner join import.business_st b on (r.id_business = b.id), user_geom u
             where r.id_user = {}  limit 20 )
         , decay as (
             select *, round(  1/( distance   )::numeric , 4) as decay_factor
             from user_recs
         ), rerank  as (
         select *, rank*decay_factor as score_adjusted ,  ROW_NUMBER()
         over (partition by id_user order by (rank*decay_factor) desc)
         from decay
         )
         select id_business, name,
         latitude, longitude, score, rank, score_adjusted::double precision,row_number as rank_adjusted, (rank - row_number) as rank_change
         from rerank
         order by row_number asc limit 20
        '''.format(lon_param, lat_param, id_param)
        results = query_db(query)
        user_obj[0]["recs_by_svd_with_context"] = results
        ########################################################################################################
        ########################################################################################################
        json_output = json.dumps(user_obj[0])
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        app.logger.error( sys.exc_value )
        raise e
    return json_output


########################################################################################
# negocios más populares según coordenadas
########################################################################################

@app.route('/popular', methods = ['GET'])
def api_popular_business():
    app.logger.debug('api_popular_business')
    try:
        lat_param = request.args['lat']
        lon_param = request.args['lon']
        id_category_param = request.args.get("id_category", None, type=int)

        ########################################################################################
        ########################################################################################
        biz_obj = {}
        buffer_in_meters = 10000
        ########################################################################################
        ########################################################################################
        query = '''
        with a as (
            SELECT ST_Buffer_Meters(ST_SetSRID(ST_MakePoint({}, {}),4326) ,{}) as geom
        ), c as (
            select id as id_business, b.name, ST_Y(b.geom) as latitude, ST_X(b.geom) as longitude, stars as score
            from import.business_st  b, a
            where ST_Intersects(b.geom, a.geom)
            order by stars desc
            limit 10
        )
        select id_business, name, latitude, longitude, score, row_number() OVER () as rank
        from c
        '''.format(lon_param, lat_param,buffer_in_meters  )
        biz_obj["popular_by_stars"] = query_db(query)
        ########################################################################################
        ########################################################################################
        query = '''
        with a as (
            SELECT ST_Buffer_Meters(ST_SetSRID(ST_MakePoint({}, {}),4326) ,{}) as geom
        ), c as (
            select id as id_business, b.name,  ST_Y(b.geom) as latitude, ST_X(b.geom) as longitude, review_count as score
            from import.business_st  b, a
            where ST_Intersects(b.geom, a.geom) and review_count is not null
            order by review_count desc
            limit 10
        )
        select id_business, name, latitude, longitude, score, row_number() OVER () as rank
        from c
        '''.format(lon_param, lat_param,buffer_in_meters  )
        biz_obj["popular_by_review_count"] = query_db(query)
        ########################################################################################
        ########################################################################################
        query = '''
        with a as (
            SELECT ST_Buffer_Meters(ST_SetSRID(ST_MakePoint({}, {}),4326) ,{}) as geom
        ), c as (
            select id as id_business, b.name, ST_Y(b.geom) as latitude, ST_X(b.geom) as longitude, total_checkins as score
            from import.business_st  b, a
            where ST_Intersects(b.geom, a.geom) and total_checkins is not null
            order by total_checkins desc
            limit 10
        )
        select id_business, name, latitude, longitude, score, row_number() OVER () as rank
        from c
        '''.format(lon_param, lat_param,buffer_in_meters  )
        biz_obj["popular_by_checkins"] = query_db(query)
        ########################################################################################
        ########################################################################################

        app.logger.debug( biz_obj )

        json_output = json.dumps(biz_obj)
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        app.logger.error( sys.exc_value )
        raise e
    return json_output

########################################################################################
########################################################################################
# indexar datos en elasticsearch
########################################################################################
########################################################################################
@app.route('/indexdata', methods = ['GET'])
def api_index_businesses():
    app.logger.debug('api_index_businesses')
    try:
        query = '''
                select id
                from import.business_st
                where id >   156637
                order by id '''
        results = query_db(query)
        indexed = []
        for row in results:
            app.logger.debug("########################################################################################")
            #app.logger.debug(row)
            id_param = row["id"]
            app.logger.debug(id_param)
            biz_obj = get_biz_data(id_param,True )
            #app.logger.debug(biz_obj)
            #app.logger.debug(biz_obj["city"])
            location = {}
            location["lat"]= biz_obj["latitude"];
            location["lon"]= biz_obj["longitude"];

            biz_obj["location"] = location

            json_output = json.dumps(biz_obj)
            app.logger.debug(json_output)
            es.index(index=ES_INDEX, doc_type=ES_TYPE, id=id_param,  body=json_output)
            indexed.append(id_param)
            app.logger.debug("########################################################################################")

        biz_obj = {}
        biz_obj["indexed"] = indexed
        json_output = json.dumps(biz_obj)
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        app.logger.error( sys.exc_value )
        raise e
    return json_output


########################################################################################
# búsqueda  en el índice de elasticsearch
########################################################################################

@app.route('/search', methods = ['GET'])
def api_search():
    try:
        app.logger.debug('api_search')
        lat_param = request.args['lat']
        lon_param = request.args['lon']
        search_param = request.args.get("q", None, type=str).strip()

        app.logger.debug(lat_param)
        app.logger.debug(lon_param)
        app.logger.debug(search_param)



        query = {
                "function_score": {
                  "query": {
                    "bool": {
                      "should": [
                        {
                          "multi_match": {
                            "query": search_param,
                            "fuzziness": "AUTO",
                            "fields": [
                              "name^5",
                              "categories.category^4",
                              "reviews.text",
                              "tips.text"
                            ]
                          }
                        }
                      ]
                    }
                  },
                  "boost": "2",
                  "boost_mode":"multiply",
                  "functions": [
                    {
                      "gauss": {
                        "location": {
                          "origin": {
                            "lat": lat_param,
                            "lon": lon_param
                          },
                          "scale": "2km",
                          "offset": "2km",
                          "decay": 0.33
                        }
                      },
                      "weight": 2
                    },
                    {
                      "gauss": {
                        "stars": {
                          "origin": "5",
                          "scale": "1",
                          "offset": "1",
                          "decay": 0.5
                        }
                      },
                      "weight": 1
                    }
                  ]
                }
              }

        if (search_param == "" ):
            query.pop('bool', None)
            query["function_score"]["query"] = { "match_all": {} }


        script_fields =  {
            "distance": {
                "script": {
                    "source": "doc.location.planeDistance("+lat_param+","+lon_param+")*0.001",
                    "lang": "painless"
                }
            }
        }


        start_record = 0
        page_size = 20
        fields = ["id", "name", "state", "city","stars", "latitude", "longitude"]

        highlight = {
            "fields" : {
                "name" : {},
                "categories.category" : {},
                "reviews.text" : {},
                "tips.text" : {}
            }
        }
        response = query_elasticsearch(query, start_record, page_size, ES_INDEX, ES_TYPE, fields, highlight, script_fields)

        results = []
        rank = 1
        hits = response["hits"]["hits"]
        for hit in hits:
            item = hit["_source"]
            item["score"] = hit["_score"]
            item["rank"] = rank
            item["id_business"] = int(hit["_id"])
            item["distance"] = hit["fields"]["distance"][0]
            item["highlight"] = hit["highlight"]
            results.append(item)
            rank +=1
        json_output = json.dumps(results)
        return json_output
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        app.logger.error( sys.exc_type )
        app.logger.error( sys.exc_value )
        raise e


########################################################################################
########################################################################################
# Utilitarios
########################################################################################
########################################################################################


########################################################################################
'''
Ejecuta un query en elasticsearch
'''
########################################################################################
def query_elasticsearch(query, start_record, page_size, index, index_type, fields=[],
    highlight=None,
    script_fields=None, explain=False):

    try:
        if len(fields) == 0:
            fields = ["*"]
        app.logger.debug("query_elasticsearch")

        body={
          "_source": fields,
          "explain": explain,
          "query": query,
          "from": start_record,
          "size": page_size
        }

        if (highlight is not None):
            body['highlight'] = highlight

        if (script_fields is not None):
            body['script_fields'] = script_fields

        app.logger.debug(index)
        app.logger.debug(index_type)
        #app.logger.debug(body)
        app.logger.debug( json.dumps(body, sort_keys=True, indent=4, separators=(',', ': '))  )
        res = es.search(index=index, doc_type=index_type, body=body)
        #app.logger.debug( json.dumps(res, sort_keys=True, indent=4, separators=(',', ': '))  )
        return res
    except Exception as e:
        tb = sys.exc_info()[2]
        tbinfo = traceback.format_tb(tb)[0]
        app.logger.error( tbinfo )
        app.logger.error( sys.exc_type )
        app.logger.error( sys.exc_value )
        raise e
######################################################################
