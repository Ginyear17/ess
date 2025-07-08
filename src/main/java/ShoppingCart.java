import java.util.Date;

public class ShoppingCart {
    private int cart_id;
    private int user_id;
    private int product_id;
    private int quantity;
    private Date added_at;

    // 构造函数
    public ShoppingCart() {
    }

    public ShoppingCart(int cart_id, int user_id, int product_id, int quantity, Date added_at) {
        this.cart_id = cart_id;
        this.user_id = user_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.added_at = added_at;
    }

    // Getters和Setters，保持两种命名风格一致性
    // cart_id 相关方法
    public int getCart_id() {
        return cart_id;
    }

    public int getCartId() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public void setCartId(int cart_id) {
        this.cart_id = cart_id;
    }

    // user_id 相关方法
    public int getUser_id() {
        return user_id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    // product_id 相关方法
    public int getProduct_id() {
        return product_id;
    }

    public int getProductId() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public void setProductId(int product_id) {
        this.product_id = product_id;
    }

    // quantity 相关方法
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // added_at 相关方法
    public Date getAdded_at() {
        return added_at;
    }

    public Date getAddedAt() {
        return added_at;
    }

    public void setAdded_at(Date added_at) {
        this.added_at = added_at;
    }

    public void setAddedAt(Date added_at) {
        this.added_at = added_at;
    }
}
