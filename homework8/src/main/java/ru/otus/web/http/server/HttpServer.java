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
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private int port;
    private String staticResourcePath;
    private Dispatcher dispatcher;
    private ExecutorService executorService;
    private ThreadLocal<byte[]> requestBuffer;

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class.getName());

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public HttpServer(int port, String staticResourcePath) {
        this.port = port;
        this.staticResourcePath = staticResourcePath;
    }

    public void start() {
        executorService = Executors.newFixedThreadPool(4);
        requestBuffer = new ThreadLocal<>();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Сервер запущен на порту: {}", port);
            this.dispatcher = new Dispatcher(this.staticResourcePath);
            Storage.init();
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(() -> executeRequest(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    private void executeRequest(Socket socket) {
        try {
            if (requestBuffer.get() == null) {
                requestBuffer.set(new byte[DEFAULT_BUFFER_SIZE]);
            }
            byte[] buffer = requestBuffer.get();
            int n = socket.getInputStream().read(buffer);
            if (n > 0) {
                String rawRequest = new String(buffer, 0, n);
                Optional<HttpRequest> request = parseRequest(rawRequest);
                if (request.isEmpty()) {
                    HttpResponse httpResponse = new HttpResponse();
                    httpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
                    socket.getOutputStream().write(httpResponse.toRawResponse().getBytes(StandardCharsets.UTF_8));
                } else {
                    dispatcher.execute(request.get(), socket.getOutputStream());
                }
                socket.getOutputStream().flush();
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
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
