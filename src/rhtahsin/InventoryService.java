package rhtahsin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import ari.*;
import gpaul.*;

public class InventoryService {
    private List<Product> products;
    public InventoryService() { products = new ArrayList<>(); }
    public void loadFromFile() throws FileOperationException { products = FileManager.loadInventory(); System.out.println("Loaded " + products.size() + " products from file."); }
    public void saveToFile() throws FileOperationException { FileManager.saveInventory(products); System.out.println("Saved " + products.size() + " products to file."); }
    public void addProduct(Product product) { products.add(product); System.out.println("Product added: " + product.getName()); }
    public void removeProduct(String productId) throws ProductNotFoundException { Product product = findProductById(productId); products.remove(product); System.out.println("Product removed: " + product.getName()); }
    public Product findProductById(String productId) throws ProductNotFoundException { return products.stream().filter(p -> p.getId().equalsIgnoreCase(productId)).findFirst().orElseThrow(() -> new ProductNotFoundException("Product with ID '" + productId + "' not found")); }
    public List<Product> searchProducts(String keyword) { return products.stream().filter(p -> p.matchesKeyword(keyword)).collect(Collectors.toList()); }
    public List<Product> getAllProducts() { return new ArrayList<>(products); }
    public List<Product> getAvailableProducts() { return products.stream().filter(Product::isAvailable).collect(Collectors.toList()); }
    public void updateStock(String productId, int newStock) throws ProductNotFoundException { Product product = findProductById(productId); product.setStockQuantity(newStock); System.out.println("Updated stock for " + product.getName() + " to " + newStock); }
    public void displayAllProducts() { if (products.isEmpty()) { System.out.println("No products in inventory."); return; } System.out.println("\n=== INVENTORY ==="); for (Product product : products) System.out.println(product.toString()); }
    public void displayProductDetails(String productId) throws ProductNotFoundException { Product product = findProductById(productId); System.out.println("\n" + product.getDetailedInfo()); }
    public String generateInventoryReport() {
        StringBuilder report = new StringBuilder(); report.append("=== INVENTORY REPORT ===\n");
        report.append(String.format("Total Products: %d\n", products.size()));
        report.append(String.format("Available Products: %d\n", products.stream().filter(Product::isAvailable).count())); report.append("\n");
        for (Product product : products) report.append(product.toString()).append("\n"); return report.toString();
    }
}
