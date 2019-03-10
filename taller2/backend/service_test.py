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
    python service_test.py ApiTest.test_hello
    '''
    def test_hello(self):
        try:
            self.logger.debug( "test_hello" )
            url = BASE_URL+"hello?name=Luis orm"
            headers = {'Accept': 'application/json', 'Content-type':'text/html'}
            r = requests.get(url, headers=headers)
            self.logger.debug( r.status_code )
            jsonResponse = r.json()
            self.logger.debug( jsonResponse)
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            self.logger.error( tbinfo )
            self.logger.error( sys.exc_type )
            self.logger.error( sys.exc_value )
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
    python service_test.py ApiTest.test_categories
    '''
    def test_categories(self):
        try:
            self.logger.debug( "test_categories" )
            url = BASE_URL+"categories?"
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
    python service_test.py ApiTest.test_places
    '''
    def test_places(self):
        try:
            self.logger.debug( "test_places" )
            url = BASE_URL+"places?"
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
    python service_test.py ApiTest.test_business
    '''
    def test_business(self):
        try:
            self.logger.debug( "test_places" )
            url = BASE_URL+"business?id=60"
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
    python service_test.py ApiTest.test_business_full
    '''
    def test_business_full(self):
        try:
            self.logger.debug( "test_places" )
            url = BASE_URL+"business?id=60&full=true"
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
    python service_test.py ApiTest.test_user
    '''
    def test_user(self):
        try:
            self.logger.debug( "test_places" )
            url = BASE_URL+"user?id=60&lat=36.41948&lon=-115.2424431"
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
    python service_test.py ApiTest.test_popular_business
    '''
    def test_popular_business(self):
        try:
            self.logger.debug( "test_popular_business" )
            url = BASE_URL+"popular?lat=36.4248&lon=-115.2424431"
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
    python service_test.py ApiTest.test_search
    '''
    def test_search(self):
        try:
            self.logger.debug( "test_popular_business" )
            url = BASE_URL+"search?lat=36.1318&lon=-115.1977&q=kolumbian"
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
    python service_test.py ApiTest.test_index_businesses
    '''
    def test_index_businesses(self):
        try:
            self.logger.debug( "test_index_businesses" )
            url = BASE_URL+"indexdata?"
            self.logger.debug( url )
            headers = {'Accept': 'application/json', 'Content-type':'text/html'}
            r = requests.get(url, headers=headers)
            self.logger.debug( r.status_code )
            jsonResponse = r.json()
            self.logger.debug( json.dumps(jsonResponse, sort_keys=True, indent=4, separators=(',', ': '))  )
        except Exception as e:
            tb = sys.exc_info()[2]
            tbinfo = traceback.format_tb(tb)[0]
            self.logger.error( tbinfo )
            self.logger.error( sys.exc_type )
            self.logger.error( sys.exc_value )






if __name__ == '__main__':
    unittest.main()
