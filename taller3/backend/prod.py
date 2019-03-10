import os, logging
from tornado.wsgi import WSGIContainer
from tornado.httpserver import HTTPServer
from tornado.ioloop import IOLoop
from service import app
from service import init
######################################################################
def runserver():
    port = int(os.environ.get('PORT', 5001))
    init()
    app.run(host='0.0.0.0', port=port, debug=True)
    http_server = HTTPServer(WSGIContainer(app))
    http_server.listen(port, address='0.0.0.0')
    IOLoop.instance().start()

if __name__ == '__main__':
    runserver()
