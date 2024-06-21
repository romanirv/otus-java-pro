package ru.otus.web.http.server.protocol.http.utils;

import ru.otus.web.http.server.protocol.http.HttpRequest;

import java.util.Arrays;
import java.util.Optional;

public class HttpRequestUtils {
    public static boolean checkRequestAccept(HttpRequest request, String exceptedAcceptValue) {
        Optional<String> accept = request.getHeader("Accept");
        return accept.isEmpty() || Arrays.asList(accept.get().split(", ")).contains(exceptedAcceptValue);
    }

    private HttpRequestUtils() { }
}
