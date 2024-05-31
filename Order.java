import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
        LocalDate orderDate;
        LocalDate endDate;
        int orderNumber = 1;
        double subTotal;
        double total;
        Status status;
        List<CartItem> menus;
        Customer customer;

        public Order(LocalDate orderDate, LocalDate endDate, double subTotal, double total, Customer customer) {
                this.orderDate = orderDate;
                this.endDate = endDate;
                this.subTotal = subTotal;
                this.total += subTotal;
                this.customer = customer;
                this.menus = new ArrayList<>();
                this.status = Status.UNPAID;
        }

        public Order(){}
        public List<CartItem> getMenus() {
                return menus;
        }
        public void setStatus(Status status) {
                this.status = status;
        }
}
