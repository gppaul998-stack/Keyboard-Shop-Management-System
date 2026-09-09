package gpaul;

public class MechanicalKeyboard extends Keyboard {
    private boolean hotSwappable;
    private String connectivity;

    public MechanicalKeyboard(String id, String name, double price, int stockQuantity,
                             String description, String switchType, String layout,
                             String brand, boolean hasRGB, boolean hotSwappable, String connectivity) {
        super(id, name, price, stockQuantity, description, switchType, layout, brand, hasRGB);
        this.hotSwappable = hotSwappable;
        this.connectivity = connectivity;
    }

    @Override
    public String getProductType() { return "Mechanical Keyboard"; }

    @Override
    public String getDetailedInfo() {
        return String.format("=== MECHANICAL KEYBOARD ===\nID: %s\nName: %s\nBrand: %s\nPrice: ৳%.2f\nStock: %d units\nDescription: %s\nSwitch Type: %s\nLayout: %s\nRGB Lighting: %s\nHot-Swappable: %s\nConnectivity: %s",
            getId(), getName(), getBrand(), getPrice(), getStockQuantity(), getDescription(), getSwitchType(),
            getLayout(), isHasRGB() ? "Yes" : "No", hotSwappable ? "Yes" : "No", connectivity);
    }

    public boolean isHotSwappable() { return hotSwappable; }
    public void setHotSwappable(boolean hotSwappable) { this.hotSwappable = hotSwappable; }
    public String getConnectivity() { return connectivity; }
    public void setConnectivity(String connectivity) { this.connectivity = connectivity; }

    public String toCSV() {
        return String.format("MECH,%s,%s,%.2f,%d,%s,%s,%s,%s,%s,%s,%s", getId(), getName(), getPrice(),
            getStockQuantity(), getDescription().replace(",", ";"), getSwitchType(), getLayout(), getBrand(), isHasRGB(), hotSwappable, connectivity);
    }

    public static MechanicalKeyboard fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", 12);
        return new MechanicalKeyboard(parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[4]),
            parts[5].replace(";", ","), parts[6], parts[7], parts[8], Boolean.parseBoolean(parts[9]), Boolean.parseBoolean(parts[10]), parts[11]);
    }
}
