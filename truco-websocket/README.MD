
API
=
Connect to Websocket
-----
    $ wscat -c ws://localhost:8050/ws -H 'Authentication: 1234'


Get Rooms
--------

    $ {"command":"get_rooms"}

Create Room
--------

    $ {"command":"create_room", "data" : {"name":"My First Room"}}
