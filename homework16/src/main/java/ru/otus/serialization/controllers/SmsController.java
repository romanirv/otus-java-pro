package ru.otus.serialization.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.serialization.exceptions.SerializeError;
import ru.otus.serialization.services.SmsService;
import ru.otus.serialization.utils.SerializeMethod;
import ru.otus.serialization.utils.SerializeMethodFactory;

import java.util.logging.Logger;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sms")
public class SmsController {
    private final Logger logger = Logger.getLogger(SmsController.class.getName());
    private final SmsService productService;

    @GetMapping("/statistics")
    public ResponseEntity<String> getSmsStatistics(@RequestHeader("Content-Type") String contentType) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", contentType);
        try {
            SerializeMethod serializeMethod = SerializeMethodFactory.buildSerializeMethod(contentType);
            return new ResponseEntity<>(serializeMethod.serialize(productService.getStatistics()), responseHeaders,
                    HttpStatus.OK);
        } catch (SerializeError e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
