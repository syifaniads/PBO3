import java.time.LocalDate;
import java.util.*;
public class Order {
        LocalDate orderDate;
        LocalDate endDate;
        int orderNumber;
        double subTotal;
        double shippingFee;
        double discount;
        double total;
        Status status;
        Promotion promotion;
        List<CartItem> menus;
        Customer customer;

        public Order(LocalDate orderDate, LocalDate endDate, double subTotal, double shippingFee, double discount, double total, Customer customer) {
                this.orderDate = orderDate;
                this.endDate = endDate;
//                this.orderNumber = orderNumber;
                this.subTotal = subTotal;
                this.shippingFee = shippingFee;
                this.discount = discount;
                this.total += subTotal + shippingFee - discount;
                this.customer = customer;
                this.menus = new ArrayList<>();
                this.status = Status.UNPAID;
        }

        public List<CartItem> getMenus() {
                return menus;
        }

        // class Pelanggan harus diisi dari constructor
        public void checkOut() {
                if (status == Status.UNPAID) {
                        status = Status.SUCCESSFUL;
                        this.orderDate = LocalDate.now();
                } else {
                        System.out.println("The order has been paid or cancelled.");
                }
        }

        public void pay() {
                if (status == Status.SUCCESSFUL) {
                        System.out.println("Payment successful.");
                        status = Status.SUCCESSFUL;
                } else {
                        System.out.println("Order has not been paid for.");
                }
        }
}
