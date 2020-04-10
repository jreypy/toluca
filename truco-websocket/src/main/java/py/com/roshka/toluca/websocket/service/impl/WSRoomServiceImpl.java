package py.com.roshka.toluca.websocket.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import py.com.roshka.toluca.websocket.service.AMQPDispatcher;
import py.com.roshka.toluca.websocket.service.RoomService;
import py.com.roshka.truco.api.TrucoRoom;
import py.com.roshka.truco.api.TrucoRoomEvent;
import py.com.roshka.truco.api.TrucoRoomTable;
import py.com.roshka.truco.api.TrucoRoomTableEvent;
import py.com.roshka.truco.api.request.SitDownRequest;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Component
public class WSRoomServiceImpl implements RoomService {

    private final RestTemplate restTemplate;

    @Value("${trucoServerHost}")
    private String trucoServerHost;

    public WSRoomServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

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
    public TrucoRoomTable createRoomTable(TrucoRoomTable trucoRoomTable) {
        return restTemplate.postForObject(trucoServerHost + "/api/room/" + trucoRoomTable.getRoomId() + "/table", trucoRoomTable, TrucoRoomTable.class);
    }

    @Override
    public TrucoRoomTableEvent sitDownTable(SitDownRequest sitDownRequest) {
        return putForObject(trucoServerHost + "/api/room/" + sitDownRequest.getRoomId() + "/table/" + sitDownRequest.getTableId() + "/position/" + sitDownRequest.getChair(), null, TrucoRoomTableEvent.class);
    }

    @Override
    public TrucoRoom findRoomById(String id) {
        return restTemplate.getForObject(trucoServerHost + "/api/room/" + id, TrucoRoom.class);
    }

    private <T> T putForObject(String url, Object o, java.lang.Class<T> responseType) {
        return requestForObject(url, HttpMethod.PUT, o, responseType);
    }

    private <T> T requestForObject(String url, HttpMethod method, Object o, java.lang.Class<T> responseType) {
        HttpEntity request = new HttpEntity(o);
        ResponseEntity<T> response = restTemplate
                .exchange(url, method, request, responseType);
        return response.getBody();
    }


}
