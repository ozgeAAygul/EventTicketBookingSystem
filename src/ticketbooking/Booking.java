package ticketbooking;

public class Booking {
    private int bookingId;
    private Customer customer;
    private Event event;
    private int quantity;
    private double originalTotal;
    private double finalPaid;
    private BookingStatus status;

    public Booking(int bookingId, Customer customer, Event event,
                   int quantity, double originalTotal, double finalPaid) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.event = event;
        this.quantity = quantity;
        this.originalTotal = originalTotal;
        this.finalPaid = finalPaid;
        this.status = BookingStatus.ACTIVE;
    }

    public int getBookingId(){
        return bookingId;
    }

    public Customer getCustomer(){
        return customer;
    }

    public Event getEvent() {
        return event;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getOriginalTotal() {
        return originalTotal;
    }

    public double getFinalPaid(){
        return finalPaid;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void cancel() {
        status = BookingStatus.CANCELED;
    }
}
