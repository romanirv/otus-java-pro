package ru.otus.web.http.server.application.processors;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;
import ru.otus.web.http.server.application.Item;
import ru.otus.web.http.server.application.Storage;
import ru.otus.web.http.server.processors.RequestProcessor;
import ru.otus.web.http.server.protocol.http.utils.HttpRequestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class GetAllProductsProcessor implements RequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(GetAllProductsProcessor.class.getName());

    @Override
    public void execute(String sessionId, HttpRequest request, HttpResponse response) throws IOException {
        logger.info("{} - Executing GetAllProductsProcessor", sessionId);

        String responseContentType = "application/json";
        if (HttpRequestUtils.checkRequestAccept(request, responseContentType)) {
            List<Item> items = Storage.getItems();
            Gson gson = new Gson();
            response.setStatusCode(HttpStatus.OK);
            response.setHeader("Content-Type", responseContentType);
            response.setHeader("Connection", "keep-alive");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setBody(gson.toJson(items).getBytes(StandardCharsets.UTF_8));
        } else {
            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
