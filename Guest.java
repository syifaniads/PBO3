import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class Guest extends Customer {
    private int startBalance;

    public Guest(String id, int balance) {
        super(id, "GUEST", "", balance);
        this.startBalance = balance;
        this.cart = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public Map<Menu, CartItem> getCart() {
        Map<Menu, CartItem> cartCO = new HashMap<>();
        for (CartItem cartItem : cart.values()) {
            cartCO.put(cartItem.menuItem, cartItem);
        }
        return cartCO;
    }

    @Override
    public String getFullName() {
        return "Guest";
    }

    @Override
    public int getBalance() {
        return startBalance;
    }

    @Override
    public void topUp(int amount) {
        this.balance += amount;
    }

    @Override
    Order makeOrder(LocalDate orderDate, LocalDate endDate, double subTotal, double shippingFee, double discount, double total) {
        return new Order(orderDate, endDate, subTotal, shippingFee, discount, total, this);
    }

    public String toString() {
        return "CREATE GUEST SUCCESS: " + id;
    }
}
