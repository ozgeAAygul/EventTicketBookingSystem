package ticketbooking;

public class Query {

    private BookingSystem system;

    public Query(BookingSystem system) {
        this.system = system;
    }

    public void printMostPopularEvent() {
        Event mostPopular = null;
        int maxTickets = 0;

        for (Event event : system.getEvents()) {
            int totalTickets = 0;

            for (Booking booking : system.getBookings()) {
                if (booking.getEvent().getId() == event.getId() && booking.getStatus() == BookingStatus.ACTIVE) {
                    totalTickets += booking.getQuantity();
                }
            }

            if (totalTickets > 0
                    && (mostPopular == null || totalTickets > maxTickets
                    || (totalTickets == maxTickets && event.getId() < mostPopular.getId()) )) {
                mostPopular = event;
                maxTickets = totalTickets;
            }
        }

        if ( mostPopular == null) {
            System.out.println("Most Popular Event: No active bookings");
        } else {
            System.out.println("Most Popular Event: " + mostPopular.getName()
                    + " | " + maxTickets + " tickets");
        }
    }

    public void printHighestRevenueEvent() {
        Event highestRevenueEvent = null;
        double maxRevenue = 0.0;

        for (Event event: system.getEvents()) {
            double totalRevenue = 0.0;
            boolean hasActiveBooking = false;

            for (Booking booking: system.getBookings()) {
                if (booking.getEvent().getId() == event.getId() && booking.getStatus() == BookingStatus.ACTIVE) {
                    totalRevenue += booking.getFinalPaid();
                    hasActiveBooking = true;
                }
            }

            if (hasActiveBooking && (highestRevenueEvent == null || totalRevenue > maxRevenue
                    || (totalRevenue == maxRevenue && event.getId() < highestRevenueEvent.getId()))) {
                highestRevenueEvent = event;
                maxRevenue = totalRevenue;
            }

        }

        if (highestRevenueEvent == null) {
            System.out.println("Highest Revenue Event: No active bookings");
        } else {
            System.out.printf(java.util.Locale.US, "Highest Revenue Event: %s | %.2f%n", highestRevenueEvent.getName(), maxRevenue);
        }


    }

    public void printTopCustomer() {
        Customer topCustomer = null;
        int maxTickets = 0;

        for (Customer customer : system.getCustomers()) {
            int totalTickets = 0;

            for (Booking booking : system.getBookings()) {
                if( booking.getCustomer().getId() == customer.getId() && booking.getStatus() == BookingStatus.ACTIVE) {
                    totalTickets += booking.getQuantity();
                }
            }

            if (totalTickets > 0 && (topCustomer == null || totalTickets > maxTickets
                    || (totalTickets == maxTickets && customer.getId() < topCustomer.getId()))) {
                topCustomer = customer;
                maxTickets = totalTickets;
            }
        }

        if (topCustomer == null) {
            System.out.println("Top Customer: No active bookings");
        } else {
            System.out.println("Top Customer: " + topCustomer.getName() + " | "
                    + topCustomer.getMembershipType() + " | " + maxTickets + " tickets");
        }
    }

    public void printTicketsByCategory() {
        int concertTickets = 0;
        int conferenceTickets = 0;
        int sportsTickets = 0;

        for (Booking booking : system.getBookings()) {
            if(booking.getStatus() == BookingStatus.ACTIVE) {
                EventCategory category = booking.getEvent().getCategory();

                switch (category) {
                    case CONCERT:
                        concertTickets += booking.getQuantity();
                        break;

                    case CONFERENCE:
                        conferenceTickets += booking.getQuantity();
                        break;

                    case SPORTS:
                        sportsTickets += booking.getQuantity();
                        break;
                }
            }
        }

        System.out.println("Tickets by Category: CONCERT: " + concertTickets + " | CONFERENCE: "
        + conferenceTickets + " | SPORTS: " + sportsTickets);
    }
}
