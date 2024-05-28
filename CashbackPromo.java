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
        return false;
    }

    @Override
    public boolean isMinimumPriceEligible(Order order) {
        return false;
    }

    @Override
    public boolean isShippingFeeEligible(Order order) {
        return false;
    }

    @Override
    public double calculateTotalDiscount(Order order) throws Exception {
        return 0;
    }

    @Override
    public double calculateTotalCashback(Order order) throws Exception {
        return 0;
    }

    @Override
    public double calculateTotalShippingFeeDiscount(Order order) throws Exception {
        return 0;
    }
}
