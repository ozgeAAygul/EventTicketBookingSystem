package ticketbooking;

public class PremiumCustomer extends Customer {

    public PremiumCustomer(int id, String name, double balance) {
        super(id, name, balance);
    }

    @Override
    public MembershipType getMembershipType() {
        return MembershipType.PREMIUM;
    }

    @Override
    public double calculatePrice(double amount) {
        return amount * 0.95;
    }

}
