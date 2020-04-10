Building
-------


mvn clean compile



Run
=
Run RabbitMQ
-----
    $ docker-compose -f toluca-server/docker/rabbitmq.yml up &

Run Truco Server
-----
    
    $ java -jar toluca-server/target/toluca-server-2.0.0.jar &

Run Truco WebSocket Server
-----
    $ java -jar truco-websocket/target/truco-websocket-2.0.0.jar &

Run Toluca Client
-----
    $ java -jar truco-ui/target/truco.jar 

