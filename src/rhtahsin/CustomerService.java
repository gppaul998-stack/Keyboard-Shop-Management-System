package rhtahsin;

import java.util.ArrayList;
import java.util.List;
import ari.FileManager;
import ari.FileOperationException;
import gpaul.Customer;

public class CustomerService {
    private List<Customer> customers;
    private int nextCustomerId;
    public CustomerService() { customers = new ArrayList<>(); nextCustomerId = 1; }
    public void loadFromFile() throws FileOperationException {
        customers = FileManager.loadCustomers();
        if (!customers.isEmpty()) nextCustomerId = customers.stream().mapToInt(c -> Integer.parseInt(c.getCustomerId().substring(1))).max().orElse(0) + 1;
        System.out.println("Loaded " + customers.size() + " customers from file.");
    }
    public void saveToFile() throws FileOperationException { FileManager.saveCustomers(customers); System.out.println("Saved " + customers.size() + " customers to file."); }
    public Customer registerCustomer(String name, String email, String phone) {
        String customerId = "C" + String.format("%04d", nextCustomerId++);
        Customer customer = new Customer(customerId, name, email, phone); customers.add(customer); System.out.println("Customer registered: " + customerId); return customer;
    }
    public Customer findCustomerById(String customerId) { return customers.stream().filter(c -> c.getCustomerId().equalsIgnoreCase(customerId)).findFirst().orElse(null); }
    public List<Customer> getAllCustomers() { return new ArrayList<>(customers); }
    public void displayAllCustomers() {
        if (customers.isEmpty()) { System.out.println("No customers registered."); return; }
        System.out.println("\n=== CUSTOMERS ==="); for (Customer customer : customers) System.out.println(customer.toString());
    }
    public void displayCustomerDetails(String customerId) {
        Customer customer = findCustomerById(customerId);
        if (customer == null) { System.out.println("Customer not found: " + customerId); return; }
        System.out.println("\n=== CUSTOMER DETAILS ===");
        System.out.println("ID: " + customer.getCustomerId()); System.out.println("Name: " + customer.getName());
        System.out.println("Email: " + customer.getEmail()); System.out.println("Phone: " + customer.getPhone());
        System.out.println("Order History: " + customer.getOrderHistory().size() + " orders");
        if (!customer.getOrderHistory().isEmpty()) System.out.println("Orders: " + String.join(", ", customer.getOrderHistory()));
    }
}
