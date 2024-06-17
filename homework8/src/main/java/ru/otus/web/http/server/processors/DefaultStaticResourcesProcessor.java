package ru.otus.web.http.server.processors;

import ru.otus.web.http.server.protocol.http.HttpRequest;
import ru.otus.web.http.server.protocol.http.HttpResponse;
import ru.otus.web.http.server.protocol.http.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultStaticResourcesProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        HttpResponse response = new HttpResponse();
        String filename = httpRequest.getUri().substring(1);
        Path filePath = Paths.get("static/", filename);
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);
        byte[] fileData = Files.readAllBytes(filePath);
        response.setStatusCode(HttpStatus.OK);
        response.setHeader("Content-Length", String.valueOf(fileData.length));
        if (fileType.equals("pdf")) {
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        }
        output.write(response.toRawResponse().getBytes(StandardCharsets.UTF_8));
        output.write(fileData);
    }
}
