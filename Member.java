import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

class Member extends Customer {
    private LocalDate membershipDate;
    private int startBalance;
    private Order order;
    Promotion promo;

    public Member(String id, String firstName, String lastName, LocalDate membershipDate, int balance) {
        super(id, firstName, lastName, balance);
        this.membershipDate = membershipDate;
        this.startBalance = balance;
        this.cart = new HashMap<>();
    }

    public Promotion getPromo() {
        return promo;
    }

    public void setPromo(Promotion promo) {
        this.promo = promo;
    }

    public Order getOrder(){
        return this.order;
    }

    public void setOrder(Order order){
        this.order = order;
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
        long dateMember = ChronoUnit.DAYS.between(membershipDate, today);
        return dateMember;
    }


    public String toString() {
        return "CREATE MEMBER SUCCESS: " + id + " " + getFullName();
    }
}
