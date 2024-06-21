package ru.otus.web.http.server.application.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.web.http.server.processors.DefaultUnknownOperationProcessor;
import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;
import ru.otus.web.http.server.processors.RequestProcessor;
import ru.otus.web.http.server.protocol.http.utils.HttpRequestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CalculatorRequestProcessor implements RequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorRequestProcessor.class.getName());

    @Override
    public void execute(String sessionId, HttpRequest request, HttpResponse response) throws IOException {
        logger.info("{} - Execute request", sessionId);

        String responseContentType = "text/html";
        if (HttpRequestUtils.checkRequestAccept(request, responseContentType)) {
            try {
                int a = Integer.parseInt(request.getParameter("a"));
                int b = Integer.parseInt(request.getParameter("b"));
                int result = a + b;
                String outMessage = a + " + " + b + " = " + result;
                response.setStatusCode(HttpStatus.OK);
                response.setHeader("Content-Type", responseContentType);
                response.setBody(("<html><body><h1>" + outMessage + "</h1></body></html>")
                        .getBytes(StandardCharsets.UTF_8));
            } catch (NumberFormatException e) {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                logger.error(" - {} - Parse param error", sessionId);
            }
        } else {
            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
