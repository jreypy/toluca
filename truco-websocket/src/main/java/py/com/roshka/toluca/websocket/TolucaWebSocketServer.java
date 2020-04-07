package py.com.roshka.toluca.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class TolucaWebSocketServer {
    public static void main(String[] args) {
        SpringApplication.run(TolucaWebSocketServer.class, args);
    }
}
