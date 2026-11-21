package ticketbooking;

public class Customer {
    private int id;
    private String name;
    private double balance;

    public Customer(int id, String name, double balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public double getBalance(){
        return balance;
    }

    public MembershipType getMembershipType(){
        return MembershipType.REGULAR;
    }

    public double calculatePrice(double amount) {
        return amount;
    }

    public void deductBalance(double amount) {
        balance -= amount;
    }

    public void addBalance(double amount){
        balance += amount;
    }
}
