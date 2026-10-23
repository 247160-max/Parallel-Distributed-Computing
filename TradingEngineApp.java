public class TradingEngineApp {
    public static void main(String[] args) {

        TradeEvent event1 = new FIXOrderEvent(
            "FIX-Gateway", System.currentTimeMillis(),
            10000.0, "ORD-101"
        );

        TradeEvent event2 = new ReutersQuoteEvent(
            "Reuters-Gateway", System.currentTimeMillis(),
            100.0, 99.50, 100.00
        );

        event1.dispatch();
        event2.dispatch();
    }
}