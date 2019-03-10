# Ejecución Backend


## Desarrollo

iniciar neo4

    ps aux | grep neo4j

    cd /Users/ivanmatis/Downloads/University/recommend/taller3_data/neo4j-community-3.3.0
    ./bin/neo4j console

iniciar el entorno de ejecución de python

    source activate gl-env
    pip install -r requirements.txt

ejecutar servidor

    export LC_ALL=en_US.UTF-8
    export LANG=en_US.UTF-8
    python runserver.py


## Ejecución prueba unitaria

    source activate gl-env

configurar el url...

    python service_test.py ApiTest.test_testdb



## Producción


### neo4j

    cd /opt/sites/jmendez/neo4j-community-3.3.0
    ./bin/neo4j start

    tail -f /opt/sites/jmendez/neo4j-community-3.3.0/logs/neo4j.log

### python

Bajar la aplicación anterior

    sudo netstat -nlp | grep :9999

ejecutar

    mkdir virt_env
    virtualenv virt_env/virt1

    cd /opt/sites/jmendez/mine4201/
    git pull origin master


    source  /opt/sites/jmendez/virt_env/virt1/bin/activate

    cd /opt/sites/jmendez/mine4201/taller3/backend
    pip install -U scikit-learn
    pip install -r requirements.txt
    sh run_prod.sh



## Delete data from neo4j 


    '''
      if data["total_rated_movies"]  > 1000000:
          app.logger.debug("######################################################################")
          app.logger.debug( "forgetting some rels")
          # nota : el order by en una relation del grafo es demasiado demorado. no  permite crear index
          #  el SKIP 50000  se demora mucho y bloquea la app
          query = '  MATCH ()-[r:RATED]->()    WITH  r SKIP 50000 LIMIT 5000        DELETE r '
          app.neo4j_graph.run(query)
          app.count_rated_relationships()
          app.logger.debug("######################################################################")
      '''
