# Ejecución Backend


## Desarrollo

    source activate gl-env
    pip install -r requirements.txt

    python runserver.py



## Ejecución prueba unitaria

    source activate gl-env

configurar el url...

    python service_test.py ApiTest.test_hello



## Producción

Bajar la aplicación anterior

    sudo netstat -nlp | grep :9999

ejecutar

    cd /opt/sites/jmendez/mine4201/taller2/backend
    sudo pip install -r requirements.txt
    sudo sh run_prod.sh
