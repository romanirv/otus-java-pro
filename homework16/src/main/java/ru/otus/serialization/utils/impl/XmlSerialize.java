package ru.otus.serialization.utils.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.otus.serialization.exceptions.SerializeError;
import ru.otus.serialization.utils.SerializeMethod;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlSerialize implements SerializeMethod {

    public String serialize(Object object) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new SerializeError(e.getLocalizedMessage());
        }
    }
}
