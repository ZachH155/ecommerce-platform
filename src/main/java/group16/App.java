package group16;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import group16.classes.User.DatabaseConnectionZ;
import group16.classes.User.User;
import group16.classes.User.UserDAO;
import group16.classes.User.UserService;
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
        User user = new User();

        System.out.println("___________________________________________________");
        System.out.println();

        System.out.println("Set role");
        System.out.println("Buyer, Seller, or Admin");
        user.setRole(scanner.nextLine());

        System.out.println("Enter username");
        user.setUsername(scanner.nextLine());

        System.out.println("Enter password");
        user.setPassword(scanner.nextLine());

        System.out.println("Enter email");
        user.setEmail(scanner.nextLine());

        userService.addUser(user);
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
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter product quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter seller ID: ");
        int sellerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        productService.addProduct(name, price, quantity, sellerId);
        System.out.println("Product added successfully.");
    }

    private static void viewProductsBySeller(Scanner scanner) throws SQLException {
        System.out.print("Enter seller ID: ");
        int sellerId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        List<Product> products = productService.getProductsBySeller(sellerId);
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static void updateProduct(Scanner scanner) throws SQLException {
        System.out.print("Enter product ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new product price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter new product quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        productService.updateProduct(id, name, price, quantity);
        System.out.println("Product updated successfully.");
    }

    private static void deleteProduct(Scanner scanner) throws SQLException {
        System.out.print("Enter product ID: ");
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

    private static String showUserInfo(User loggedUser) {
        if (!loggedUser.getUsername().equals("Tempuser")) {
            String username = loggedUser.getUsername();
            String role = loggedUser.getRole();
            return "User: " + username + " Role: " + role;
        }
        return "";
    }

    //checks if loggedUser role is admin, changes options accordingly
    private static void checkUserRole(User loggedUser, Scanner scanner) throws SQLException {
        if (loggedUser.getRole().equals("admin")) {
            System.out.println("3. Product Management");
            System.out.println("4. Admin Management");
            System.out.println("5. Exit");
            System.out.println();

            switch (Integer.parseInt(scanner.nextLine())) {
                case 1:
                    loginUser(scanner);
                    break;
                case 2:
                    createUser(scanner);
                    break;
                case 3:
                    productManagement(scanner);
                    break;
                case 4:
                    adminManagement(scanner);
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choose one of the available numbers");
                    break;
            }
        } else {
            System.out.println("3. Exit");
            System.out.println();

            switch (Integer.parseInt(scanner.nextLine())) {
                case 1:
                    loginUser(scanner);
                    break;
                case 2:
                    createUser(scanner);
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Choose one of the available numbers");
                    break;
            }   
        }

    }
}
