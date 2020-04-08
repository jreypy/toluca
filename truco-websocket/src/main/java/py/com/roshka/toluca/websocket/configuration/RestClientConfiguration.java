package py.com.roshka.toluca.websocket.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import py.com.roshka.truco.api.TrucoPrincipal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestClientConfiguration {
    Logger logger = LoggerFactory.getLogger(RestClientConfiguration.class);

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
                logger.debug("Adding Authentication Header");
                SecurityContext sc = SecurityContextHolder.getContext();
                httpRequest.getHeaders().add("Authentication", ((TrucoPrincipal) sc.getAuthentication().getPrincipal()).getAuthKey());

                ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, body);
                return response;
            }
        });
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

}