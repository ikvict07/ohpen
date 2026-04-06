package com.ohpenl.midoffice.configurationtracker.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;

@Configuration
public class NotificationClientConfiguration {

    @Bean
    public NotificationClient notificationClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://mock-notification-service")
                .requestInterceptor((request, body, execution) -> {
                    @SuppressWarnings("java:S2119")
                    boolean isError = new Random().nextBoolean();
                    //noinspection NullableProblems
                    return new ClientHttpResponse() {
                        @Override
                        public HttpStatusCode getStatusCode() {
                            return isError ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;
                        }

                        @Override
                        public String getStatusText() {
                            return isError ? "Internal Server Error" : "OK";
                        }

                        @Override
                        public void close() {
                            // Nothing to do
                        }

                        @Override
                        public InputStream getBody() {
                            return new ByteArrayInputStream(new byte[0]);
                        }

                        @Override
                        public HttpHeaders getHeaders() {
                            return new HttpHeaders();
                        }
                    };
                })
                .build();

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build()
                .createClient(NotificationClient.class);
    }
}
