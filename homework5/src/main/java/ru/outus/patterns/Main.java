package ru.outus.patterns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.patterns.entity.Box;
import ru.outus.patterns.entity.Product;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Iterator;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Product product = Product.builder()
                .id(1L)
                .title("title")
                .description("description")
                .cost(new BigDecimal(20))
                .weight(10L)
                .width(100L)
                .length(10000L)
                .height(200L)
                .build();

        logger.info("Product [id: " + product.id()  + ", " +
                "title: " + product.title() + ", " +
                "description: " + product.description() + ", " +
                "cost: " + product.cost() + ", " +
                "weight: " + product.weight() + ", " +
                "width: " + product.width() + ", " +
                "length: " + product.length() + ", " +
                "height: " + product.height() + "]");


        Box box = new Box(
                Arrays.asList("a", "b"),
                Arrays.asList("c", "d", "d"),
                Arrays.asList("e", "f", "ff"),
                Arrays.asList("g", "gg"));
        Iterator<String> it = box.iterator();
        while (it.hasNext()) {
            logger.info(it.next());
        }
    }
}
