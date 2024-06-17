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

public class CreateNewProductProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        Gson gson = new Gson();
        Item item = gson.fromJson(httpRequest.getBody(), Item.class);
        Storage.save(item);

//        String jsonOutItem = gson.toJson(item);

//        String response = "HTTP/1.1 200 OK\r\n" +
//                "Content-Type: application/json\r\n" +
//                "Connection: keep-alive\r\n" +
//                "Access-Control-Allow-Origin: *\r\n" +
//                "\r\n" + jsonOutItem;

        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(HttpStatus.OK);
        httpResponse.setHeader("Content-Type", "application/json");
        httpResponse.setHeader("Connection", "keep-alive");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setBody(gson.toJson(item));
        output.write(httpResponse.toRawResponse().getBytes(StandardCharsets.UTF_8));
    }
}
