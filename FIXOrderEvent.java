public class FIXOrderEvent extends TradeEvent {
    private String clOrdId;

    public FIXOrderEvent(String senderId, long timestamp,
                         double basePrice, String clOrdId) {
        super(senderId, timestamp, basePrice);
        this.clOrdId = clOrdId;
    }

    @Override
    public void dispatch() {
        double fee = basePrice * 0.15 / 100;
        double totalCost = basePrice + fee;

        System.out.println("\n--- FIX Purchase Order ---");
        System.out.println("Sender ID: " + senderId);
        System.out.println("Order ID: " + clOrdId);
        System.out.println("Base Price: " + basePrice);
        System.out.println("Transaction Fee: " + fee);
        System.out.println("Total Cost: " + totalCost);
    }
}