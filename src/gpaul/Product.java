package gpaul;

import ari.Purchasable;
import ari.Searchable;

public abstract class Product implements Purchasable, Searchable {
    private String id;
    private String name;
    private double price;
    private int stockQuantity;
    private String description;

    public Product(String id, String name, double price, int stockQuantity, String description) {
        this.id = id; this.name = name; this.price = price; this.stockQuantity = stockQuantity; this.description = description;
    }
    @Override public double getPrice() { return price; }
    @Override public String getName() { return name; }
    @Override public boolean isAvailable() { return stockQuantity > 0; }
    @Override public boolean matchesKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return name.toLowerCase().contains(lowerKeyword) || description.toLowerCase().contains(lowerKeyword) || id.toLowerCase().contains(lowerKeyword);
    }
    @Override public String getSearchableInfo() { return String.format("ID: %s | Name: %s | Price: ৳%.2f | Stock: %d", id, name, price, stockQuantity); }
    public abstract String getProductType();
    public abstract String getDetailedInfo();
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public void reduceStock(int quantity) throws IllegalArgumentException {
        if (quantity > stockQuantity) throw new IllegalArgumentException("Insufficient stock. Available: " + stockQuantity);
        this.stockQuantity -= quantity;
    }
    public void addStock(int quantity) { this.stockQuantity += quantity; }
    @Override public String toString() { return String.format("%s - %s (৳%.2f) [Stock: %d]", id, name, price, stockQuantity); }
}
