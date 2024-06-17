package ru.otus.web.http.server.application.processors;

import com.google.gson.Gson;
import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;
import ru.otus.web.http.server.application.Item;
import ru.otus.web.http.server.application.Storage;
import ru.otus.web.http.server.processors.RequestProcessor;
import ru.otus.web.http.server.protocol.http.utils.HttpRequestUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetAllProductsProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse();
        String responseContentType = "application/json";
        if (HttpRequestUtils.checkRequestAccept(httpRequest, responseContentType)) {
            List<Item> items = Storage.getItems();
            Gson gson = new Gson();
            response.setStatusCode(HttpStatus.OK);
            response.setHeader("Content-Type", responseContentType);
            response.setHeader("Connection", "keep-alive");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setBody(gson.toJson(items));
        } else {
            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
        }
        output.write(response.toRawResponse().getBytes(StandardCharsets.UTF_8));
        output.flush();
    }
}
