package ru.otus.web.http.server.protocol.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class.getName());

    private final String rawRequest;
    private String uri;
    private HttpMethod method;
    private final Map<String, String> parameters = new HashMap<>();
    private final Map<String, String> headers = new HashMap<>();
    private String body;

    public HttpRequest(String rawRequest) throws HttpError {
        this.rawRequest = rawRequest;

        List<String> lines = rawRequest.lines().toList();
        if (lines.isEmpty()) {
            throw new HttpError("HTTP request is empty");
        }

        int requestLineIdx = 0;
        this.parseRequestLine(lines.get(requestLineIdx));

        int requestHeaderStartIdx = requestLineIdx + 1;
        int requestHeaderEndIdx = lines.indexOf("");
        if (requestHeaderEndIdx != -1) {
            this.parseHeaders(lines, requestHeaderStartIdx, requestHeaderEndIdx);
            if (this.method == HttpMethod.POST || this.method == HttpMethod.PUT) {
                this.parseBody(lines, requestHeaderEndIdx + 1);
            }
        }

        logger.debug("\n{}", rawRequest);
        logger.trace("{} {}\nParameters: {}\n Headers: {} Body: {}", method, uri, parameters, headers, body); // TODO правильно все поназывать
    }

    public String getRawRequest() {
        return rawRequest;
    }

    public String getRouteKey() {
        return String.format("%s %s", method, uri);
    }

    public String getUri() {
        return uri;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public String getBody() {
        return body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    private void parseBody(List<String> requestLines, int start) {
        if (start > -1) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = start; i < requestLines.size(); i++) {
                stringBuilder.append(requestLines.get(i));
            }
            this.body = stringBuilder.toString();
        }
    }

    private void parseHeaders(List<String> requestLines, int start, int end) throws HttpError {
        for (int i = start; i < end; ++i) {
            String headerLine = requestLines.get(i);
            String[] parts = headerLine.split( ": ");
            if (parts.length != 2) {
                throw new HttpError("HTTP request error: parse header error!");
            }
            headers.put(parts[0], parts[1]);
        }
    }

    private void parseRequestLine(String requestLine) throws HttpError {
        int startIndex = requestLine.indexOf(' ');
        int endIndex = requestLine.indexOf(' ', startIndex + 1);
        if (startIndex == -1 || endIndex == -1) {
            throw new HttpError("HTTP request error: parse request line failed!");
        }
        this.uri = requestLine.substring(startIndex + 1, endIndex);
        this.method = HttpMethod.valueOf(requestLine.substring(0, startIndex));
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            this.uri = elements[0];
            if (elements.length > 1) {
                String[] keysValues = elements[1].split("&");
                for (String o : keysValues) {
                    String[] keyValue = o.split("=");
                    this.parameters.put(keyValue[0], keyValue[1]);
                }
            }
        }
    }
}
