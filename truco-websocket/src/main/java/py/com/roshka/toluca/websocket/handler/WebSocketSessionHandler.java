package py.com.roshka.toluca.websocket.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class WebSocketSessionHandler implements WebSocketSession {
    private String userId;
    private String username;
    private WebSocketSession target;
    private boolean fired = false;
    private String sessionId;

    public WebSocketSessionHandler(String userId, String username, WebSocketSession webSocketSession) {
        this.userId = userId;
        this.username = username;
        this.target = webSocketSession;
        this.sessionId = webSocketSession.getId();
    }

    public void setFired(boolean fired) {
        this.fired = fired;
    }

    public boolean isFired() {
        return fired;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    @Override
    public String getId() {
        return target.getId();
    }

    @Override
    public URI getUri() {
        return target.getUri();
    }

    @Override
    public HttpHeaders getHandshakeHeaders() {
        return target.getHandshakeHeaders();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return target.getAttributes();
    }

    @Override
    public Principal getPrincipal() {
        return target.getPrincipal();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return target.getLocalAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return target.getRemoteAddress();
    }

    @Override
    public String getAcceptedProtocol() {
        return target.getAcceptedProtocol();
    }

    @Override
    public void setTextMessageSizeLimit(int i) {
        target.setTextMessageSizeLimit(i);
    }

    @Override
    public int getTextMessageSizeLimit() {
        return target.getTextMessageSizeLimit();
    }

    @Override
    public void setBinaryMessageSizeLimit(int i) {
        target.setBinaryMessageSizeLimit(i);
    }

    @Override
    public int getBinaryMessageSizeLimit() {
        return target.getBinaryMessageSizeLimit();
    }

    @Override
    public List<WebSocketExtension> getExtensions() {
        return target.getExtensions();
    }

    @Override
    public void sendMessage(WebSocketMessage<?> webSocketMessage) throws IOException {
        target.sendMessage(webSocketMessage);
    }

    @Override
    public boolean isOpen() {
        return target.isOpen();
    }

    @Override
    public void close() throws IOException {
        target.close();
    }

    @Override
    public void close(CloseStatus closeStatus) throws IOException {
        target.close(closeStatus);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebSocketSessionHandler that = (WebSocketSessionHandler) o;
        return Objects.equals(sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sessionId);
    }
}