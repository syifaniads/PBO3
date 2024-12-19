import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class Guest extends Customer {
    private int startBalance;

    public Guest(String id, int balance) {
        super(id, balance);
        this.startBalance = balance;
    }

    public String getId() {
        return id;
    }

    public Map<String, CartItem> getCart() {
        return cart;
    }

    @Override
    public Promotion getPromo() {
        return null;
    }

    @Override
    public void setPromo(Promotion promo) {

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
    public Order makeOrder(LocalDate orderDate, LocalDate endDate, double subTotal, double total, Customer customer, Map<String, CartItem> map){
        return new Order(orderDate, endDate, subTotal, total, this, map);
    }

    public String toString() {
        return "CREATE GUEST SUCCESS: " + id;
    }
}
