
API
=
Login
-----
    $ curl -X POST http://localhost:8091/auth/login -H 'Content-Type: application/json' -d '{"username":"user","password":"password"}'


Get Rooms
--------

    $ curl http://localhost:8091/api/room -H 'Authentication: user-cd0d1ea3-5207-40cc-86c3-b48a2f00f902'

Create Room
--------

    $ curl -X POST http://localhost:8091/api/room -H 'Content-Type: application/json' -d '{"name":"UCA"}'

Join Room
--------

    $ curl -X POST "http://localhost:8091/api/room/principal/join?authkey=julio-rey" -H 'Content-Type: application/json'
