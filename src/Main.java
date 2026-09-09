import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ari.*;
import gpaul.*;
import rhtahsin.*;

public class Main {
    private static InventoryService inventoryService;
    private static CustomerService customerService;
    private static OrderService orderService;
    private static Scanner scanner;
    private static List<CartItem> shoppingCart;
    private static Customer currentCustomer;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        shoppingCart = new ArrayList<>();      
        inventoryService = new InventoryService();
        customerService = new CustomerService();
        orderService = new OrderService();

        System.out.println("==============================================");
        System.out.println("   WELCOME TO MECHANICAL KEYBOARD SHOP");
        System.out.println("==============================================\n");

        loadData();
        
        if (inventoryService.getAllProducts().isEmpty()) {
            System.out.println("No products found. Initializing with sample data...\n");
            initializeSampleData();
        }

        mainMenu();
        
        System.out.println("\nThank you for using Mechanical Keyboard Shop!");
        scanner.close();
    }

    private static void loadData() {
        try {
            inventoryService.loadFromFile();
            customerService.loadFromFile();
            orderService.loadFromFile();
        } catch (FileOperationException e) {
            System.out.println("Note: " + e.getMessage());
            System.out.println("Starting with empty data.\n");
        }
    }

    private static void saveAllData() {
        try {
            inventoryService.saveToFile();
            customerService.saveToFile();
            orderService.saveToFile();
            System.out.println("All data saved successfully!");
        } catch (FileOperationException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Browse Products");
            System.out.println("2. Search Products");
            System.out.println("3. Shopping Cart");
            System.out.println("4. Customer Management");
            System.out.println("5. Inventory Management (Admin)");
            System.out.println("6. View All Orders");
            System.out.println("7. Generate Reports");
            System.out.println("8. Save Data");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1: browseProducts(); break;
                    case 2: searchProducts(); break;
                    case 3: shoppingCartMenu(); break;
                    case 4: customerMenu(); break;
                    case 5: inventoryMenu(); break;
                    case 6: viewAllOrders(); break;
                    case 7: reportsMenu(); break;
                    case 8: saveAllData(); break;
                    case 9: saveAllData(); return;
                    default: System.out.println("Invalid option. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void browseProducts() {
        inventoryService.displayAllProducts();
        System.out.print("\nEnter product ID to view details (or press Enter to go back): ");
        String productId = scanner.nextLine().trim();
        if (!productId.isEmpty()) {
            try {
                inventoryService.displayProductDetails(productId);
                System.out.print("\nAdd to cart? (y/n): ");
                if (scanner.nextLine().trim().equalsIgnoreCase("y")) addToCart(productId);
            } catch (ProductNotFoundException e) { System.out.println("Error: " + e.getMessage()); }
        }
    }

    private static void searchProducts() {
        System.out.print("Enter search keyword: ");
        String keyword = scanner.nextLine().trim();
        List<Product> results = inventoryService.searchProducts(keyword);
        if (results.isEmpty()) System.out.println("No products found matching: " + keyword);
        else {
            System.out.println("\n=== SEARCH RESULTS ===");
            for (Product product : results) System.out.println(product.toString());
        }
    }

    private static void shoppingCartMenu() {
        while (true) {
            System.out.println("\n========== SHOPPING CART ==========");
            displayCart();
            System.out.println("\n1. Add Item\n2. Remove Item\n3. Update Quantity\n4. Checkout\n5. Clear Cart\n6. Back to Main Menu");
            System.out.print("Choose an option: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1: System.out.print("Enter product ID: "); addToCart(scanner.nextLine().trim()); break;
                    case 2: removeFromCart(); break;
                    case 3: updateCartQuantity(); break;
                    case 4: checkout(); break;
                    case 5: shoppingCart.clear(); System.out.println("Cart cleared."); break;
                    case 6: return;
                    default: System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) { System.out.println("Invalid input. Please enter a number."); }
        }
    }

    private static void displayCart() {
        if (shoppingCart.isEmpty()) { System.out.println("Your cart is empty."); return; }
        double total = 0;
        System.out.println("\nItems in cart:");
        for (int i = 0; i < shoppingCart.size(); i++) {
            CartItem item = shoppingCart.get(i);
            System.out.println((i + 1) + ". " + item.toString());
            total += item.getSubtotal();
        }
        System.out.println("\nTotal: ৳" + String.format("%.2f", total));
    }

    private static void addToCart(String productId) {
        try {
            Product product = inventoryService.findProductById(productId);
            if (!product.isAvailable()) { System.out.println("Product is out of stock."); return; }
            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());
            if (quantity <= 0) { System.out.println("Quantity must be positive."); return; }
            if (quantity > product.getStockQuantity()) { System.out.println("Insufficient stock. Available: " + product.getStockQuantity()); return; }
            CartItem existingItem = shoppingCart.stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst().orElse(null);
            if (existingItem != null) existingItem.increaseQuantity(quantity); else shoppingCart.add(new CartItem(product, quantity));
            System.out.println("Added to cart: " + product.getName() + " x" + quantity);
        } catch (ProductNotFoundException e) { System.out.println("Error: " + e.getMessage()); }
          catch (NumberFormatException e) { System.out.println("Invalid quantity."); }
    }

    private static void removeFromCart() {
        if (shoppingCart.isEmpty()) { System.out.println("Cart is empty."); return; }
        displayCart();
        System.out.print("Enter item number to remove: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index >= 0 && index < shoppingCart.size()) {
                CartItem removed = shoppingCart.remove(index);
                System.out.println("Removed: " + removed.getProduct().getName());
            } else System.out.println("Invalid item number.");
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private static void updateCartQuantity() {
        if (shoppingCart.isEmpty()) { System.out.println("Cart is empty."); return; }
        displayCart();
        System.out.print("Enter item number to update: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index >= 0 && index < shoppingCart.size()) {
                CartItem item = shoppingCart.get(index);
                System.out.print("Enter new quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine().trim());
                if (quantity <= 0) { System.out.println("Quantity must be positive."); return; }
                if (quantity > item.getProduct().getStockQuantity()) { System.out.println("Insufficient stock."); return; }
                item.setQuantity(quantity);
                System.out.println("Quantity updated.");
            } else System.out.println("Invalid item number.");
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private static void checkout() {
        if (shoppingCart.isEmpty()) { System.out.println("Cart is empty."); return; }
        if (currentCustomer == null) {
            System.out.print("Enter customer ID: ");
            String customerId = scanner.nextLine().trim();
            currentCustomer = customerService.findCustomerById(customerId);
            if (currentCustomer == null) { System.out.println("Customer not found. Please register first."); return; }
        }
        try {
            Order order = orderService.createOrder(currentCustomer.getCustomerId(), shoppingCart, inventoryService);
            currentCustomer.addOrderToHistory(order.getOrderId());
            shoppingCart.clear();
            System.out.println("\nCheckout successful!");
            orderService.displayOrderDetails(order.getOrderId());
        } catch (InsufficientStockException | ProductNotFoundException e) { System.out.println("Checkout failed: " + e.getMessage()); }
    }

    private static void customerMenu() {
        while (true) {
            System.out.println("\n========== CUSTOMER MANAGEMENT ==========");
            System.out.println("1. Register Customer\n2. View All Customers\n3. Customer Details\n4. Set Current Customer\n5. Back");
            System.out.print("Choose an option: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1: registerCustomer(); break;
                    case 2: customerService.displayAllCustomers(); break;
                    case 3: System.out.print("Enter customer ID: "); customerService.displayCustomerDetails(scanner.nextLine().trim()); break;
                    case 4: setCurrentCustomer(); break;
                    case 5: return;
                    default: System.out.println("Invalid option.");
                }
            } catch (NumberFormatException e) { System.out.println("Invalid input."); }
        }
    }

    private static void registerCustomer() {
        System.out.print("Enter name: "); String name = scanner.nextLine().trim();
        System.out.print("Enter email: "); String email = scanner.nextLine().trim();
        System.out.print("Enter phone: "); String phone = scanner.nextLine().trim();
        try {
            Customer customer = customerService.registerCustomer(name, email, phone);
            currentCustomer = customer;
            System.out.println("Customer registered successfully: " + customer.getCustomerId());
        } catch (InvalidInputException e) { System.out.println("Registration failed: " + e.getMessage()); }
    }

    private static void setCurrentCustomer() {
        System.out.print("Enter customer ID: ");
        Customer customer = customerService.findCustomerById(scanner.nextLine().trim());
        if (customer == null) System.out.println("Customer not found.");
        else { currentCustomer = customer; System.out.println("Current customer: " + customer.getName()); }
    }

    private static void inventoryMenu() {
        while (true) {
            System.out.println("\n========== INVENTORY MANAGEMENT ==========");
            System.out.println("1. View Inventory\n2. Add Product\n3. Remove Product\n4. Update Stock\n5. Back");
            System.out.print("Choose an option: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1: inventoryService.displayAllProducts(); break;
                    case 2: addProduct(); break;
                    case 3: System.out.print("Enter product ID: "); inventoryService.removeProduct(scanner.nextLine().trim()); break;
                    case 4: updateStock(); break;
                    case 5: return;
                    default: System.out.println("Invalid option.");
                }
            } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
        }
    }

    private static void addProduct() {
        System.out.println("Product addition is supported through the Product model classes.");
        System.out.println("Use the existing keyboard subclasses when extending the inventory.");
    }

    private static void updateStock() {
        try {
            System.out.print("Enter product ID: "); String id = scanner.nextLine().trim();
            System.out.print("Enter new stock quantity: "); int stock = Integer.parseInt(scanner.nextLine().trim());
            inventoryService.updateStock(id, stock);
        } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void viewAllOrders() { orderService.displayAllOrders(); }

    private static void reportsMenu() {
        System.out.println("\n========== REPORTS ==========");
        System.out.println(inventoryService.generateInventoryReport());
        System.out.println(orderService.generateSalesReport());
        try {
            FileManager.exportReport("inventory_report.txt", inventoryService.generateInventoryReport());
            FileManager.exportReport("sales_report.txt", orderService.generateSalesReport());
            System.out.println("Reports exported to data folder.");
        } catch (FileOperationException e) { System.out.println("Report export failed: " + e.getMessage()); }
    }

    private static void initializeSampleData() {
        inventoryService.addProduct(new MechanicalKeyboard("KB001", "Keychron K2 Pro", 10000, 15, "Compact wireless mechanical keyboard with hot-swappable switches", "Gateron Brown", "75%", "Keychron", true, true, "Wireless/Wired"));
        inventoryService.addProduct(new MechanicalKeyboard("KB002", "Ducky One 3", 12000, 10, "Premium mechanical keyboard with PBT keycaps", "Cherry MX Red", "TKL", "Ducky", true, false, "Wired"));
        inventoryService.addProduct(new CustomKeyboard("KB003", "GMMK Pro", 16500, 7, "Fully customizable gasket-mounted mechanical keyboard", "Glorious Panda", "75%", "Glorious", true, "PBT Double-Shot", "Black", true));
    }
}