package rhtahsin;

import java.util.ArrayList;
import java.util.List;
import ari.*;
import gpaul.*;

public class OrderService {
    private List<Order> orders;
    private int nextOrderId;
    public OrderService() { orders = new ArrayList<>(); nextOrderId = 1; }
    public void loadFromFile() throws FileOperationException {
        orders = FileManager.loadOrders();
        if (!orders.isEmpty()) nextOrderId = orders.stream().mapToInt(o -> Integer.parseInt(o.getOrderId().substring(1))).max().orElse(0) + 1;
        System.out.println("Loaded " + orders.size() + " orders from file.");
    }
    public void saveToFile() throws FileOperationException { FileManager.saveOrders(orders); System.out.println("Saved " + orders.size() + " orders to file."); }
    public Order createOrder(String customerId, List<CartItem> cartItems, InventoryService inventoryService) throws InsufficientStockException, ProductNotFoundException {
        for (CartItem item : cartItems) { Product product = item.getProduct(); if (product.getStockQuantity() < item.getQuantity()) throw new InsufficientStockException(product.getName(), item.getQuantity(), product.getStockQuantity()); }
        String orderId = "O" + String.format("%04d", nextOrderId++); Order order = new Order(orderId, customerId);
        for (CartItem item : cartItems) { Product product = item.getProduct(); product.reduceStock(item.getQuantity()); order.addItem(product.getId(), product.getName(), item.getQuantity(), product.getPrice()); }
        orders.add(order); System.out.println("Order created: " + orderId); return order;
    }
    public List<Order> getCustomerOrders(String customerId) { List<Order> result = new ArrayList<>(); for (Order order : orders) if (order.getCustomerId().equalsIgnoreCase(customerId)) result.add(order); return result; }
    public Order findOrderById(String orderId) { return orders.stream().filter(o -> o.getOrderId().equalsIgnoreCase(orderId)).findFirst().orElse(null); }
    public List<Order> getAllOrders() { return new ArrayList<>(orders); }
    public void displayAllOrders() { if (orders.isEmpty()) { System.out.println("No orders found."); return; } System.out.println("\n=== ALL ORDERS ==="); for (Order order : orders) System.out.println(order.toString()); }
    public void displayOrderDetails(String orderId) {
        Order order = findOrderById(orderId); if (order == null) { System.out.println("Order not found: " + orderId); return; }
        System.out.println("\n=== ORDER DETAILS ==="); System.out.println("Order ID: " + order.getOrderId()); System.out.println("Customer ID: " + order.getCustomerId());
        System.out.println("Date: " + order.getFormattedDate()); System.out.println("Status: " + order.getStatus()); System.out.println("\nItems:");
        for (Order.OrderItem item : order.getItems()) System.out.println(item.toString()); System.out.println("\nTotal: ৳" + String.format("%.2f", order.getTotalAmount()));
    }
    public String generateSalesReport() {
        StringBuilder report = new StringBuilder(); report.append("=== SALES REPORT ===\n"); report.append(String.format("Total Orders: %d\n", orders.size()));
        double totalRevenue = orders.stream().mapToDouble(Order::getTotalAmount).sum(); report.append(String.format("Total Revenue: ৳%.2f\n\n", totalRevenue));
        for (Order order : orders) report.append(String.format("%s | Customer: %s | Total: ৳%.2f | Date: %s\n", order.getOrderId(), order.getCustomerId(), order.getTotalAmount(), order.getFormattedDate()));
        return report.toString();
    }
}
