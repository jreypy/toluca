
API
=
Login
-----
    $ curl -X POST http://localhost:8090/auth/login -H 'Content-Type: application/json' -d '{"username":"user","password":"password"}'


Get Rooms
--------

    $ curl http://localhost:8090/api/room -H 'Authentication: 12345'
