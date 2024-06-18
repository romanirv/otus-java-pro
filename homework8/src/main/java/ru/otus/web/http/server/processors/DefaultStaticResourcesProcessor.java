package ru.otus.web.http.server.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultStaticResourcesProcessor implements RequestProcessor {
    private static final Logger logger = LoggerFactory.getLogger(DefaultStaticResourcesProcessor.class.getName());

    @Override
    public void execute(String sessionId, HttpRequest request, HttpResponse response) throws IOException {
        logger.info("{} - Execute DefaultStaticResourcesProcessor", sessionId);

        String filename = request.getUri().substring(1);
        Path filePath = Paths.get("static/", filename);
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);
        byte[] fileData = Files.readAllBytes(filePath);
        response.setStatusCode(HttpStatus.OK);
        response.setHeader("Content-Length", String.valueOf(fileData.length));
        if (fileType.equals("pdf")) {
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        }
        response.setBody(fileData);
    }
}
