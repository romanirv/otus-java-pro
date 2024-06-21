package ru.otus.web.http.server.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DefaultUnknownOperationProcessor implements RequestProcessor {
    private static final Logger logger = LoggerFactory.getLogger(DefaultUnknownOperationProcessor.class.getName());


    @Override
    public void execute(String sessionId, HttpRequest request, HttpResponse response) throws IOException {
        logger.info("{} - Execute request", sessionId);

        response.setStatusCode(HttpStatus.OK);
        response.setHeader("Content-Type", "text/html");
        response.setBody("<html><body><h1>UNKNOWN OPERATION REQUEST!!!</h1></body></html>"
                .getBytes(StandardCharsets.UTF_8));
    }
}
