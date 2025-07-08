import java.math.BigDecimal;
import java.util.Date;

public class Orders {
    private int order_id;
    private int user_id;
    private Date order_date;
    private BigDecimal order_amount;
    private String status; // enum('pending','shipped','delivered','cancelled')
    private String shipping_address;
    private Date created_at;

    // 构造函数
    public Orders() {
    }

    public Orders(int order_id, int user_id, Date order_date, BigDecimal order_amount,
            String status, String shipping_address, Date created_at) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.order_date = order_date;
        this.order_amount = order_amount;
        this.status = status;
        this.shipping_address = shipping_address;
        this.created_at = created_at;
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

    // order_amount
    public BigDecimal getOrder_amount() {
        return order_amount;
    }

    public BigDecimal getOrderAmount() {
        return order_amount;
    }

    public void setOrder_amount(BigDecimal order_amount) {
        this.order_amount = order_amount;
    }

    public void setOrderAmount(BigDecimal order_amount) {
        this.order_amount = order_amount;
    }

    // status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // shipping_address
    public String getShipping_address() {
        return shipping_address;
    }

    public String getShippingAddress() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public void setShippingAddress(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    // created_at
    public Date getCreated_at() {
        return created_at;
    }

    public Date getCreatedAt() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public void setCreatedAt(Date created_at) {
        this.created_at = created_at;
    }
}
