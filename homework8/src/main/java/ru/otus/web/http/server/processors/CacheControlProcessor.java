package ru.otus.web.http.server.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Optional;

public class CacheControlProcessor implements RequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(CacheControlProcessor.class.getName());

    private final RequestProcessor requestProcessor;
    private final HttpResponse cachedResponse = new HttpResponse();
    private Timestamp cacheExpiredTime;
    private final Object monitor = new Object();
    private final int maxAge;
    private String responseETag = "";

    public CacheControlProcessor(RequestProcessor requestProcessor, int maxAge) {
        this.requestProcessor = requestProcessor;
        this.maxAge = maxAge;
    }

    @Override
    public void execute(String sessionId, HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        logger.info("{} - Execute request", sessionId);

        synchronized (monitor) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (cacheExpiredTime != null && cacheExpiredTime.after(now)) {
                logger.info("{} - Found cached response URI={}", sessionId, httpRequest.getUri());

                Optional<String> checkETag = httpRequest.getHeader("If-None-Match");
                if (checkETag.isPresent()) {
                    if (responseETag.equals(checkETag.get())) {
                        logger.info("{} - Resource URI={} not modified", sessionId, httpRequest.getUri());
                        httpResponse.setStatusCode(HttpStatus.NOT_MODIFIED);
                    }
                } else {
                    httpResponse.setStatusCode(cachedResponse.getStatusCode());
                    cachedResponse.getHeaders().forEach(httpResponse::setHeader);
                    httpResponse.setBody(cachedResponse.getBody());
                }

                logger.info("{} - Get response from cash URI={}", sessionId, httpRequest.getUri());
            } else {
                logger.info("{} - No cached response URI={} or expired", sessionId, httpRequest.getUri());
                requestProcessor.execute(sessionId, httpRequest, httpResponse);

                cachedResponse.setStatusCode(httpResponse.getStatusCode());
                httpResponse.getHeaders().forEach(cachedResponse::setHeader);
                cachedResponse.setBody(httpResponse.getBody());
                cacheExpiredTime = Timestamp.from(new Timestamp(System.currentTimeMillis()).toInstant().plusSeconds(maxAge));
                responseETag = hashMD5(httpResponse.getBody());
                logger.info("{} - Update cache response URI={}", sessionId, httpRequest.getUri());
            }
        }
        httpResponse.setHeader("Cache-Control", "max-age=" + maxAge + ", public");
        httpResponse.setHeader("ETag", responseETag);
    }

    private String hashMD5(byte[] data) {
        if (data.length > 0) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.reset();
                messageDigest.update(data);
                return String.format("%032x", new BigInteger(1, messageDigest.digest()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
