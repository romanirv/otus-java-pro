package ru.otus.web.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.web.http.server.processors.DefaultOptionsProcessor;
import ru.otus.web.http.server.processors.DefaultStaticResourcesProcessor;
import ru.otus.web.http.server.processors.RequestProcessor;
import ru.otus.web.http.server.processors.DefaultUnknownOperationProcessor;
import ru.otus.web.http.server.application.processors.CalculatorRequestProcessor;
import ru.otus.web.http.server.application.processors.CreateNewProductProcessor;
import ru.otus.web.http.server.application.processors.GetAllProductsProcessor;
import ru.otus.web.http.server.application.processors.HelloWorldRequestProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private Map<String, RequestProcessor> router;
    private RequestProcessor unknownOperationRequestProcessor;
    private RequestProcessor optionsRequestProcessor;
    private RequestProcessor staticResourcesProcessor;

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class.getName());

    public Dispatcher() {
        this.router = new HashMap<>();
        this.router.put("GET /calc", new CalculatorRequestProcessor());
        this.router.put("GET /hello", new HelloWorldRequestProcessor());
        this.router.put("GET /items", new GetAllProductsProcessor());
        this.router.put("POST /items", new CreateNewProductProcessor());

        this.unknownOperationRequestProcessor = new DefaultUnknownOperationProcessor();
        this.optionsRequestProcessor = new DefaultOptionsProcessor();
        this.staticResourcesProcessor = new DefaultStaticResourcesProcessor();

        logger.info("Диспетчер проинициализирован");
    }

    public void execute(HttpRequest httpRequest, OutputStream outputStream) throws IOException {
        if (httpRequest.getMethod() == HttpMethod.OPTIONS) {
            optionsRequestProcessor.execute(httpRequest, outputStream);
            return;
        }
        if (Files.exists(Paths.get("static/", httpRequest.getUri().substring(1)))) {
            staticResourcesProcessor.execute(httpRequest, outputStream);
            return;
        }
        if (!router.containsKey(httpRequest.getRouteKey())) {
            unknownOperationRequestProcessor.execute(httpRequest, outputStream);
            return;
        }
        router.get(httpRequest.getRouteKey()).execute(httpRequest, outputStream);
    }
}
