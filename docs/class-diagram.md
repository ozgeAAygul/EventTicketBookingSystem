# Class diagram

The diagrams describe the source in `src/ticketbooking`. Solid arrows show stored references or inheritance. Dashed arrows show method-level dependencies. Collection multiplicities indicate the number of objects a field can reference.

## Domain model

```mermaid
classDiagram
    direction TB
    Customer <|-- PremiumCustomer
    Booking "0..*" --> "1" Customer : customer
    Booking "0..*" --> "1" Event : event
    Customer ..> MembershipType : returns
    PremiumCustomer ..> MembershipType : returns PREMIUM
    Event --> EventCategory : category
    Booking --> BookingStatus : status

    class Customer {
        -int id
        -String name
        -double balance
        +Customer(int id, String name, double balance)
        +getMembershipType() MembershipType
        +calculatePrice(double amount) double
        +deductBalance(double amount) void
        +addBalance(double amount) void
    }
    class PremiumCustomer {
        +PremiumCustomer(int id, String name, double balance)
        +getMembershipType() MembershipType
        +calculatePrice(double amount) double
    }
    class Event {
        -int id
        -String name
        -EventCategory category
        -int capacity
        -double ticketPrice
    }
    class Booking {
        -int bookingId
        -Customer customer
        -Event event
        -int quantity
        -double originalTotal
        -double finalPaid
        -BookingStatus status
        +cancel() void
    }
    class MembershipType {
        <<enumeration>>
        REGULAR
        PREMIUM
    }
    class EventCategory {
        <<enumeration>>
        CONCERT
        CONFERENCE
        SPORTS
    }
    class BookingStatus {
        <<enumeration>>
        ACTIVE
        CANCELED
    }
```

Customer, Event, Booking, and the result classes provide getters for their stored data. PremiumCustomer inherits customer identification and balance operations and overrides membership and price calculation. Event capacity always represents total capacity. Remaining seats are calculated by BookingSystem.

## Booking operations and reporting

```mermaid
classDiagram
    direction TB
    BookingSystem "1" --> "0..*" Event : events
    BookingSystem "1" --> "0..*" Customer : customers
    BookingSystem "1" --> "0..*" Booking : bookings
    Query --> BookingSystem : system
    BookingSystem ..> BookingResult : returns
    BookingSystem ..> CancellationResult : returns
    BookingResult --> "0..1" Booking : booking

    class BookingSystem {
        -double groupDiscountRate
        -boolean groupDiscountEnabled
        -int nextBookingId
        +getEvents() ArrayList~Event~
        +getCustomers() ArrayList~Customer~
        +getBookings() ArrayList~Booking~
        +findCustomer(int customerId) Customer
        +findEvent(int eventId) Event
        +findBooking(int bookingId) Booking
        +getAvailableSeats(int eventId) int
        +getGroupDiscountRate() double
        +bookTicket(int customerId, int eventId, int quantity) BookingResult
        +cancelBooking(int bookingId) CancellationResult
    }
    class BookingResult {
        -boolean success
        -String reason
        -Booking booking
        -double originalTotal
        -double groupDiscount
        -double premiumDiscount
        -double finalPaid
    }
    class CancellationResult {
        -boolean success
        -String reason
        -double refund
    }
    class Query {
        -BookingSystem system
        +Query(BookingSystem system)
        +printMostPopularEvent() void
        +printHighestRevenueEvent() void
        +printTopCustomer() void
        +printTicketsByCategory() void
    }
```

A failed purchase returns a BookingResult with a null booking and zero monetary fields. A successful purchase stores a Booking and returns its reference. CancellationResult reports success or failure and the refunded amount.

## Loading and application entry points

```mermaid
classDiagram
    direction TB
    TicketBookingApp ..> FileIO : loads records
    TicketBookingApp ..> BookingSystem : creates and calls
    TicketBookingApp ..> Query : creates and calls
    TicketBookingApp ..> BookingResult : reads
    TicketBookingApp ..> CancellationResult : reads
    FileIO ..> Event : creates
    FileIO ..> Customer : creates
    FileIO ..> PremiumCustomer : creates
    FileIO ..> EventCategory : parses
    FileIO ..> MembershipType : parses
    

    class TicketBookingApp {
        +main(String[] args)$ void
    }
    class FileIO {
        +loadEvents(String filename)$ ArrayList~Event~
        +loadCustomers(String filename)$ ArrayList~Customer~
    }
```

TicketBookingApp runs the public demonstration. FileIO reads UTF-8 CSV rows and creates the corresponding model objects.

## Purchase and cancellation flow

1. BookingSystem finds the customer and event and validates quantity and available seats.
2. It calculates the original total and any enabled group discount.
3. It calls `customer.calculatePrice(...)`. The actual customer type selects the regular or premium implementation.
4. It checks the final payment against the balance. On success it deducts payment, stores the booking, and advances the ID.
5. For cancellation it finds an active booking, changes its status, and credits its final paid amount back to the customer.
6. Query reads only ACTIVE bookings when calculating reports.
