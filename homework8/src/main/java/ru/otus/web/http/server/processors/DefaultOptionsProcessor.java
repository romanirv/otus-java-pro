package ru.otus.web.http.server.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;

import java.io.IOException;

public class DefaultOptionsProcessor implements RequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(DefaultOptionsProcessor.class.getName());

    @Override
    public void execute(String sessionId, HttpRequest request, HttpResponse response) throws IOException {
        logger.info("{} - Execute request", sessionId);

        response.setStatusCode(HttpStatus.NO_CONTENT);
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Max-Age", "86400");
    }
}
