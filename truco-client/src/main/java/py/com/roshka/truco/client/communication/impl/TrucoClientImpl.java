package py.com.roshka.truco.client.communication.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import py.com.roshka.truco.api.TrucoPrincipal;
import py.com.roshka.truco.client.communication.TrucoClient;
import py.com.roshka.truco.client.communication.exception.TrucoClientException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class TrucoClientImpl implements TrucoClient {

    String serverHost;
    String websocketHost;

    private ObjectMapper objectMapper = new ObjectMapper();

    public TrucoClientImpl(String serverHost, String websocketHost) {
        this.serverHost = serverHost;
        this.websocketHost = websocketHost;
    }

    public TrucoPrincipal login(String username, String password) throws TrucoClientException {
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
                throw new TrucoClientException("Login Incorrecto");
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            return objectMapper.readValue(content.toString(), TrucoPrincipal.class);
        } catch (Exception e) {
            throw new TrucoClientException("Error al intentar realizar login", e);
        }
    }
}
