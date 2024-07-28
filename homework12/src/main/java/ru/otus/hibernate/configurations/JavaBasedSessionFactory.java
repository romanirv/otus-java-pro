package ru.otus.hibernate.configurations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.hibernate.entities.Customer;
import ru.otus.hibernate.entities.Product;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JavaBasedSessionFactory {

    private static final Log LOGGER = LogFactory.getLog(MethodHandles.lookup().lookupClass());

    public static SessionFactory getSessionFactory(Map<String, String> envMap) throws ExceptionInInitializerError {
        Properties properties = getProperties(envMap);
        try {
            Configuration configuration = new Configuration();
            configuration.setProperties(properties);

            configuration.addAnnotatedClass(Customer.class);
            configuration.addAnnotatedClass(Product.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            LOGGER.info("Hibernate Java Config serviceRegistry created");

            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            LOGGER.error("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static Properties getProperties(Map<String, String> envMap) throws ExceptionInInitializerError {
        Properties props = new Properties();
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver");

        if (!envMap.containsKey("POSTGRES_CONNECTION_URL")) {
            throw new ExceptionInInitializerError("'POSTGRES_CONNECTION_URL' is not defined!");
        }
        props.put("hibernate.connection.url", envMap.get("POSTGRES_CONNECTION_URL"));

        if (!envMap.containsKey("POSTGRES_USER")) {
            throw new ExceptionInInitializerError("'POSTGRES_USER' is not defined!");
        }
        props.put("hibernate.connection.username", envMap.get("POSTGRES_USER"));

        if (!envMap.containsKey("POSTGRES_PASSWORD")) {
            throw new ExceptionInInitializerError("'POSTGRES_PASSWORD' is not defined");
        }
        props.put("hibernate.connection.password", envMap.get("POSTGRES_PASSWORD"));

        props.put("hibernate.current_session_context_class", "thread");
        props.put("hibernate.hbm2ddl.auto", "create");
        props.put("hibernate.show_sql", "true");

        return props;
    }

}
