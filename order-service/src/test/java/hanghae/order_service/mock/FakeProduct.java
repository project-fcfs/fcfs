package hanghae.order_service.mock;

public class FakeProduct {

    private String name;
    private int price;
    private int quantity;
    private Long productId;

    public FakeProduct(String name, int price, int quantity, Long productId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.productId = productId;
    }

    public static FakeProduct create(String name, int price, int quantity, Long productId) {
        return new FakeProduct(name, price, quantity, productId);
    }

    public void removeStock(int count) {
        int remainingQuantity = quantity - count;
        if (remainingQuantity < 0) {
            throw new IllegalArgumentException("out of stock");
        }
        this.quantity = remainingQuantity;
    }

    public void addStock(int count) {
        this.quantity += count;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }
}
