import java.time.LocalDate;

class CashbackPromo extends Promotion {

    CashbackPromo(String promoCode, LocalDate startDate, LocalDate endDate, int percentPieces, int maxPieces, int minPurchase, String promoType) {
        super(promoCode, startDate, endDate, percentPieces, maxPieces, minPurchase, promoType);
    }

    @Override
    public int compareTo(Promotion o) {
        return 0;
    }

    @Override
    public String getPromoCode() {
        return this.promoCode;
    }

    @Override
    public boolean isCustomerEligible(Customer customer) {
        // Umur akun lebih dari 30 hari
        if (customer instanceof Guest) {
            return false; // Guest tidak berlaku
        }
        else if (customer instanceof Member) {
            long membershipDuration = ((Member) customer).getMembershipDuration();
            return membershipDuration > 30;
        }
        return true;
    }

    @Override
    public boolean isMinimumPriceEligible(Order order) {
        if(order.subTotal < super.minPurchase){
            return false;
        }
        return true;
    }

    @Override
    public double calculateTotalDiscount(Order order) throws Exception {
        return 0;
    }

    @Override
    public double calculateTotalCashback(Order order) throws Exception {
        return Math.min(order.subTotal * super.percentPieces, super.maxPieces);
    }

}
