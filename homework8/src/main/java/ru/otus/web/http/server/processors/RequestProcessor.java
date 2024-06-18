package ru.otus.web.http.server.processors;

import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;

import java.io.IOException;


public interface RequestProcessor {
    void execute(String sessionId, HttpRequest httpRequest, HttpResponse httpResponse) throws IOException;
}
