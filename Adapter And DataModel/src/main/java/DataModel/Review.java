package DataModel;

public class Review {
    private String userId,productId,productStar;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductStar() {
        return productStar;
    }

    public void setProductStar(String productStar) {
        this.productStar = productStar;
    }

    public Review() {
    }
}
