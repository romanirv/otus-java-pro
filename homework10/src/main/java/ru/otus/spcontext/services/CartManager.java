package ru.otus.spcontext.services;

import org.springframework.context.ApplicationContext;
import ru.otus.spcontext.entiry.Product;
import ru.otus.spcontext.repository.ProductRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CartManager {

    private final ApplicationContext context;
    private final ProductRepository productRepository;
    private Optional<Cart> currentCart = Optional.empty();

    public CartManager(ApplicationContext context) {
        this.context = context;
        this.productRepository = context.getBean(ProductRepository.class);
    }

    public void start() throws IOException {
        showSupportedCommands();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine;
        while (true) {
            System.out.print( "> " );
            inputLine = reader.readLine();
            Optional<Command> command = parseCommand(inputLine);
            if (command.isEmpty()) {
                System.out.println("Unsupported command.");
            } else {
                if (command.get().commandType == CommandType.EXIT) {
                    break;
                }
                handleCommand(command.get());
            }
        }
    }

    private void showSupportedCommands() {
        System.out.println("Supported commands:");
        System.out.println("===================");
        System.out.println("'" + CommandType.SHOW_ALL_PRODUCTS.command + "'             - show all existing products");
        System.out.println("'" + CommandType.CREATE_NEW_CART.command + "'               - create new cart");
        System.out.println("'" + CommandType.ADD_PRODUCT.command + "':'<product_id>'    - add new product to cart");
        System.out.println("'" + CommandType.DELETE_PRODUCT.command + "':'<product_id>' - delete product from cart");
        System.out.println("'" + CommandType.SHOW_CART.command + "'                     - show all products from cart");
        System.out.println("'" + CommandType.DELETE_CART.command + "'                   - delete cart");
    }

    private Optional<Command> parseCommand(String inputLine) {
        if (inputLine.isEmpty()) {
            return Optional.empty();
        }

        String[] parts = inputLine.strip().split(":");
        Optional<CommandType> commandType = Arrays.stream(CommandType.values())
                .filter(c -> c.command.equals(parts[0]))
                .findFirst();
        if (commandType.isEmpty()) {
            return Optional.empty();
        }

        Optional<Long> value = Optional.empty();
        if (parts.length > 1) {
            try {
                value = Optional.of(Long.parseLong(parts[1]));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        return Optional.of(new Command(commandType.get(), value));
    }

    private void handleCommand(Command command) {
        switch (command.commandType) {
            case SHOW_ALL_PRODUCTS -> {
                showAllProductsHandle();
            }
            case CREATE_NEW_CART -> {
                createNewCartHandle();
            }
            case SHOW_CART -> {
                showCartHandle();
            }
            case ADD_PRODUCT -> {
                addProductHandle(command);
            }
            case DELETE_PRODUCT -> {
                deleteProductHandle(command);
            }
            case DELETE_CART -> {
                deleteCartHandle();
            }
            default -> {
                System.out.println("Unsupported command.");
            }
        }
    }

    private void showAllProductsHandle() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            System.out.println("Product list is empty");
        } else {
            products.forEach(System.out::println);
        }
    }

    private void createNewCartHandle() {
        if (currentCart.isPresent()) {
            System.out.println("Cart is already exist. You need to delete it first");
        } else {
            currentCart = Optional.of(context.getBean(Cart.class));
        }
    }

    private void showCartHandle() {
        if (currentCart.isEmpty()) {
            System.out.println("Cart not created!");
            return;
        }

        List<Product> products = currentCart.get().getAllProducts();
        if (products.isEmpty()) {
            System.out.println("Cart is empty!");
            return;
        }

        products.forEach(System.out::println);
    }

    private void addProductHandle(Command command) {
        if (command.value.isEmpty()) {
            System.out.println("Input format error, required 'product id'");
        } else {
            if (currentCart.isEmpty()) {
                System.out.println("Cart not created!");
            } else {
                System.out.println(currentCart.get().addProduct(command.value.get()) ? "added product success"
                                                                                     : "added product failed");
            }
        }
    }

    private void deleteProductHandle(Command command) {
        if (command.value.isEmpty()) {
            System.out.println("Input format error, required 'product id'");
        } else {
            if (currentCart.isEmpty()) {
                System.out.println("Cart not created!");
            } else {
                System.out.println(currentCart.get().removeProduct(command.value.get()) ? "delete product success"
                                                                                        : "delete product failed");
            }
        }
    }

    private void deleteCartHandle() {
        if (currentCart.isEmpty()) {
            System.out.println("Cart not created!");
        } else {
            currentCart = Optional.empty();
            System.out.println("Cart removed!");
        }

    }

    static class Command {
        private final CommandType commandType;
        private final Optional<Long> value;

        public Command(CommandType command, Optional<Long> value) {
            this.commandType = command;
            this.value = value;
        }

        public CommandType getCommandType() {
            return commandType;
        }

        public Optional<Long> getValue() {
            return value;
        }
    }


    enum CommandType {
        SHOW_ALL_PRODUCTS("show all products"),
        CREATE_NEW_CART("create new cart"),
        SHOW_CART("show cart"),
        ADD_PRODUCT("add product"),
        DELETE_PRODUCT("delete product"),
        DELETE_CART("delete cart"),
        EXIT("exit");

        final String command;
        CommandType(String command) {
            this.command = command;
        }
    }
}
