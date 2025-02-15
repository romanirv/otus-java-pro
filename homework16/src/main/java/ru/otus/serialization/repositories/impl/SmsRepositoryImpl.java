package ru.otus.serialization.repositories.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.serialization.entities.SmsInfo;
import ru.otus.serialization.repositories.SmsRepository;

import java.net.URL;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Component
@NoArgsConstructor
public class SmsRepositoryImpl implements SmsRepository {

    @Value("${data.filename}")
    private String smsFilename;

    private SmsInfo smsInfo;

    private final static Logger logger = Logger.getLogger(SmsRepositoryImpl.class.getName());

    @Override
    public SmsInfo getSmsInfo() {
        return smsInfo;
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing repository. Try read data...");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            URL url = getClass().getClassLoader().getResource(smsFilename);
            if (url != null) {
                smsInfo = objectMapper.readValue(Paths.get(url.getFile()).toFile(), SmsInfo.class);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
