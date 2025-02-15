package ru.otus.serialization.utils;

import ru.otus.serialization.exceptions.SerializeError;
import ru.otus.serialization.utils.impl.JavaSerialize;
import ru.otus.serialization.utils.impl.JsonSerialize;
import ru.otus.serialization.utils.impl.XmlSerialize;
import ru.otus.serialization.utils.impl.YamlSerialize;

public class SerializeMethodFactory {

    static public SerializeMethod buildSerializeMethod(String contentType) throws SerializeError {
        return switch (contentType) {
            case "application/json" -> new JsonSerialize();
            case "application/xml" -> new XmlSerialize();
            case "application/yaml" -> new YamlSerialize();
            case "application/java" -> new JavaSerialize();
            default -> throw new SerializeError("Not supported 'Content-Type!'");
        };
    }
}
