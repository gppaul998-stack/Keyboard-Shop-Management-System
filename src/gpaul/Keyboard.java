package gpaul;

public abstract class Keyboard extends Product {
    private String switchType;
    private String layout;
    private String brand;
    private boolean hasRGB;

    public Keyboard(String id, String name, double price, int stockQuantity,
                   String description, String switchType, String layout,
                   String brand, boolean hasRGB) {
        super(id, name, price, stockQuantity, description);
        this.switchType = switchType;
        this.layout = layout;
        this.brand = brand;
        this.hasRGB = hasRGB;
    }

    @Override
    public String getPurchaseDetails() {
        return String.format("Keyboard: %s | Brand: %s | Switch: %s | Layout: %s | RGB: %s | Price: ৳%.2f",
                           getName(), brand, switchType, layout, hasRGB ? "Yes" : "No", getPrice());
    }

    @Override
    public boolean matchesKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return super.matchesKeyword(keyword) || switchType.toLowerCase().contains(lowerKeyword)
               || layout.toLowerCase().contains(lowerKeyword) || brand.toLowerCase().contains(lowerKeyword);
    }

    public String getSwitchType() { return switchType; }
    public void setSwitchType(String switchType) { this.switchType = switchType; }
    public String getLayout() { return layout; }
    public void setLayout(String layout) { this.layout = layout; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public boolean isHasRGB() { return hasRGB; }
    public void setHasRGB(boolean hasRGB) { this.hasRGB = hasRGB; }
}
