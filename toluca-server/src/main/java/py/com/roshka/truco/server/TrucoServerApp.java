package py.com.roshka.truco.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;


//@EnableEurekaClient
//@EnableCircuitBreaker
@SpringBootApplication
@EnableScheduling
public class TrucoServerApp {
    public static void main(String[] args) {
        SpringApplication.run(TrucoServerApp.class, args);
    }
}
