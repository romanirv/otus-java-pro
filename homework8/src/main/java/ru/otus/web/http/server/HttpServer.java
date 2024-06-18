package ru.otus.web.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.web.http.server.application.Storage;
import ru.otus.web.http.server.protocol.http.HttpError;
import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HttpServer {
    private final int port;
    private final String staticResourcePath;
    private Dispatcher dispatcher;
    private ThreadLocal<byte[]> requestBuffer;

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class.getName());

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public HttpServer(int port, String staticResourcePath) {
        this.port = port;
        this.staticResourcePath = staticResourcePath;
    }

    public void start() {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        requestBuffer = new ThreadLocal<>();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Server listen port: {}", port);
            this.dispatcher = new Dispatcher(this.staticResourcePath);
            Storage.init();
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(() -> executeRequest(socket));
            }
        } catch (Exception e) {
            logger.error("Error while starting server: {}", e.getLocalizedMessage());
        } finally {
            executorService.shutdown();
        }
    }

    private void executeRequest(Socket socket) {
        try {
            logger.debug("New connection {}", socket.getRemoteSocketAddress());

            if (requestBuffer.get() == null) {
                requestBuffer.set(new byte[DEFAULT_BUFFER_SIZE]);
            }

            byte[] buffer = requestBuffer.get();
            int n = socket.getInputStream().read(buffer);
            if (n > 0) {
                HttpResponse response = new HttpResponse();
                Optional<HttpRequest> request = parseRequest(new String(buffer, 0, n));
                if (request.isEmpty()) {
                    logger.info("Parse HTTP request failed. Received bad request.");
                    response.setStatusCode(HttpStatus.BAD_REQUEST);
                } else {
                    Optional<String> sessionId = request.get().getCookie("SESSIONID");
                    if (sessionId.isEmpty()) {
                        sessionId = Optional.of(UUID.randomUUID().toString());
                        logger.info("In Cookie SESSIONID not found. Set new SESSIONID: {}", sessionId.get());
                        response.setHeader("Set-Cookie", "SESSIONID=" + sessionId.get());
                    }
                    dispatcher.execute(sessionId.get(), request.get(), response);
                }
                response.writeToStream(socket.getOutputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Optional<HttpRequest> parseRequest(String request) {
        try {
            return Optional.of(new HttpRequest(request));
        } catch (HttpError e) {
            logger.error("Error while parsing HTTP request: {}", e.getLocalizedMessage());
        }
        return Optional.empty();
    }
}
