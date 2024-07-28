package ru.otus.hibernate.services.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.otus.hibernate.entities.Customer;
import ru.otus.hibernate.entities.Product;
import ru.otus.hibernate.repository.AbstractRepository;
import ru.otus.hibernate.services.ShoppingService;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

public class ShoppingServiceImpl implements ShoppingService {
    private static final Log LOGGER = LogFactory.getLog(MethodHandles.lookup().lookupClass());

    private final AbstractRepository<Product> productRepository;

    private final AbstractRepository<Customer> customerRepository;

    public ShoppingServiceImpl(AbstractRepository<Product> productRepository,
                               AbstractRepository<Customer> customerRepository) {
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }
    public void start() {
        LOGGER.info("Start service");
        showSupportedCommands();
    }

    private void showSupportedCommands() {
        System.out.println("Supported commands:");
        System.out.println("===================");
        Arrays.stream(CommandType.values()).forEach(c ->
                System.out.println("'" + c.command + "' - " + c.getDescription()));
    }

    enum CommandType {
        SHOW_ALL_PRODUCTS("show all products"),
        SHOW_ALL_CUSTOMERS("show all customers"),
        CUSTOMER_BUY_PRODUCT("customer buy product"),
        SHOW_BOUGHT_BY_CUSTOMER_PRODUCTS("show bought by customer products"),
        SHOW_CUSTOMERS_BOUGHT_PRODUCT("show customers bought the product"),
        DELETE_PRODUCT_BY_ID("delete product by id"),
        DELETE_CUSTOMER("delete customer by id"),
        CUSTOMER_PRODUCT_DETAILS("show customer-product details"),
        EXIT("exit");

        public String getDescription() {
            switch (this) {
                case SHOW_ALL_PRODUCTS -> {
                    return "show all existing products";
                }
                case SHOW_ALL_CUSTOMERS -> {
                    return "show all existing customers";
                }
//                case CUSTOMER_BUY_PRODUCT -> {
//                    return "':'<product_id>' - add new product to cart";
//                }
                case SHOW_BOUGHT_BY_CUSTOMER_PRODUCTS -> {
                    return "':'<customer_id>' - show bought customer product";
                }
                case SHOW_CUSTOMERS_BOUGHT_PRODUCT -> {
                    return "':'<product_id> - show customers bought product";
                }
                case DELETE_PRODUCT_BY_ID -> {
                    return "delete product by id";
                }
                case EXIT -> {
                    return "exit";
                }
                default -> throw new IllegalStateException("Unexpected value: " + this);
            }
        }

        final String command;
        CommandType(String command) {
            this.command = command;
        }
    }
}
