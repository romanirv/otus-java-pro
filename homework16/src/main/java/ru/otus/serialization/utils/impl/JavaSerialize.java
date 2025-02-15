package ru.otus.serialization.utils.impl;

import ru.otus.serialization.exceptions.SerializeError;
import ru.otus.serialization.utils.SerializeMethod;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class JavaSerialize implements SerializeMethod {

    public String serialize(Object object) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream ois = new ObjectOutputStream(baos)) {

            ois.writeObject(object);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            throw new SerializeError(e.getLocalizedMessage());
        }
    }
}
