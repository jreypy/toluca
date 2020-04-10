
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
    

Get Room
--------
    $ {"command":"get_room", "data" : {"id":"1"}}


Join to Room
--------
    $ {"command":"join_room", "data" : {"id":"1"}}

Events
-------

Room Created Event
-------    

    $ {"type":"ROOM_CREATED","data":{"id":"af138e6b-bae4-4671-940e-5508c06484f2","name":"My First Room"}}


Joined to Room Event
------

    $ {"type":"JOINED_TO_ROOM","data":{"eventName":"ROOM_USER_JOINED","message":"User joined to the Room [1]","user":{"id":"julio","username":"julio"},"room":{"id":"1","name":"Principal","users":[{"user":{"id":"julio","username":"julio"},"online":true},{"user":{"id":"user","username":"user"},"online":true}]}}}
