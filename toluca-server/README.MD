
API
=
Login
-----
    $ curl -X POST http://localhost:8090/auth/login -H 'Content-Type: application/json' -d '{"username":"user","password":"password"}'


Get Rooms
--------

    $ curl http://localhost:8091/api/room -H 'Authentication: 12345'

Create Room
--------

    $ curl -X POST http://localhost:8091/api/room -H 'Content-Type: application/json' -d '{"name":"UCA"}'

Join Room
--------

    $ curl -X POST "http://localhost:8091/api/room/principal/join?authkey=julio-rey" -H 'Content-Type: application/json'
