import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

class Member extends Customer {
    private LocalDate membershipDate;
    private int startBalance;
    private Order order;
    private String name;
    Promotion promo;


    public Member(String id, String name, LocalDate membershipDate, int balance) {
        super(id, balance);
        this.name = name;
        this.membershipDate = membershipDate;
        this.startBalance = balance;
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
        return name;
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
    public Order makeOrder(LocalDate orderDate, LocalDate endDate, double subTotal, double total,Customer customer ,Map<String, CartItem> map){
        return new Order(orderDate, endDate, subTotal, total, this, map);
    }

    public Map<String, CartItem> getCart() {
        return cart;
    }

    public long getMembershipDuration() {
        LocalDate today = LocalDate.now();
        long dateMember = ChronoUnit.DAYS.between(membershipDate, today);
        return dateMember;
    }


    // public String toString() {
    //     return "CREATE MEMBER SUCCESS: " + id + " " + getFullName();
    // }
}
