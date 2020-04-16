package py.com.roshka.truco.server.service;

import py.com.roshka.truco.api.*;

public interface AMQPSender {


    void convertAndSend(TrucoEvent trucoEvent);

    void joinToChannel(String channel, TrucoUser trucoUser, TrucoRoomEvent trucoEvent, String joinChannel);

    void convertAndSend(String channel, TrucoRoomEvent trucoEvent);

    void convertAndSend(String channel, TrucoRoomTableEvent trucoEvent);

    void convertAndSend(TrucoGameEvent trucoGameEvent);

    void convertAndSendDirectMessage(String userId, TrucoGameEvent trucoEvent);
}
