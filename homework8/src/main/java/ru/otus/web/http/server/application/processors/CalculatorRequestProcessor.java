package ru.otus.web.http.server.application.processors;

import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;
import ru.otus.web.http.server.processors.RequestProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CalculatorRequestProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        HttpResponse httpResponse = new HttpResponse();
        String accept = httpRequest.getHeader("Accept");
        if (accept != null && !accept.equals("text/html")) {
            httpResponse.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
        } else {
            int a = Integer.parseInt(httpRequest.getParameter("a"));
            int b = Integer.parseInt(httpRequest.getParameter("b"));
            int result = a + b;
            String outMessage = a + " + " + b + " = " + result;
            httpResponse.setStatusCode(HttpStatus.OK);
            httpResponse.setHeader("Content-Type", "text/html");
            httpResponse.setBody("<html><body><h1>" + outMessage + "</h1></body></html>");
        }
        output.write(httpResponse.toRawResponse().getBytes(StandardCharsets.UTF_8));
    }
}
