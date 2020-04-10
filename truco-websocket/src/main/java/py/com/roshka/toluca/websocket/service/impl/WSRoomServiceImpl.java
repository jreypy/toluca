package py.com.roshka.toluca.websocket.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import py.com.roshka.toluca.websocket.service.AMQPDispatcher;
import py.com.roshka.toluca.websocket.service.RoomService;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomEvent;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Component
public class WSRoomServiceImpl implements RoomService {

    private final RestTemplate restTemplate;
    private final AMQPDispatcher amqpDispatcher;

    @Value("${trucoServerHost}")
    private String trucoServerHost;

    public WSRoomServiceImpl(RestTemplate restTemplate, AMQPDispatcher amqpDispatcher) {
        this.restTemplate = restTemplate;
        this.amqpDispatcher = amqpDispatcher;
    }

    @Override
    public void connect(String user) {
        Map map = new LinkedHashMap();
        map.put("username", user);
        amqpDispatcher.send("truco", "room", map);
    }

    @Override
    public TrucoRoom createRoom(TrucoRoom trucoRoom) {
        return restTemplate.postForObject(trucoServerHost + "/api/room", trucoRoom, TrucoRoom.class);
    }

    @Override
    public TrucoRoomEvent joinRoom(TrucoRoom trucoRoom) {
        return restTemplate.postForObject(trucoServerHost + "/api/room/" + trucoRoom.getId() + "/join", null, TrucoRoomEvent.class);
    }


    @Override
    public List<TrucoRoom> findAllRooms() {
        return Arrays.asList(restTemplate.getForObject(trucoServerHost + "/api/room", TrucoRoom[].class));
    }

    @Override
    public TrucoRoom findRoomById(String id) {
        return restTemplate.getForObject(trucoServerHost + "/api/room/" + id, TrucoRoom.class);
    }
}
