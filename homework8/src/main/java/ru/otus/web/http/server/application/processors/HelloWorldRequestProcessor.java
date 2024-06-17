package ru.otus.web.http.server.application.processors;

import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;
import ru.otus.web.http.server.processors.RequestProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HelloWorldRequestProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        // CRLF
//        String response = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<html><body><h1>Hello World!!!</h1></body></html>";
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(HttpStatus.OK);
        httpResponse.setHeader("Content-Type", "text/html");
        httpResponse.setBody("<html><body><h1>Hello World!!!</h1></body></html>");
        output.write(httpResponse.toRawResponse().getBytes(StandardCharsets.UTF_8));
        output.flush();
    }
}
