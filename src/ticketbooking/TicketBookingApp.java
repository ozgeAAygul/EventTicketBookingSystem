package ticketbooking;

import java.util.ArrayList;
import java.util.Locale;

public class TicketBookingApp {

    public static void main(String[] args) {

        ArrayList<Event> events = FileIO.loadEvents("events.csv");
        ArrayList<Customer> customers = FileIO.loadCustomers("customers.csv");

        BookingSystem system = new BookingSystem(events, customers, 0.05, true);

        System.out.println(" ================= EVENT TICKET BOOKING ================= ");
        printEvents(system);
        printCustomers(system);

        System.out.println("\nBooking Demo");

        BookingResult regularBooking = system.bookTicket(201, 101, 1);
        printBookingResult("Regular purchase", regularBooking);

        BookingResult premiumBooking = system.bookTicket(202, 101, 4);
        printBookingResult("Premium group purchase", premiumBooking);

        BookingResult invalidBooking = system.bookTicket(201, 101, 0);
        printBookingResult("Invalid quantity example", invalidBooking);

        System.out.println("\nCancellation Demo");

        if (regularBooking.isSuccess()) {
            int bookingId = regularBooking.getBooking().getBookingId();

            CancellationResult cancellation = system.cancelBooking(bookingId);
            printCancellationResult("Cancel regular purchase", cancellation);

            CancellationResult repeatedCancellation = system.cancelBooking(bookingId);
            printCancellationResult("Cancel again", repeatedCancellation);
        }

        System.out.println("\nReports");

        Query query = new Query(system);
        query.printMostPopularEvent();
        query.printHighestRevenueEvent();
        query.printTopCustomer();
        query.printTicketsByCategory();

        System.out.println("\nUpdated System");
        printEvents(system);
        printCustomers(system);
    }

    private static void printEvents(BookingSystem system) {
        System.out.println("\nAvailable Events");

        for (Event event : system.getEvents()) {
            System.out.printf(Locale.US, "%d | %s | %s | Seats: %d | Price: %.2f%n",
                    event.getId(), event.getName(), event.getCategory(), system.getAvailableSeats(event.getId()), event.getTicketPrice());
        }
    }

    private static void printCustomers(BookingSystem system) {
        System.out.println("\nCustomers");

        for (Customer customer : system.getCustomers()) {
            System.out.printf(Locale.US, "%d | %s | %s | Balance: %.2f%n"
                    , customer.getId(), customer.getName(), customer.getMembershipType(), customer.getBalance());
        }
    }

    private static void printBookingResult(String label, BookingResult result){

        if (result.isSuccess()) {
           System.out.printf(Locale.US, "%s: SUCCESS | Booking ID: %d | Paid: %.2f%n",
                   label, result.getBooking().getBookingId(), result.getFinalPaid());

           System.out.printf(Locale.US, " Original: %.2f | Group discount: %.2f" + " | Premium discount: %.2f%n",
                   result.getOriginalTotal(), result.getGroupDiscount(), result.getPremiumDiscount());
        } else {
            System.out.println(label + ": FAILED | " + result.getReason());
        }

    }

    private static void printCancellationResult(String label, CancellationResult result) {

        if (result.isSuccess()) {
            System.out.printf(Locale.US, "%s: SUCCESS | Refund: %.2f%n",
                    label, result.getRefund());
        } else {
            System.out.println(label + ": FAILED | " + result.getReason());
        }
    }

}
