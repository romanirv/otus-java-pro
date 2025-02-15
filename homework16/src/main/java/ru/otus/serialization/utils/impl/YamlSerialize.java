package ru.otus.serialization.utils.impl;

import org.yaml.snakeyaml.Yaml;
import ru.otus.serialization.exceptions.SerializeError;
import ru.otus.serialization.utils.SerializeMethod;

public class YamlSerialize implements SerializeMethod {

    public String serialize(Object object) {
        try {
            Yaml yaml = new Yaml();
            return yaml.dump(object);
        } catch (Exception e) {
            throw new SerializeError(e.getLocalizedMessage());
        }
    }
}
