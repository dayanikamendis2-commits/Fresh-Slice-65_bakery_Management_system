package model;

public class Stock {
    private int id;
    private int productId;
    private int quantity;
    private int restockLevel;

    public Stock() {}

    public Stock(int id, int productId, int quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.restockLevel = 10; // default restock level
    }

    public Stock(int id, int productId, int quantity, int restockLevel) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.restockLevel = restockLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRestockLevel() {
        return restockLevel;
    }

    public void setRestockLevel(int restockLevel) {
        this.restockLevel = restockLevel;
    }

    /**
     * Check if stock needs restocking
     */
    public boolean needsRestock() {
        return quantity <= restockLevel;
    }
}
