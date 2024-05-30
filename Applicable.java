interface Applicable {
    String getPromoCode();
    boolean isCustomerEligible(Customer customer);
    boolean isMinimumPriceEligible(Order order);
    double calculateTotalDiscount(Order order) throws Exception;
    double calculateTotalCashback(Order order) throws Exception;
    double calculateTotalShippingFeeDiscount(Order order) throws Exception;
}
