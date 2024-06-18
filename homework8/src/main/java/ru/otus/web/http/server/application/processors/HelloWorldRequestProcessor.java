package ru.otus.web.http.server.application.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;
import ru.otus.web.http.server.processors.RequestProcessor;
import ru.otus.web.http.server.protocol.http.utils.HttpRequestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HelloWorldRequestProcessor implements RequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldRequestProcessor.class.getName());

    @Override
    public void execute(String sessionId, HttpRequest request, HttpResponse response) throws IOException {
        logger.info("{} - Execute HelloWorldRequestProcessor", sessionId);

        String responseContentType = "text/html";
        if (HttpRequestUtils.checkRequestAccept(request, responseContentType)) {
            response.setStatusCode(HttpStatus.OK);
            response.setHeader("Content-Type", "text/html");
            response.setBody("<html><body><h1>Hello World!!!</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        } else {
            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
