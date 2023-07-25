import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class Book {
    private String title;
    private String author;
    private double price;
    private int stock;

    public Book(String title, String author, double price, int stock) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

public class OnlineBookstore {
    private Map<String, Book> bookCatalog;
    private Map<String, User> userMap;
    private Map<String, List<Book>> shoppingCarts;

    public OnlineBookstore() {
        bookCatalog = new HashMap<>();
        userMap = new HashMap<>();
        shoppingCarts = new HashMap<>();
    }

    public void addBook(Book book) {
        bookCatalog.put(book.getTitle(), book);
    }

    public void updateBookStock(String title, int newStock) {
        if (bookCatalog.containsKey(title)) {
            bookCatalog.get(title).setStock(newStock);
        }
    }

    public Book getBook(String title) {
        return bookCatalog.get(title);
    }

    public void registerUser(User user) {
        userMap.put(user.getUsername(), user);
    }

    public User getUser(String username) {
        return userMap.get(username);
    }

    public User loginUser(String username, String password) {
        User user = getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public void addToCart(String username, Book book) {
        if (!shoppingCarts.containsKey(username)) {
            shoppingCarts.put(username, new ArrayList<>());
        }
        shoppingCarts.get(username).add(book);
    }

    public List<Book> getShoppingCart(String username) {
        return shoppingCarts.getOrDefault(username, new ArrayList<>());
    }

    public void removeFromCart(String username, Book book) {
        List<Book> cart = shoppingCarts.get(username);
        if (cart != null) {
            cart.remove(book);
        }
    }

    public void processOrder(String username) {
        List<Book> cart = shoppingCarts.get(username);
        if (cart == null || cart.isEmpty()) {
            System.out.println("Your cart is empty. Cannot process the order.");
            return;
        }

        double totalAmount = 0;
        for (Book book : cart) {
            totalAmount += book.getPrice();
            book.setStock(book.getStock() - 1); // Reduce the stock by 1 after purchase
        }

        shoppingCarts.remove(username); // Clear the cart after processing the order

        System.out.println("Order processed successfully!");
        System.out.println("Total amount: $" + totalAmount);
    }

    public static void main(String[] args) {
        OnlineBookstore bookstore = new OnlineBookstore();
        Scanner sc= new Scanner(System.in);

        // Sample book data
        Book book1 = new Book("My_Book", "Robin Sharma", 33.99, 50);
        Book book2 = new Book("Nikhil_Babhulkar", "Nick", 12.49, 50);
        Book book3 = new Book("My_Book2", "Robin Sharma", 33.99, 50);
        Book book4 = new Book("NB", "Nick", 12.49, 50);

        // Sample user data
        User user1 = new User("user1", "password1");
        User user2 = new User("user2", "password2");

        // Register users and add books to the catalog
        bookstore.registerUser(user1);
        bookstore.registerUser(user2);
        bookstore.addBook(book1);
        bookstore.addBook(book2);

        boolean loggedIn = false;
        User currentUser = null;

        while (!loggedIn) {
            System.out.println("Welcome to the Online Bookstore!");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume the newline character after reading the integer

            switch (choice) {
                case 1:
                    System.out.print("Enter your username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter your password: ");
                    String password = sc.nextLine();
                    currentUser = bookstore.loginUser(username, password);
                    if (currentUser != null) {
                        loggedIn = true;
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Invalid username or password. Please try again.");
                    }
                    break;
                case 2:
                    System.out.print("Enter a new username: ");
                    String newUsername = sc.nextLine();
                    System.out.print("Enter a new password: ");
                    String newPassword = sc.nextLine();
                    User newUser = new User(newUsername, newPassword);
                    bookstore.registerUser(newUser);
                    System.out.println("Registration successful! You can now log in.");
                    break;
                case 3:
                    System.out.println("Exiting the Online Bookstore. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }

        // After successful login
        System.out.println("Hello, " + currentUser.getUsername() + "!");

        // Sample shopping cart functionality (same as provided earlier)
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Add Book to Cart");
            System.out.println("2. View Cart");
            System.out.println("3. Remove Book from Cart");
            System.out.println("4. Process Order");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume the newline character after reading the integer

            switch (choice) {
                case 1:
                    System.out.print("Enter the book title to add to cart: ");
                    String title = sc.nextLine();
                    Book book = bookstore.getBook(title);
                    if (book != null) {
                        bookstore.addToCart(currentUser.getUsername(), book);
                        System.out.println("Book added to cart.");
                    } else {
                        System.out.println("Book not found in the catalog.");
                    }
                    break;
                case 2:
                    List<Book> cartItems = bookstore.getShoppingCart(currentUser.getUsername());
                    if (cartItems.isEmpty()) {
                        System.out.println("Your cart is empty.");
                    } else {
                        System.out.println("Your cart:");
                        for (Book item : cartItems) {
                            System.out.println("- " + item.getTitle());
                        }
                    }
                    break;
                case 3:
                    System.out.print("Enter the book title to remove from cart: ");
                    title = sc.nextLine();
                    book = bookstore.getBook(title);
                    if (book != null) {
                        bookstore.removeFromCart(currentUser.getUsername(), book);
                        System.out.println("Book removed from cart.");
                    } else {
                        System.out.println("Book not found in the cart.");
                    }
                    break;
                case 4:
                    bookstore.processOrder(currentUser.getUsername());
                    break;
                case 5:
                    System.out.println("Exiting the Online Bookstore. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
