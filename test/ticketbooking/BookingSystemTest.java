package ticketbooking;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingSystemTest {

    private static final double DELTA = 0.001;

    private BookingSystem system;

    @BeforeEach
    void setUp() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event(101, "Rock Festival", EventCategory.CONCERT, 12, 500.0));

        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(new Customer(201, "Ali", 10000.0));
        customers.add(new PremiumCustomer(202, "Ayse", 10000.0));
        customers.add(new Customer(203, "Mehmet", 100.0));

        system = new BookingSystem(events, customers, 0.05, true);
    }

    @Test
    void regularCustomerPaysFullPrice() {
        BookingResult result = system.bookTicket(201, 101, 1);

        assertTrue(result.isSuccess());
        assertEquals(500.0, result.getFinalPaid(), DELTA);
        assertEquals(9500.0, system.findCustomer(201).getBalance(), DELTA);
    }

    @Test
    void premiumGroupPurchaseAppliesBothDiscounts() {
        BookingResult result = system.bookTicket(202, 101, 4);

        assertTrue(result.isSuccess());
        assertEquals(2000.0, result.getOriginalTotal(), DELTA);
        assertEquals(100.0, result.getGroupDiscount(), DELTA);
        assertEquals(95.0, result.getPremiumDiscount(), DELTA);
        assertEquals(1805.0, result.getFinalPaid(), DELTA);
        assertEquals(8, system.getAvailableSeats(101));
    }

    @Test
    void cancelRefundsPaymentAndFreesSeats() {
        BookingResult booking = system.bookTicket(202, 101, 4);
        int id = booking.getBooking().getBookingId();

        CancellationResult cancellation = system.cancelBooking(id);

        assertTrue(cancellation.isSuccess());
        assertEquals(1805.0, cancellation.getRefund(), DELTA);
        assertEquals(10000.0, system.findCustomer(202).getBalance(), DELTA);
        assertEquals(12, system.getAvailableSeats(101));
        assertEquals(BookingStatus.CANCELED, system.findBooking(id).getStatus());
    }

    @Test
    void secondCancellationFailsWithoutRefund() {
        int id = system.bookTicket(201, 101, 1).getBooking().getBookingId();
        system.cancelBooking(id);

        CancellationResult second = system.cancelBooking(id);

        assertFalse(second.isSuccess());
        assertEquals("Already Cancelled", second.getReason());
        assertEquals(0.0, second.getRefund(), DELTA);
        assertEquals(10000.0, system.findCustomer(201).getBalance(), DELTA);
    }

    @Test
    void zeroQuantityIsRejected() {
        BookingResult result = system.bookTicket(201, 101, 0);

        assertFalse(result.isSuccess());
        assertEquals("Invalid Quantity", result.getReason());
        assertNull(result.getBooking());
    }

    @Test
    void tooManyTicketsAreRejected() {
        BookingResult result = system.bookTicket(201, 101, 13);

        assertFalse(result.isSuccess());
        assertEquals("Insufficient Capacity", result.getReason());
    }

    @Test
    void insufficientBalanceLeavesStateUnchanged() {
        BookingResult result = system.bookTicket(203, 101, 1);

        assertFalse(result.isSuccess());
        assertEquals("Insufficient Balance", result.getReason());
        assertEquals(100.0, system.findCustomer(203).getBalance(), DELTA);
        assertEquals(12, system.getAvailableSeats(101));
    }

    @Test
    void unknownCustomerAndEventAreRejected() {
        assertEquals("Customer Not Found", system.bookTicket(999, 101, 1).getReason());
        assertEquals("Event Not Found", system.bookTicket(201, 999, 1).getReason());
    }

    @Test
    void groupDiscountCanBeDisabled() {
        BookingSystem noDiscount = new BookingSystem(
                system.getEvents(), system.getCustomers(), 0.05, false);

        BookingResult result = noDiscount.bookTicket(201, 101, 4);

        assertEquals(0.0, result.getGroupDiscount(), DELTA);
        assertEquals(2000.0, result.getFinalPaid(), DELTA);
    }

    @Test
    void invalidDiscountRateIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new BookingSystem(new ArrayList<>(), new ArrayList<>(), 1.5, true));
    }

    @Test
    void sampleCsvFilesLoad() {
        // Maven testleri proje kök klasöründen çalıştırır, bu yüzden yol doğrudur.
        assertEquals(6, FileIO.loadEvents("events.csv").size());
        assertEquals(6, FileIO.loadCustomers("customers.csv").size());
    }
}
