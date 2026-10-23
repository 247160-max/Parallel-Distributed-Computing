public class AsyncWebEngineApp {

    public static void main(String[] args) throws InterruptedException {

        System.out.println(
            "Main thread: " + Thread.currentThread().getName()
        );

        TelemetryThread telemetry = new TelemetryThread();
        telemetry.setName("Telemetry-Worker");

        Thread logWorker = new Thread(
            new LogProcessorTask(),
            "Log-Worker"
        );

        telemetry.start();
        logWorker.start();

        System.out.println(
            "Telemetry active: " + telemetry.isAlive()
        );

        System.out.println(
            "Log worker active: " + logWorker.isAlive()
        );

        telemetry.join();
        logWorker.join();

        System.out.println("All worker threads completed.");
    }
}