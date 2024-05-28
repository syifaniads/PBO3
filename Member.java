import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class Member extends Customer {
    private LocalDate membershipDate;
    private int startBalance;

    public Member(String id, String firstName, String lastName, LocalDate membershipDate, int balance) {
        super(id, firstName, lastName, balance);
        this.membershipDate = membershipDate;
        this.startBalance = balance;
        this.cart = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    @Override
    public String getFullName() {
        return firstName + " " + lastName;
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
    public Order makeOrder(LocalDate orderDate, LocalDate endDate, double subTotal, double shippingFee, double discount, double total) {
        return new Order(orderDate, endDate, subTotal, shippingFee, discount, total, this);
    }

    public Map<Menu, CartItem> getCart() {
        Map<Menu, CartItem> cartCO = new HashMap<>();
        for (CartItem cartItem : cart.values()) {
            cartCO.put(cartItem.menuItem, cartItem);
        }
        return cartCO;
    }

    public long getMembershipDuration() {
        LocalDate today = LocalDate.now();
        return membershipDate.until(today).getDays();
    }

    public boolean isEligibleForDiscount() {
        LocalDate today = LocalDate.now();
        long membershipDuration = membershipDate.until(today).getDays();
        return membershipDuration > 100;
    }

    public String toString() {
        return "CREATE MEMBER SUCCESS: " + id + " " + getFullName();
    }
}
