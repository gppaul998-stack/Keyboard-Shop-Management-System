package gpaul;

public class CustomKeyboard extends Keyboard {
    private String keycapMaterial;
    private String caseColor;
    private boolean isCustomBuilt;

    public CustomKeyboard(String id, String name, double price, int stockQuantity,
                         String description, String switchType, String layout,
                         String brand, boolean hasRGB, String keycapMaterial,
                         String caseColor, boolean isCustomBuilt) {
        super(id, name, price, stockQuantity, description, switchType, layout, brand, hasRGB);
        this.keycapMaterial = keycapMaterial;
        this.caseColor = caseColor;
        this.isCustomBuilt = isCustomBuilt;
    }

    @Override
    public String getProductType() { return "Custom Keyboard"; }

    @Override
    public String getDetailedInfo() {
        return String.format(
            "=== CUSTOM KEYBOARD ===\n" +
            "ID: %s\nName: %s\nBrand: %s\nPrice: ৳%.2f\nStock: %d units\n" +
            "Description: %s\nSwitch Type: %s\nLayout: %s\nRGB Lighting: %s\n" +
            "Keycap Material: %s\nCase Color: %s\nCustom Built: %s",
            getId(), getName(), getBrand(), getPrice(), getStockQuantity(),
            getDescription(), getSwitchType(), getLayout(), isHasRGB() ? "Yes" : "No",
            keycapMaterial, caseColor, isCustomBuilt ? "Yes" : "No");
    }

    public String getKeycapMaterial() { return keycapMaterial; }
    public void setKeycapMaterial(String keycapMaterial) { this.keycapMaterial = keycapMaterial; }
    public String getCaseColor() { return caseColor; }
    public void setCaseColor(String caseColor) { this.caseColor = caseColor; }
    public boolean isCustomBuilt() { return isCustomBuilt; }
    public void setCustomBuilt(boolean customBuilt) { isCustomBuilt = customBuilt; }

    public String toCSV() {
        return String.format("CUSTOM,%s,%s,%.2f,%d,%s,%s,%s,%s,%s,%s,%s,%s",
                           getId(), getName(), getPrice(), getStockQuantity(),
                           getDescription().replace(",", ";"), getSwitchType(),
                           getLayout(), getBrand(), isHasRGB(), keycapMaterial, caseColor, isCustomBuilt);
    }

    public static CustomKeyboard fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", 13);
        return new CustomKeyboard(parts[1], parts[2], Double.parseDouble(parts[3]),
            Integer.parseInt(parts[4]), parts[5].replace(";", ","), parts[6], parts[7], parts[8],
            Boolean.parseBoolean(parts[9]), parts[10], parts[11], Boolean.parseBoolean(parts[12]));
    }
}
