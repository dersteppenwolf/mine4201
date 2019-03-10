#-*- coding: UTF-8 -*-

import logging
import datetime
import json
import datetime, requests
import unittest,  traceback,  logging, sys, os, csv,  hashlib

#test
BASE_URL = "http://localhost:5001/"


class ApiTest(unittest.TestCase):
    def setUp(self):
        logFormat = '%(asctime)-15s %(name)-12s %(levelname)-8s %(message)s'
        logfile = "test.log"
        logging.basicConfig(level=logging.DEBUG, format=logFormat, filename=logfile,  filemode='w' )
        self.logger = logging.getLogger("imdb_movie")
        self.logger.setLevel(logging.DEBUG)
        ch = logging.StreamHandler()
        self.logger.addHandler(ch)
        logging.getLogger('test').setLevel(logging.INFO)

    '''
    python service_test.py ApiTest.test_testdb
    '''
    def test_testdb(self):
        try:
            self.logger.debug( "test_testdb" )
            url = BASE_URL+"testdb?"
            headers = {'Accept': 'application/json', 'Content-type':'text/html'}
            r = requests.get(url, headers=headers)
            self.logger.debug( r.status_code )
            jsonResponse = r.json()

            self.logger.debug( json.dumps(jsonResponse, indent=4, sort_keys=True))
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            self.logger.error( tbinfo )
            self.logger.error( sys.exc_type )
            self.logger.error( sys.exc_value )

    '''
    python service_test.py ApiTest.test_streams_recent
    '''
    def test_streams_recent(self):
        try:
            self.logger.debug( "test_streams_recent" )
            url = BASE_URL+"streams/recent?"
            self.logger.debug( url )
            headers = {'Accept': 'application/json', 'Content-type':'text/html'}
            r = requests.get(url, headers=headers)
            self.logger.debug( r.status_code )
            jsonResponse = r.json()

            self.logger.debug( json.dumps(jsonResponse, indent=4, sort_keys=True))
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            self.logger.error( tbinfo )
            self.logger.error( sys.exc_type )
            self.logger.error( sys.exc_value )

    '''
    python service_test.py ApiTest.test_user_popular
    '''
    def test_user_popular(self):
        try:
            self.logger.debug( "test_user_popular" )
            url = BASE_URL+"user/popular?"
            self.logger.debug( url )
            headers = {'Accept': 'application/json', 'Content-type':'text/html'}
            r = requests.get(url, headers=headers)
            self.logger.debug( r.status_code )
            jsonResponse = r.json()
            self.logger.debug( json.dumps(jsonResponse, indent=4, sort_keys=True))
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            self.logger.error( tbinfo )
            self.logger.error( sys.exc_type )
            self.logger.error( sys.exc_value )


    ############################################################################
    ############################################################################


    '''
    python service_test.py ApiTest.test_movie
    '''
    def test_movie(self):
        try:
            self.logger.debug( "test_places" )
            url = BASE_URL+"movie?id=2788"
            self.logger.debug( url )
            headers = {'Accept': 'application/json', 'Content-type':'text/html'}
            r = requests.get(url, headers=headers)
            self.logger.debug( r.status_code )
            jsonResponse = r.json()

            self.logger.debug( json.dumps(jsonResponse, indent=4, sort_keys=True))
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            self.logger.error( tbinfo )
            self.logger.error( sys.exc_type )
            self.logger.error( sys.exc_value )


    '''
    python service_test.py ApiTest.test_user_recs
    '''
    def test_user_recs(self):
        try:
            self.logger.debug( "test_user_recs" )
            url = BASE_URL+"user/recs/flurs?id=173643"
            self.logger.debug( url )
            headers = {'Accept': 'application/json', 'Content-type':'text/html'}
            r = requests.get(url, headers=headers)
            self.logger.debug( r.status_code )
            jsonResponse = r.json()
            self.logger.debug( json.dumps(jsonResponse, indent=4, sort_keys=True))
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            self.logger.error( tbinfo )
            self.logger.error( sys.exc_type )
            self.logger.error( sys.exc_value )

    '''
    python service_test.py ApiTest.test_user_recs_graph_directors
    '''
    def test_user_recs_graph_directors(self):
        try:
            self.logger.debug( "test_user_recs" )
            url = BASE_URL+"user/recs/graph/directors?id=198536"
            self.logger.debug( url )
            headers = {'Accept': 'application/json', 'Content-type':'text/html'}
            r = requests.get(url, headers=headers)
            self.logger.debug( r.status_code )
            jsonResponse = r.json()
            self.logger.debug( json.dumps(jsonResponse, indent=4, sort_keys=True))
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            self.logger.error( tbinfo )
            self.logger.error( sys.exc_type )
            self.logger.error( sys.exc_value )

    '''
    python service_test.py ApiTest.test_user_recs_graph_actors
    '''
    def test_user_recs_graph_actors(self):
        try:
            self.logger.debug( "test_user_recs" )
            url = BASE_URL+"user/recs/graph/actors?id=181749"
            self.logger.debug( url )
            headers = {'Accept': 'application/json', 'Content-type':'text/html'}
            r = requests.get(url, headers=headers)
            self.logger.debug( r.status_code )
            jsonResponse = r.json()
            self.logger.debug( json.dumps(jsonResponse, indent=4, sort_keys=True))
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            self.logger.error( tbinfo )
            self.logger.error( sys.exc_type )
            self.logger.error( sys.exc_value )

    '''
    python service_test.py ApiTest.test_user_recs_graph_genres
    '''
    def test_user_recs_graph_genres(self):
        try:
            self.logger.debug( "test_user_recs" )
            url = BASE_URL+"user/recs/graph/genres?id=181749"
            self.logger.debug( url )
            headers = {'Accept': 'application/json', 'Content-type':'text/html'}
            r = requests.get(url, headers=headers)
            self.logger.debug( r.status_code )
            jsonResponse = r.json()
            self.logger.debug( json.dumps(jsonResponse, indent=4, sort_keys=True))
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            self.logger.error( tbinfo )
            self.logger.error( sys.exc_type )
            self.logger.error( sys.exc_value )

    '''
    python service_test.py ApiTest.test_user_pprank
    '''
    def test_user_pprank(self):
        try:
            self.logger.debug( "test_user_pprank" )
            url = BASE_URL+"user/recs/pprank?id=134988"
            self.logger.debug( url )
            headers = {'Accept': 'application/json', 'Content-type':'text/html'}
            r = requests.get(url, headers=headers)
            self.logger.debug( r.status_code )
            jsonResponse = r.json()
            self.logger.debug( json.dumps(jsonResponse, indent=4, sort_keys=True))
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            self.logger.error( tbinfo )
            self.logger.error( sys.exc_type )
            self.logger.error( sys.exc_value )


    '''
    python service_test.py ApiTest.test_user_explain
    '''
    def test_user_explain(self):
        try:
            self.logger.debug( "test_user_explain" )
            url = BASE_URL+"user/recs/explain?userid=107664&movieid=tt0111503"
            url = BASE_URL+"user/recs/explain?userid=172224&movieid=tt0143145"
            url = BASE_URL+"user/recs/explain?userid=172224&movieid=tt0091187"
            url = BASE_URL+"user/recs/explain?userid=172224&movieid=tt0203009"
            url = BASE_URL+"user/recs/explain?userid=172224&movieid=tt0114388"





            self.logger.debug( url )
            headers = {'Accept': 'application/json', 'Content-type':'text/html'}
            r = requests.get(url, headers=headers)
            self.logger.debug( r.status_code )
            jsonResponse = r.json()
            self.logger.debug( json.dumps(jsonResponse, indent=4, sort_keys=True))
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            self.logger.error( tbinfo )
            self.logger.error( sys.exc_type )
            self.logger.error( sys.exc_value )










if __name__ == '__main__':
    unittest.main()
