package ticketbooking;

import java.util.ArrayList;

public class BookingSystem {
    private ArrayList<Event> events;
    private ArrayList<Customer> customers;
    private ArrayList<Booking> bookings;
    private double groupDiscountRate;
    private boolean groupDiscountEnabled;
    private int nextBookingId;

    public BookingSystem(ArrayList<Event> events,
                         ArrayList<Customer> customers,
                         double groupDiscountRate, boolean groupDiscountEnabled){

        if (!Double.isFinite(groupDiscountRate)
                || groupDiscountRate < 0.0
                || groupDiscountRate > 1.0) {
            throw new IllegalArgumentException(
                    "Discount rate must be between 0 and 1.");
        }

        this.events = events;
        this.customers = customers;
        this.bookings = new ArrayList<>();
        this.groupDiscountRate = groupDiscountRate;
        this.groupDiscountEnabled = groupDiscountEnabled;
        this.nextBookingId = 1;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public Customer findCustomer(int customerId) {
        for (Customer customer : customers) {
            if (customer.getId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    public Event findEvent(int eventId) {
        for (Event event : events) {
            if (event.getId() == eventId) {
                return event;
            }
        }
        return null;
    }

    public Booking findBooking(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId) {
                return booking;
            }
        }
        return null;
    }

    public int getAvailableSeats(int eventId) {
        Event event = findEvent(eventId);
        if (event == null) {
            return 0;
        }

        int reservedSeats = 0;

        for (Booking booking : bookings) {
            if (booking.getEvent().getId() == eventId
                    && booking.getStatus() == BookingStatus.ACTIVE ) {
                reservedSeats += booking.getQuantity();
            }
        }

        return event.getCapacity() - reservedSeats;
    }

    public double getGroupDiscountRate() {
        return groupDiscountRate;
    }

    public BookingResult bookTicket(int customerId, int eventId, int quantity) {
        Customer customer = findCustomer(customerId);
        if (customer == null) {
            return new BookingResult(false, "Customer Not Found",
                    null, 0.0, 0.0, 0.0, 0.0);
        }

        Event event = findEvent(eventId);
        if (event == null) {
            return new BookingResult(false, "Event Not Found",
                    null, 0.0, 0.0, 0.0, 0.0);
        }

        if (quantity <= 0) {
            return new BookingResult(false, "Invalid Quantity",
                    null, 0.0, 0.0, 0.0, 0.0);
        }

        if (quantity > getAvailableSeats(eventId)) {
            return new BookingResult(false, "Insufficient Capacity",
                    null, 0.0, 0.0, 0.0, 0.0);
        }

        double originalTotal = quantity * event.getTicketPrice();
        double groupDiscount = 0.0;

        if (groupDiscountEnabled && quantity >= 4) {
            groupDiscount = originalTotal * groupDiscountRate;
        }

        double afterGroup = originalTotal - groupDiscount;
        double finalPaid = customer.calculatePrice(afterGroup);
        double premiumDiscount = afterGroup - finalPaid;

        if (customer.getBalance() < finalPaid) {
            return new BookingResult(false, "Insufficient Balance",
                    null, 0.0, 0.0, 0.0, 0.0);
        }

        Booking booking = new Booking(nextBookingId, customer, event, quantity, originalTotal, finalPaid);

        customer.deductBalance(finalPaid);
        bookings.add(booking);
        nextBookingId++;

        return new BookingResult(true, "", booking, originalTotal, groupDiscount, premiumDiscount, finalPaid);
    }

    public CancellationResult cancelBooking(int bookingId) {
        Booking booking = findBooking(bookingId);

        if (booking == null) {
            return new CancellationResult(false, "Booking Not Found", 0.0);
        }

        if (booking.getStatus() == BookingStatus.CANCELED) {
            return new CancellationResult(false, "Already Cancelled", 0.0);
        }

        double refund = booking.getFinalPaid();

        booking.cancel();
        booking.getCustomer().addBalance(refund);

        return new CancellationResult(true, "", refund);
    }
}
