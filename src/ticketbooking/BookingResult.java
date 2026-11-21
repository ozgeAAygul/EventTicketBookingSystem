package ticketbooking;

public class BookingResult {
    private boolean success;
    private String reason;
    private Booking booking;
    private double originalTotal;
    private double groupDiscount;
    private double premiumDiscount;
    private double finalPaid;

    public BookingResult(boolean success, String reason, Booking booking,
                         double originalTotal, double groupDiscount,
                         double premiumDiscount, double finalPaid){
        this.success = success;
        this.reason = reason;
        this.booking = booking;
        this.originalTotal = originalTotal;
        this.groupDiscount = groupDiscount;
        this.premiumDiscount = premiumDiscount;
        this.finalPaid = finalPaid;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReason(){
        return reason;
    }

    public Booking getBooking(){
        return booking;
    }

    public double getOriginalTotal() {
        return originalTotal;
    }

    public double getGroupDiscount() {
        return groupDiscount;
    }

    public double getPremiumDiscount() {
        return premiumDiscount;
    }

    public double getFinalPaid() {
        return finalPaid;
    }

}
