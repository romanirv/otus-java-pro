package ru.otus.web.http.server.application.processors;

import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;
import ru.otus.web.http.server.processors.RequestProcessor;
import ru.otus.web.http.server.protocol.http.utils.HttpRequestUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CalculatorRequestProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        String responseContentType = "text/html";
        HttpResponse httpResponse = new HttpResponse();
        if (HttpRequestUtils.checkRequestAccept(httpRequest, responseContentType)) {
            try {
                int a = Integer.parseInt(httpRequest.getParameter("a"));
                int b = Integer.parseInt(httpRequest.getParameter("b"));
                int result = a + b;
                String outMessage = a + " + " + b + " = " + result;
                httpResponse.setStatusCode(HttpStatus.OK);
                httpResponse.setHeader("Content-Type", responseContentType);
                httpResponse.setBody("<html><body><h1>" + outMessage + "</h1></body></html>");
            } catch (NumberFormatException e) {
                httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
                e.printStackTrace();
            }
        } else {
            httpResponse.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
        }
        output.write(httpResponse.toRawResponse().getBytes(StandardCharsets.UTF_8));
        output.flush();
    }
}
