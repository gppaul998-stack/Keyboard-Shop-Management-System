package gpaul;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private String customerId;
    private List<OrderItem> items;
    private double totalAmount;
    private LocalDateTime orderDate;
    private String status;

    public Order(String orderId, String customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
        this.orderDate = LocalDateTime.now();
        this.status = "Completed";
    }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public List<OrderItem> getItems() { return new ArrayList<>(items); }
    public void addItem(String productId, String productName, int quantity, double price) { items.add(new OrderItem(productId, productName, quantity, price)); calculateTotal(); }
    public double getTotalAmount() { return totalAmount; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    private void calculateTotal() { totalAmount = items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum(); }

    public String toCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder itemsStr = new StringBuilder();
        for (OrderItem item : items) itemsStr.append(item.toCSV()).append("|");
        if (itemsStr.length() > 0) itemsStr.setLength(itemsStr.length() - 1);
        return String.format("%s,%s,%.2f,%s,%s,%s", orderId, customerId, totalAmount, orderDate.format(formatter), status, itemsStr.toString());
    }

    public static Order fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", 6);
        Order order = new Order(parts[0], parts[1]);
        order.totalAmount = Double.parseDouble(parts[2]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        order.orderDate = LocalDateTime.parse(parts[3], formatter);
        order.status = parts[4];
        if (parts.length > 5 && !parts[5].isEmpty()) {
            for (String itemStr : parts[5].split("\\|")) order.items.add(OrderItem.fromCSV(itemStr));
        }
        return order;
    }
    public String getFormattedDate() { return orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); }
    @Override public String toString() { return String.format("Order %s: %d items - ৳%.2f [%s]", orderId, items.size(), totalAmount, status); }

    public static class OrderItem {
        private String productId;
        private String productName;
        private int quantity;
        private double price;
        public OrderItem(String productId, String productName, int quantity, double price) { this.productId = productId; this.productName = productName; this.quantity = quantity; this.price = price; }
        public String getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public double getPrice() { return price; }
        public String toCSV() { return String.format("%s~%s~%d~%.2f", productId, productName, quantity, price); }
        public static OrderItem fromCSV(String csvLine) { String[] parts = csvLine.split("~"); return new OrderItem(parts[0], parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3])); }
        @Override public String toString() { return String.format("  - %s x%d @ $%.2f = $%.2f", productName, quantity, price, price * quantity); }
    }
}
