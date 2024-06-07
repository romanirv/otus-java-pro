package ru.outus.db;

import ru.outus.db.data.AbstractRepository;
import ru.outus.db.data.DataSource;
import ru.outus.db.data.exception.DbError;
import ru.outus.db.data.impl.AbstractRepositoryImpl;
import ru.outus.db.data.impl.DataSourceImpl;
import ru.outus.db.entiry.Product;
import ru.outus.db.data.migrations.DbMigrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.db.services.ProductService;
import java.util.Optional;

public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static DataSource createH2FileDbDataSource() {
        return new DataSourceImpl("jdbc:h2:file:./db;MODE=PostgreSQL");
    }

    public static void main(String[] args) {
        logger.info("Start application ...");

        String dbInitSQLScriptFile = (args.length == 1) ? args[0] : "init.sql";

        DataSource dataSource = createH2FileDbDataSource();
        AbstractRepository<Product> productRepository = null;
        try {
            DbMigrator.initializeDb(dbInitSQLScriptFile, dataSource);
            logger.info("Initialize db successfully!");

            productRepository = new AbstractRepositoryImpl<>(dataSource, Product.class);
            logger.info("Create product repository successfully!");
        } catch (DbError e) {
            logger.error(e.getLocalizedMessage());
        }

        try {
            ProductService productService = new ProductService(productRepository);
            Product product1 = productService.createNewProduct("Product1","100.0");
            logger.info("Create new product {}", product1);

            Product product2 = productService.createNewProduct("Product2", "110.0");
            logger.info("Create new product {}", product2);

            Product product3 = productService.createNewProduct("Product3", "120.0");
            logger.info("Create new product {}", product3);

            if (productService.changeProductPrice(product1.getId(), "250.0")) {
                logger.info("Change product id={} price success!", product1.getId());
            }

            logger.info("Found products: {}", productService.getAllProducts());

            productService.deleteProductById(product3.getId());
            Optional<Product> deletedProduct = productService.getProductById(product3.getId());
            if (deletedProduct.isEmpty()) {
                logger.info("Product was deleted!");
            } else {
                logger.error("Product was not deleted!");
            }

            productService.deleteAllProducts();
            logger.info("Found products: {}", productService.getAllProducts());
        } catch (RuntimeException e) {
            logger.error("Exception: {}", e.getLocalizedMessage());
        }
    }
}