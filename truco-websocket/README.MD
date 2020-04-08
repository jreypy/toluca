
API
=
Connect to Websocket
-----
    $ wscat -c ws://localhost:8050/ws -H 'Authentication: 1234'

Commands
--------

Get Rooms
--------

    $ {"command":"get_rooms"}

Create Room
--------

    $ {"command":"create_room", "data" : {"name":"My First Room"}}
    

Join to Room
--------
    $ {"command":"join_room", "data" : {"id":"1"}}

Events
-------

Room Created Event
-------    

    $ {"type":"ROOM_CREATED","data":{"id":"af138e6b-bae4-4671-940e-5508c06484f2","name":"My First Room"}}
