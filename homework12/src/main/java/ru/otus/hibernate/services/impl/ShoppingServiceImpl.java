package ru.otus.hibernate.services.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.otus.hibernate.entities.Customer;
import ru.otus.hibernate.entities.Product;
import ru.otus.hibernate.repository.AbstractRepository;
import ru.otus.hibernate.services.ShoppingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ShoppingServiceImpl implements ShoppingService {
    private static final Log LOGGER = LogFactory.getLog(MethodHandles.lookup().lookupClass());

    private final AbstractRepository<Product> productRepository;

    private final AbstractRepository<Customer> customerRepository;

    public void start() throws IOException {
        LOGGER.info("Start shopping service ...");
        showSupportedCommands();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String inputLine;
            while (true) {
                System.out.print("> ");
                inputLine = reader.readLine();
                Optional<Command> command = parseCommand(inputLine);
                if (command.isEmpty()) {
                    System.out.println("Unknown command");
                } else {
                    if (command.get().commandType == CommandType.EXIT) {
                        return;
                    }
                    handleCommand(command.get());
                }
            }
        }
    }

    private void showSupportedCommands() {
        System.out.println("Supported commands:");
        System.out.println("===================");
        Arrays.stream(CommandType.values()).forEach(c -> System.out.println(c.getDescription()));

    }

    private Optional<Command> parseCommand(String inputLine) {
        if (inputLine.isEmpty()) {
            return Optional.empty();
        }

        String[] parts = inputLine.strip().split(":");
        Optional<CommandType> commandType = Arrays.stream(CommandType.values())
                .filter(c -> c.typeName.equals(parts[0]))
                .findFirst();
        if (commandType.isEmpty()) {
            return Optional.empty();
        }

        Optional<Long> value = Optional.ofNullable(parts.length > 1 ? Long.valueOf(parts[1]) : null);
        return Optional.of(new Command(commandType.get(), value));
    }

    private void handleCommand(Command command) {
        switch (command.commandType) {
            case SHOW_ALL_PRODUCTS -> {
                showAllProductsHandle();
            }
            case SHOW_ALL_CUSTOMERS -> {
                showAllCustomersHandle();
            }
            case SHOW_BOUGHT_BY_CUSTOMER_PRODUCTS -> {
                if (command.value.isPresent()) {
                    showBoughtByCustomerProducts(command.value.get());
                } else {
                    System.out.println("Format error");
                }
            }
            case SHOW_CUSTOMERS_BOUGHT_PRODUCT -> {
                if (command.value.isPresent()) {
                    showCustomersBoughtProduct(command.value.get());
                } else {
                    System.out.println("Format error");
                }
            }
            case PRODUCTS_DETAIL -> {
                productsDetails();
            }
            case DELETE_PRODUCT_BY_ID -> {
                if (command.value.isPresent()) {
                    deleteProductById(command.value.get());
                } else {
                    System.out.println("Format error");
                }
            }
            case DELETE_CUSTOMER_BY_ID -> {
                if (command.value.isPresent()) {
                    deleteCustomerById(command.value.get());
                } else {
                    System.out.println("Format error");
                }
            }
            default -> {
                System.out.println("Unsupported command.");
            }
        }
    }

    private void showAllProductsHandle() {
        List<Product> productList = productRepository.findAll();
        System.out.println("Products list:");
        System.out.println("=====================");
        productList.forEach(System.out::println);
    }

    private void showAllCustomersHandle() {
        List<Customer> customerList = customerRepository.findAll();
        System.out.println("Customers list:");
        System.out.println("=====================");
        customerList.forEach(System.out::println);
    }

    private void showBoughtByCustomerProducts(Long productId) {
        try {
            Optional<Product> product = productRepository.findById(productId, true);
            if (product.isEmpty()) {
                System.out.println("Product is not found!");
                return;
            }
            System.out.println("Customers: [");
            product.get().getCustomers().forEach(System.out::println);
            System.out.println("]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCustomersBoughtProduct(Long customerId) {
        try {
            Optional<Customer> customer = customerRepository.findById(customerId, true);
            if (customer.isEmpty()) {
                System.out.println("Customer not found!");
                return;
            }

            System.out.println("Products=[");
            customer.get().getProducts().forEach(System.out::println);
            System.out.println("]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void productsDetails() {

    }

    private void deleteProductById(Long productId) {
        if (productRepository.deleteById(productId)) {
            System.out.println("Product " + productId + " was deleted!");
        } else {
            System.out.println("Product " + productId + " delete error!");
        }
    }

    private void deleteCustomerById(Long customerId) {
        if (customerRepository.deleteById(customerId)) {
            System.out.println("Customer " + customerId + " was deleted!");
        } else {
            System.out.println("Customer " + customerId + " delete error!");
        }
    }

    @Getter
    static class Command {

        private final CommandType commandType;

        private final Optional<Long> value;

        public Command(CommandType command, Optional<Long> value) {
            this.commandType = command;
            this.value = value;
        }
    }


    enum CommandType {
        SHOW_ALL_PRODUCTS("show products"),
        SHOW_ALL_CUSTOMERS("show customers"),
        SHOW_BOUGHT_BY_CUSTOMER_PRODUCTS("products"),
        SHOW_CUSTOMERS_BOUGHT_PRODUCT("customers"),
        PRODUCTS_DETAIL("details"),
        DELETE_PRODUCT_BY_ID("delete product"),
        DELETE_CUSTOMER_BY_ID("delete customer"),
        EXIT("exit");

        public String getDescription() {
            switch (this) {
                case SHOW_ALL_PRODUCTS -> {
                    return "'" + SHOW_ALL_PRODUCTS.typeName + "' - show all existing products";
                }
                case SHOW_ALL_CUSTOMERS -> {
                    return "'" + SHOW_ALL_CUSTOMERS.typeName + "' - show all existing customers";
                }
                case SHOW_BOUGHT_BY_CUSTOMER_PRODUCTS -> {
                    return "'" + SHOW_BOUGHT_BY_CUSTOMER_PRODUCTS.typeName + "':'<customer_id>' - show products bought by customer";
                }
                case SHOW_CUSTOMERS_BOUGHT_PRODUCT -> {
                    return  "'" + SHOW_CUSTOMERS_BOUGHT_PRODUCT.typeName + "':'<product_id> - show customers who bought the product";
                }
                case DELETE_PRODUCT_BY_ID -> {
                    return "'" + DELETE_PRODUCT_BY_ID.typeName + "':'<product_id>' - delete product by id";
                }
                case DELETE_CUSTOMER_BY_ID -> {
                    return "'" + DELETE_CUSTOMER_BY_ID.typeName + "':'<customer_id>' - delete customer by id";
                }
                case PRODUCTS_DETAIL -> {
                    return "'" + PRODUCTS_DETAIL.typeName + "' - products detail map";
                }
                case EXIT -> {
                    return "'exit'";
                }
                default -> throw new IllegalStateException("Unexpected value: " + this);
            }
        }

        final String typeName;
        @Setter
        Optional<Long> argument;
        CommandType(String typeName) {
            this.typeName = typeName;
            this.argument = Optional.empty();
        }
    }
}
