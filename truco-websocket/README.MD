
API
=
Connect to Websocket
-----
    $ wscat -c ws://localhost:8050/ws -H 'Authentication: julio-1234'


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
    $ {"command":"get_room", "data" : {"roomId":"1"}}


Join to Room
--------
    $ {"command":"join_room", "data" : {"roomId":"1"}}
    

Create Table
--------
    $ {"command":"create_room_table", "data" : {"roomId":"1", "points": 30 }}

Join to Room Table
--------
    $ {"command":"join_room_table", "data" : {"roomId":"1","tableId":"7"}}
    
Set Table Position
--------
    $ {"command":"set_table_position", "data" : {"roomId":"1","tableId":"1", "chair":0}}

Start Game
--------
    
    $ {"command":"start_game", "data" : {"roomId":"1","tableId":"4"}}

    
Events
-------

Get Rooms Response
-------

    $ {"type":"COMMAND_RESPONSE","command":"get_rooms","data":{"user":{"id":"julio","username":"julio"},"rooms":[{"id":"1","name":"Principal","users":[]}]}


Create Room Response
------
    $ {"type":"COMMAND_RESPONSE","command":"create_room","data":{"message":"Truco Room [2 was created","user":{"id":"julio","username":"julio"},"room":{"id":"2","name":"My First Room","users":[]}}}

Room Created Event
-------    
    $ {"type":"ROOM_CREATED","data":{"id":"af138e6b-bae4-4671-940e-5508c06484f2","name":"My First Room"}}


Joined to Room Event
------

    $ {"type":"JOINED_TO_ROOM","data":{"eventName":"ROOM_USER_JOINED","message":"User joined to the Room [1]","user":{"id":"julio","username":"julio"},"room":{"id":"1","name":"Principal","users":[{"user":{"id":"julio","username":"julio"},"online":true},{"user":{"id":"user","username":"user"},"online":true}]}}}
