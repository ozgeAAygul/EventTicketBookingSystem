package ticketbooking;

public class Event {
    private int id;
    private String name;
    private EventCategory category;
    private int capacity;
    private double ticketPrice;

    public Event(int id, String name, EventCategory category,
                 int capacity, double ticketPrice ) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.category = category;
        this.ticketPrice = ticketPrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public EventCategory getCategory() {
        return category;
    }

    public int getCapacity(){
        return capacity;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }
}
