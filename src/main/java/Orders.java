import java.math.BigDecimal;
import java.util.Date;

public class Orders {
    private int order_id;
    private int user_id;
    private int product_id;
    private Date order_date;
    private BigDecimal price;
    private int quantity;
    private BigDecimal total;
    private String real_name;
    private String phone;
    private String address;

    // 构造函数
    public Orders() {
    }

    public Orders(int order_id, int user_id, int product_id,
            Date order_date, BigDecimal price, int quantity,
            BigDecimal total, String real_name, String phone, String address) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.product_id = product_id;
        this.order_date = order_date;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
        this.real_name = real_name;
        this.phone = phone;
        this.address = address;
    }

    // Getters 和 Setters - 保持两种命名风格
    // order_id
    public int getOrder_id() {
        return order_id;
    }

    public int getOrderId() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setOrderId(int order_id) {
        this.order_id = order_id;
    }

    // user_id
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

    // product_id
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

    // order_date
    public Date getOrder_date() {
        return order_date;
    }

    public Date getOrderDate() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    public void setOrderDate(Date order_date) {
        this.order_date = order_date;
    }

    // price
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // total
    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    // real_name
    public String getReal_name() {
        return real_name;
    }

    public String getRealName() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public void setRealName(String real_name) {
        this.real_name = real_name;
    }

    // phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
