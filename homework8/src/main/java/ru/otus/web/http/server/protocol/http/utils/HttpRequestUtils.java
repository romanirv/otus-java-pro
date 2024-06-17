package ru.otus.web.http.server.protocol.http.utils;

import ru.otus.web.http.server.protocol.http.HttpRequest;

import java.util.Arrays;

public class HttpRequestUtils {
    public static boolean checkRequestAccept(HttpRequest request, String exceptedAcceptValue) {
        String accept = request.getHeader("Accept");
        return accept == null || Arrays.asList(accept.split(", ")).contains(exceptedAcceptValue);
    }
}
