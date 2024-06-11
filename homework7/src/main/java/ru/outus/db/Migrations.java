package ru.outus.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.outus.db.entiry.Product;
import ru.outus.db.data.migrations.DbMigrator;

import java.util.List;


public class Migrations {

    private static final Logger logger = LoggerFactory.getLogger(Migrations.class);

    public static void main(String[] args) {
        logger.info("Migration starting...");

        String dbInitScriptFile = (args.length == 1) ? args[0] : "init.sql";
        if (DbMigrator.generateDbInitScript(dbInitScriptFile, List.of(Product.class))) {
            logger.info("Migration complete.");
        } else {
            logger.info("Migration failed.");
        }
    }
}
