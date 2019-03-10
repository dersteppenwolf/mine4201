import os
from service import app
from service import init

def runserver():
    port = int(os.environ.get('PORT', 5001))
    init()
    app.run(host='0.0.0.0', port=port, debug=True)

if __name__ == '__main__':
    runserver()
