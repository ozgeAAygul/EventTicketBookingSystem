package ticketbooking;

public class CancellationResult {

    private boolean success;
    private String reason;
    private double refund;

    public CancellationResult(boolean success, String reason, double refund) {
        this.success = success;
        this.reason = reason;
        this.refund = refund;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getReason() {
        return reason;
    }

    public double getRefund() {
        return refund;
    }
}
