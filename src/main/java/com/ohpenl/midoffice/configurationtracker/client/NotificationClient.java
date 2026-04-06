package com.ohpenl.midoffice.configurationtracker.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@HttpExchange(
        accept = {APPLICATION_JSON_VALUE},
        contentType = APPLICATION_JSON_VALUE
)
public interface NotificationClient {

    @PostExchange("/notifications")
    void sendNotification(@RequestBody NotificationRequest request);
}
