package py.com.roshka.toluca.serviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableEurekaServer
@SpringBootApplication
public class TolucaServiceRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(TolucaServiceRegistryApplication.class, args);
    }
}