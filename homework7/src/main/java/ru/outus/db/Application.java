package ru.outus.db;

import ru.outus.db.data.DataSource;
import ru.outus.db.data.impl.AbstractRepositoryImpl;
import ru.outus.db.data.impl.DataSourceImpl;
import ru.outus.db.entiry.Product;
import ru.outus.db.migrations.DbMigrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.db.services.ProductService;

import java.math.BigDecimal;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Start application ...");

        String dbInitScriptFile = (args.length == 1) ? args[0] : "init.sql";

        try {
            DataSource dataSource = new DataSourceImpl("jdbc:h2:file:./db;MODE=PostgreSQL");
            if (!DbMigrator.initializeDb(dbInitScriptFile, dataSource)) {
                logger.error("Cannot initialize db!");
                return;
            }
            logger.info("Initialize db successfully!");

            ProductService productService = new ProductService(new AbstractRepositoryImpl<>(dataSource, Product.class));
            productService.createNewProduct("Product1", new BigDecimal("100.0"));
            productService.createNewProduct("Product2", new BigDecimal("110.0"));
            productService.createNewProduct("Product3", new BigDecimal("120.0"));
            productService.createNewProduct("Product4", new BigDecimal("130.0"));
            productService.createNewProduct("Product5", new BigDecimal("140.0"));
            logger.info("Found products: {}", productService.getAllProducts());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}