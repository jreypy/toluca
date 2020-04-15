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
import py.com.roshka.truco.api.request.RoomRequest;
import py.com.roshka.truco.api.request.StartGameRequest;
import py.com.roshka.truco.api.request.TablePositionRequest;

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
    public Map createRoom(TrucoRoom trucoRoom) {
        return restTemplate.postForObject(trucoServerHost + "/api/room", trucoRoom, Map.class);
    }

    @Override
    public Map joinRoom(RoomRequest roomRequest) {
        return restTemplate.postForObject(trucoServerHost + "/api/room/" + roomRequest.getRoomId() + "/join", null, Map.class);
    }


    @Override
    public Map findAllRooms() {
        return restTemplate.getForObject(trucoServerHost + "/api/room", Map.class);
    }

    @Override
    public Map createRoomTable(TrucoRoomTable trucoRoomTable) {
        return restTemplate.postForObject(trucoServerHost + "/api/room/" + trucoRoomTable.getRoomId() + "/table", trucoRoomTable, Map.class);
    }

    @Override
    public Map setTablePosition(TablePositionRequest tablePositionRequest) {
        return putForObject(trucoServerHost + "/api/room/" + tablePositionRequest.getRoomId() + "/table/" + tablePositionRequest.getTableId() + "/position/" + tablePositionRequest.getPosition(), null, Map.class);
    }

    @Override
    public Map joinRoomTable(JoinRoomTableRequest tablePositionRequest) {
        return restTemplate.postForObject(trucoServerHost + "/api/room/" + tablePositionRequest.getRoomId() + "/table/" + tablePositionRequest.getTableId() + "/join", null, Map.class);
    }

    @Override
    public Map startGame(StartGameRequest startGameRequest) {
        return restTemplate.postForObject(trucoServerHost + "/api/room/" + startGameRequest.getRoomId() + "/table/" + startGameRequest.getTableId() + "/start-game", null, Map.class);
    }

    @Override
    public Map play(TrucoGamePlay trucoGamePlay) {
        return restTemplate.postForObject(trucoServerHost + "/api/room/" + trucoGamePlay.getRoomId() + "/table/" + trucoGamePlay.getTableId() + "/play", trucoGamePlay, Map.class);
    }

    @Override
    public Map findRoomById(String id) {
        return restTemplate.getForObject(trucoServerHost + "/api/room/" + id, Map.class);
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
