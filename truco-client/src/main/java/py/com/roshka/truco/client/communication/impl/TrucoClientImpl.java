package py.com.roshka.truco.client.communication.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.client.communication.TrucoClient;
import py.com.roshka.truco.client.communication.TrucoClientHandler;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class TrucoClientImpl implements TrucoClient, WebSocketClientListener {
    Logger logger = Logger.getLogger(TrucoClientImpl.class);

    String serverHost;
    String websocketHost;
    private WebSocketClient webSocketClient;
    private String authentication;
    private ObjectMapper objectMapper = new ObjectMapper();
    private TrucoClientHandler trucoClientHandler;


    public TrucoClientImpl(String serverHost, String websocketHost) {
        this.serverHost = serverHost;
        this.websocketHost = websocketHost;
    }

    public TrucoPrincipal login(String username, String password, TrucoClientHandler trucoClientHandler) throws TrucoClientException {
        this.trucoClientHandler = trucoClientHandler;
        //-H 'Content-Type: application/json' -d '{"username":"user","password":"password"}'
        URL url = null;
        try {
            Map map = new LinkedHashMap();
            map.put("username", username);
            map.put("password", password);
            url = new URL(serverHost + "/auth/login");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(objectMapper.writeValueAsString(map));
            out.flush();
            out.close();
            int status = con.getResponseCode();
            if (status != 200) {
                trucoClientHandler.loginFailed();
                throw new TrucoClientException("Login Incorrecto");

            } else {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();
                TrucoPrincipal trucoPrincipal = objectMapper.readValue(content.toString(), TrucoPrincipal.class);
                authentication = trucoPrincipal.getAuthKey();
                trucoClientHandler.afterLogin(trucoPrincipal);
                return trucoPrincipal;
            }

        } catch (Exception e) {

            throw new TrucoClientException("Error al intentar realizar login", e);
        }
    }

    public void connect() throws TrucoClientException {
        try {
            String uri = websocketHost + "/ws";
            logger.debug("Connecting to WS [" + uri + "][" + authentication + "]");
            Map headers = new LinkedHashMap();
            headers.put("Authentication", authentication);
            webSocketClient = new WebSocketClient(
                    new URI(uri),
                    objectMapper,
                    this,
                    headers
            );
            webSocketClient.connect();
        } catch (Exception e) {
            throw new TrucoClientException("Could not be connected", e);
        }
    }

    @Override
    public void send(String commandName, Object commandData) throws TrucoClientException {
        logger.debug("Message is sending [" + commandName + "][" + commandData + "]");
        Map request = new LinkedHashMap();
        request.put("command", commandName);
        request.put("data", commandData);
        try {
            webSocketClient.send(objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            throw new TrucoClientException("Message was not be sent", e);
        }
    }

    @Override
    public void onOpen(WebSocketClient webSocketClient) {
        logger.debug("on open");
        trucoClientHandler.ready();
    }

    @Override
    public void onClose(WebSocketClient webSocketClient) {
        logger.debug("on close");
    }

    @Override
    public boolean onMessage(WebSocketClient webSocketClient, Map map) {
        logger.debug("onmessage [" + map + "]");
        trucoClientHandler.receiveMessage(map);
        return false;
    }

    @Override
    public void onError(Exception ex) {
        logger.debug("onError", ex);
    }
}
