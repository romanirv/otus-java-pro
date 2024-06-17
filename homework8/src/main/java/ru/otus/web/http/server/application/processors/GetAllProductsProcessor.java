package ru.otus.web.http.server.application.processors;

import com.google.gson.Gson;
import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;
import ru.otus.web.http.server.application.Item;
import ru.otus.web.http.server.application.Storage;
import ru.otus.web.http.server.processors.RequestProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetAllProductsProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        List<Item> items = Storage.getItems();
        Gson gson = new Gson();
//        String result = "HTTP/1.1 200 OK\r\n" +
//                "Content-Type: application/json\r\n" +
//                "Connection: keep-alive\r\n" +
//                "Access-Control-Allow-Origin: *\r\n\r\n" + gson.toJson(items);

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(HttpStatus.OK);
        httpResponse.setHeader("Content-Type", "application/json");
        httpResponse.setHeader("Connection", "keep-alive");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setBody(gson.toJson(items));

        output.write(httpResponse.toRawResponse().getBytes(StandardCharsets.UTF_8));
        output.flush();
    }
}
