package es.testacademy.cursos.contract.testing.jvm.demo.consumer;

import es.testacademy.cursos.contract.testing.jvm.demo.consumer.products.Product;
import es.testacademy.cursos.contract.testing.jvm.demo.consumer.products.ProductService;
import es.testacademy.cursos.contract.testing.jvm.demo.consumer.users.User;
import es.testacademy.cursos.contract.testing.jvm.demo.consumer.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

@Component
public class ConsoleInterface implements CommandLineRunner {

    private final ProductService productService;
    private final UserService userService;

    private List<Product> products;
    private List<User> users;

    @Autowired
    ConsoleInterface(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        Integer choice = getMenuChoice(scanner);
        if (choice == 1)
            manageUsers(scanner);
        else
            manageProducts(scanner);
    }

    private void manageUsers(Scanner scanner) {
        while (true) {
            printAllUsers();
            Integer choice = getItemChoice(scanner);
            if (choice == null || choice <= 0 || choice > users.size()) {
                System.out.println("Exiting...Incorrect option");
                break;
            }
            printUser(choice);
        }
    }

    private void manageProducts(Scanner scanner) {
        while (true) {
            printAllProducts();
            Integer choice = getItemChoice(scanner);
            if (choice == null || choice <= 0 || choice > products.size()) {
                System.out.println("Exiting...");
                break;
            }
            printProduct(choice);
        }
    }

    private Integer getMenuChoice(Scanner scanner) {
        System.out.print("Menu: \n 1) Users \n 2) Products");
        String choice = scanner.nextLine();
        return parseChoice(choice);
    }

    private void printAllProducts() {
        products = productService.getAllProducts();
        System.out.println("\n\nProducts\n--------");
        IntStream.range(0, products.size())
                .forEach(index -> System.out.println(String.format("%d) %s", index + 1, products.get(index).getName())));
    }

    private void printAllUsers() {
        users = userService.getAllUsers();
        System.out.println("\n\nUsers\n--------");
        IntStream.range(0, users.size())
                .forEach(index -> System.out.println(String.format("%d) %s", index + 1, users.get(index).getName())));
    }

    private Integer getItemChoice(Scanner scanner) {
        System.out.print("Select item number to view details: ");
        String choice = scanner.nextLine();
        return parseChoice(choice);
    }

    private void printProduct(int index) {
        String id = products.get(index - 1).getId();
        try {
            Product product = productService.getProduct(id);

            System.out.println("Product Details\n---------------");
            System.out.println(product);
        } catch (Exception e) {
            System.out.println("Failed to load product " + id);
            System.out.println(e.getMessage());
        }
    }

    private void printUser(int index) {
        String userName = users.get(index - 1).getName();
        try {
            User user = userService.getUser(userName);
            System.out.println("User Details\n---------------");
            System.out.println(user);
        } catch (Exception e) {
            System.out.println("Failed to load user " + userName);
            System.out.println(e.getMessage());
        }
    }

    private Integer parseChoice(String choice) {
        try {
            return Integer.parseInt(choice);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
