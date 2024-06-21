package ru.otus.web.http.server.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Storage {
    private static List<Item> items;
    private static final Logger logger = LoggerFactory.getLogger(Storage.class.getName());


    public static void init() {
        logger.info("Storage init success");
        items = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            items.add(new Item("item " + i, 100 + (int)(Math.random() * 1000)));
        }
    }

    public static List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public static void save(Item item) {
        item.setId(UUID.randomUUID());
        items.add(item);
    }

    private Storage() { }
}
