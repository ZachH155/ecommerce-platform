package group16;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import group16.classes.User.DatabaseConnectionZ;
import group16.classes.User.User;
import group16.classes.User.UserDAO;
import group16.classes.User.UserService;
import group16.classes.User.Admin;
import group16.classes.User.Buyer;
import group16.classes.User.Seller;
import group16.classes.Product.ProductService;
import group16.classes.Product.DatabaseConnectionD;
import group16.classes.Product.Product;
import group16.classes.Product.ProductDAO;

public class App {
    private static UserService userService;
    private static ProductService productService;
    private static User loggedUser = new User();

    public static void main(String[] args) throws SQLException {
        Connection connectionD = DatabaseConnectionD.getCon();
        Connection connectionZ = DatabaseConnectionZ.getConnection();
        ProductDAO productDAO = new ProductDAO(connectionZ);
        UserDAO userDAO = new UserDAO(connectionZ);
        productService = new ProductService(productDAO);
        userService = new UserService(userDAO);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("___________________________________________________");
            System.out.println(showUserInfo(loggedUser));
            System.out.println("Enter a number");
            System.out.println("");
            System.out.println("1. Log in");
            System.out.println("2. Create account");

            checkUserRole(loggedUser, scanner);
        }
    }

    private static void createUser(Scanner scanner) throws SQLException {
        System.out.println("___________________________________________________");
        System.out.println();

        System.out.println("Set role");
        System.out.println("Buyer, Seller, or Admin");
        String role = scanner.nextLine().toLowerCase();

        System.out.println("Enter username");
        String username = scanner.nextLine();

        System.out.println("Enter password");
        String password = scanner.nextLine();

        System.out.println("Enter email");
        String email = scanner.nextLine();

        User user;
        switch (role) {
            case "admin":
                user = new Admin(username, password, email);
                break;
            case "buyer":
                user = new Buyer(username, password, email);
                break;
            case "seller":
                user = new Seller(username, password, email);
                break;
            default:
                System.out.println("Invalid role. Defaulting to Buyer.");
                user = new Buyer(username, password, email);
                break;
        }

        userService.addUser(user);
    }

    private static void checkUserRole(User loggedUser, Scanner scanner) throws SQLException {
        switch (loggedUser.getRole()) {
            case "admin":
                System.out.println("3. Product Management");
                System.out.println("4. Admin Management");
                System.out.println("5. Exit");
                break;
            case "seller":
                System.out.println("3. Product Management");
                System.out.println("4. Exit");
                break;
            case "buyer":
                System.out.println("3. View Products");
                System.out.println("4. Exit");
                break;
            default:
                System.out.println("3. Exit");
                break;
        }

        System.out.println();
        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                loginUser(scanner);
                break;
            case 2:
                createUser(scanner);
                break;
            case 3:
                if (loggedUser.getRole().equals("admin") || loggedUser.getRole().equals("seller")) {
                    productManagement(scanner);
                } else if (loggedUser.getRole().equals("buyer")) {
                    viewAllProducts();
                } else {
                    System.exit(0);
                }
                break;
            case 4:
                if (loggedUser.getRole().equals("admin")) {
                    adminManagement(scanner);
                } else {
                    System.exit(0);
                }
                break;
            case 5:
                if (loggedUser.getRole().equals("admin")) {
                    System.exit(0);
                } else {
                    System.out.println("Choose one of the available numbers");
                }
                break;
            default:
                System.out.println("Choose one of the available numbers");
                break;
        }
    }

    private static void loginUser(Scanner scanner) throws SQLException {
        System.out.println("___________________________________________________");
        System.out.println("User Login");
        System.out.println();
        System.out.println("Enter username");
        String username = scanner.nextLine();

        System.out.println("Enter password");
        String password = scanner.nextLine();
        userService.getUser(username, password);
        if (userService.DAO.loginSuccess == true) {
            loggedUser = userService.getUser(username, password);
            System.out.println("Login successful");
        }
    }

    private static void productManagement(Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("Product Management");
            System.out.println("1. Add Product");
            System.out.println("2. View Products by Seller");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. View All Products");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    addProduct(scanner);
                    break;
                case 2:
                    viewProductsBySeller(scanner);
                    break;
                case 3:
                    updateProduct(scanner);
                    break;
                case 4:
                    deleteProduct(scanner);
                    break;
                case 5:
                    viewAllProducts();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addProduct(Scanner scanner) throws SQLException {
        System.out.println("Enter product name:");
        String name = scanner.nextLine();
        System.out.println("Enter product price:");
        double price = scanner.nextDouble();
        System.out.println("Enter product quantity:");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter seller ID:");
        int sellerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Check if the seller exists
        User seller = userService.getUserById(sellerId);
        if (seller == null || !seller.getRole().equals("seller")) {
            System.out.println("Seller ID does not exist or is not a seller. Please enter a valid seller ID.");
            return;
        }

        productService.addProduct(name, price, quantity, sellerId);
        System.out.println("Product added successfully.");
    }

    private static void viewProductsBySeller(Scanner scanner) throws SQLException {
        System.out.println("Enter seller ID:");
        int sellerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        List<Product> products = productService.getProductsBySeller(sellerId);
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static void updateProduct(Scanner scanner) throws SQLException {
        System.out.println("Enter product ID:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter new product name:");
        String name = scanner.nextLine();
        System.out.println("Enter new product price:");
        double price = scanner.nextDouble();
        System.out.println("Enter new product quantity:");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        productService.updateProduct(id, name, price, quantity);
        System.out.println("Product updated successfully.");
    }

    private static void deleteProduct(Scanner scanner) throws SQLException {
        System.out.println("Enter product ID:");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        productService.deleteProduct(productId);
        System.out.println("Product deleted successfully.");
    }

    private static void viewAllProducts() throws SQLException {
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static void adminManagement(Scanner scanner) throws SQLException {
        while (true) {
            System.out.println("Admin Management");
            System.out.println("1. View All Users");
            System.out.println("2. Delete User");
            System.out.println("3. View All Products with Seller Info");
            System.out.println("4. Back to Main Menu");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    deleteUser(scanner);
                    break;
                case 3:
                    viewAllProductsWithSellerInfo();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewAllUsers() throws SQLException {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }

    private static void deleteUser(Scanner scanner) throws SQLException {
        System.out.print("Enter user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        userService.deleteUser(userId);
        System.out.println("User deleted successfully.");
    }

    private static void viewAllProductsWithSellerInfo() throws SQLException {
        List<Product> products = productService.getAllProducts();
        for (Product product : products) {
            User seller = userService.getUserById(product.getSellerId());
            System.out.println("Product: " + product);
            System.out.println("Seller: " + seller);
        }
    }

    private static String showUserInfo(User loggedUser) {
        if (!loggedUser.getUsername().equals("Tempname")) {
            String username = loggedUser.getUsername();
            String role = loggedUser.getRole();
            return "User: " + username + " Role: " + role;
        }
        return "";
    }
}