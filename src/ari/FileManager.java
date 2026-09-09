package ari;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import gpaul.*;

public class FileManager {
    private static final String DATA_DIR = "data";
    private static final String INVENTORY_FILE = DATA_DIR + "/inventory.csv";
    private static final String CUSTOMERS_FILE = DATA_DIR + "/customers.csv";
    private static final String ORDERS_FILE = DATA_DIR + "/orders.csv";

    public static void ensureDataDirectoryExists() {
        try { Files.createDirectories(Paths.get(DATA_DIR)); }
        catch (IOException e) { System.err.println("Warning: Could not create data directory: " + e.getMessage()); }
    }

    public static void saveInventory(List<Product> products) throws FileOperationException {
        ensureDataDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INVENTORY_FILE))) {
            for (Product product : products) {
                if (product instanceof MechanicalKeyboard) writer.write(((MechanicalKeyboard) product).toCSV());
                else if (product instanceof CustomKeyboard) writer.write(((CustomKeyboard) product).toCSV());
                writer.newLine();
            }
        } catch (IOException e) { throw new FileOperationException("save", INVENTORY_FILE, e); }
    }

    public static List<Product> loadInventory() throws FileOperationException {
        List<Product> products = new ArrayList<>(); File file = new File(INVENTORY_FILE);
        if (!file.exists()) return products;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try {
                    if (line.startsWith("MECH,")) products.add(MechanicalKeyboard.fromCSV(line));
                    else if (line.startsWith("CUSTOM,")) products.add(CustomKeyboard.fromCSV(line));
                } catch (Exception e) { System.err.println("Warning: Skipping invalid inventory line: " + line); }
            }
        } catch (IOException e) { throw new FileOperationException("load", INVENTORY_FILE, e); }
        return products;
    }

    public static void saveCustomers(List<Customer> customers) throws FileOperationException {
        ensureDataDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : customers) { writer.write(customer.toCSV()); writer.newLine(); }
        } catch (IOException e) { throw new FileOperationException("save", CUSTOMERS_FILE, e); }
    }

    public static List<Customer> loadCustomers() throws FileOperationException {
        List<Customer> customers = new ArrayList<>(); File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) return customers;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try { customers.add(Customer.fromCSV(line)); }
                catch (Exception e) { System.err.println("Warning: Skipping invalid customer line: " + line); }
            }
        } catch (IOException e) { throw new FileOperationException("load", CUSTOMERS_FILE, e); }
        return customers;
    }

    public static void saveOrders(List<Order> orders) throws FileOperationException {
        ensureDataDirectoryExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDERS_FILE))) {
            for (Order order : orders) { writer.write(order.toCSV()); writer.newLine(); }
        } catch (IOException e) { throw new FileOperationException("save", ORDERS_FILE, e); }
    }

    public static List<Order> loadOrders() throws FileOperationException {
        List<Order> orders = new ArrayList<>(); File file = new File(ORDERS_FILE);
        if (!file.exists()) return orders;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                try { orders.add(Order.fromCSV(line)); }
                catch (Exception e) { System.err.println("Warning: Skipping invalid order line: " + line); }
            }
        } catch (IOException e) { throw new FileOperationException("load", ORDERS_FILE, e); }
        return orders;
    }

    public static void exportReport(String filename, String content) throws FileOperationException {
        ensureDataDirectoryExists(); String filepath = DATA_DIR + "/" + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) { writer.write(content); }
        catch (IOException e) { throw new FileOperationException("export", filepath, e); }
    }
}
