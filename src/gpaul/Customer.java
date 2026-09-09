package gpaul;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phone;
    private List<String> orderHistory;

    public Customer(String customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.orderHistory = new ArrayList<>();
    }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<String> getOrderHistory() { return new ArrayList<>(orderHistory); }
    public void addOrder(String orderId) { orderHistory.add(orderId); }

    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s", customerId, name, email, phone, String.join(";", orderHistory));
    }

    public static Customer fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", 5);
        Customer customer = new Customer(parts[0], parts[1], parts[2], parts[3]);
        if (parts.length > 4 && !parts[4].isEmpty()) {
            for (String order : parts[4].split(";")) customer.addOrder(order);
        }
        return customer;
    }

    @Override
    public String toString() {
        return String.format("Customer[%s]: %s (%s) - %d orders", customerId, name, email, orderHistory.size());
    }
}
