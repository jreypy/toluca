package py.com.roshka.toluca.websocket.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import py.com.roshka.toluca.websocket.service.RoomService;
import py.com.roshka.truco.api.*;
import py.com.roshka.truco.api.request.JoinRoomTableRequest;
import py.com.roshka.truco.api.request.StartGameRequest;
import py.com.roshka.truco.api.request.TablePositionRequest;

import java.util.Arrays;
import java.util.List;


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
    public TrucoRoomTableEvent setTablePosition(TablePositionRequest tablePositionRequest) {
        return putForObject(trucoServerHost + "/api/room/" + tablePositionRequest.getRoomId() + "/table/" + tablePositionRequest.getTableId() + "/position/" + tablePositionRequest.getChair(), null, TrucoRoomTableEvent.class);
    }

    @Override
    public TrucoRoomTableEvent joinRoomTable(JoinRoomTableRequest tablePositionRequest) {
        return restTemplate.postForObject(trucoServerHost + "/api/room/" + tablePositionRequest.getRoomId() + "/table/" + tablePositionRequest.getTableId() + "/join", null, TrucoRoomTableEvent.class);
    }

    @Override
    public TrucoGameEvent startGame(StartGameRequest startGameRequest) {
        return restTemplate.postForObject(trucoServerHost + "/api/room/" + startGameRequest.getRoomId() + "/table/" + startGameRequest.getTableId() + "/start-game", null, TrucoGameEvent.class);
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
