import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {
        LocalDate orderDate;
        LocalDate endDate;
        int orderNumb;
        double subTotal;
        double total;
        Status status;
        Map<String, CartItem> menus = new HashMap<>();
        Customer customer;
        Promotion promotion;

        public Order(LocalDate orderDate, LocalDate endDate, double subTotal, double total, Customer customer, Map<String, CartItem> map) {
                this.orderDate = orderDate;
                this.endDate = endDate;
                this.subTotal = subTotal;
                this.total += subTotal;
                this.customer = customer;
                menus.putAll(map);
                this.status = Status.UNPAID;
                setOrderNumb(orderNumb);
        }

        public Order(){}
        public Map<String, CartItem> getMenus() {
                return menus;
        }
        public void setStatus(Status status) {
                this.status = status;
        }

        public void setOrderNumb(int orderNumb) {
            this.orderNumb = orderNumb;
        }
        public double getSubTotal() {
                subTotal = 0;
                for (Map.Entry<String, CartItem> map : menus.entrySet()) {
                        CartItem car = map.getValue();
                        subTotal+= car.qty * car.menuItem.Harga;
                }
                return subTotal;
        }
        public Promotion getPromotion() {
            return promotion;
        }
        public void setPromotion(Promotion promotion) {
            this.promotion = promotion;
        }
}
