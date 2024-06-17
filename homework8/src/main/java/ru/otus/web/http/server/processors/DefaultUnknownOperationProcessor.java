package ru.otus.web.http.server.processors;

import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class DefaultUnknownOperationProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse();
        response.setStatusCode(HttpStatus.OK);
        response.setHeader("Content-Type", "text/html");
        response.setBody("<html><body><h1>UNKNOWN OPERATION REQUEST!!!</h1></body></html>");
        output.write(response.toRawResponse().getBytes(StandardCharsets.UTF_8));
    }
}
