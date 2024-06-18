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
import ru.otus.web.http.server.protocol.http.HttpMethod;
import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class.getName());

    private final String staticResourcesPath;
    private final Map<String, Map<HttpMethod, RequestProcessor>> router;
    private final RequestProcessor unknownOperationRequestProcessor;
    private final RequestProcessor optionsRequestProcessor;
    private final RequestProcessor staticResourcesProcessor;

    public Dispatcher(String staticResourcesPath) {
        this.staticResourcesPath = staticResourcesPath;

        this.router = new HashMap<>();
        this.router.put("/calc",  Map.of(HttpMethod.GET, new CalculatorRequestProcessor()));
        this.router.put("/hello", Map.of(HttpMethod.GET, new HelloWorldRequestProcessor()));
        this.router.put("/items", Map.of(HttpMethod.GET, new GetAllProductsProcessor(),
                                         HttpMethod.POST, new CreateNewProductProcessor()));

        this.unknownOperationRequestProcessor = new DefaultUnknownOperationProcessor();
        this.optionsRequestProcessor = new DefaultOptionsProcessor();
        this.staticResourcesProcessor = new DefaultStaticResourcesProcessor();

        logger.info("Dispatcher init success");
    }

    public void execute(String  sessionId, HttpRequest request, HttpResponse response) throws IOException {
        if (request.getMethod() == HttpMethod.OPTIONS) {
            optionsRequestProcessor.execute(sessionId, request, response);
            return;
        }
        if (Files.exists(Paths.get(this.staticResourcesPath, request.getUri().substring(1)))) {
            staticResourcesProcessor.execute(sessionId, request, response);
            return;
        }

        if (router.containsKey(request.getUri())) {
            Map<HttpMethod, RequestProcessor> handlers = router.get(request.getUri());
            if (handlers.containsKey(request.getMethod())) {
                RequestProcessor requestProcessor = handlers.get(request.getMethod());
                requestProcessor.execute(sessionId, request, response);
            } else {
                logger.debug("HTTP method {} not allowed:", request.getMethod());
                response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
            }
        } else {
            unknownOperationRequestProcessor.execute(sessionId, request, response);
        }
    }
}
