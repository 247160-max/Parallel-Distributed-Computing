
import java.util.Random;
public class SensorGridMonitorApp {
    public static void main(String[] args) throws InterruptedException {
        int size = 50000;
        double[] sensorData = new double[size];
        Random random = new Random(42);
        // Generate sensor readings
        for (int i = 0; i < size; i++) {
            sensorData[i] = 10 + random.nextDouble() * 90;
        }
        int middle = size / 2;
        // Divide data into two halves for minimum search
        ParallelMinSearch min1 =
            new ParallelMinSearch(sensorData, 0, middle);
        ParallelMinSearch min2 =
            new ParallelMinSearch(sensorData, middle, size);
        // Divide data into two halves for maximum search
        ParallelMaxSearch max1 =
            new ParallelMaxSearch(sensorData, 0, middle);
        ParallelMaxSearch max2 =
            new ParallelMaxSearch(sensorData, middle, size);
        // Create four worker threads
        Thread t1 = new Thread(min1, "Min-Worker-1");
        Thread t2 = new Thread(min2, "Min-Worker-2");
        Thread t3 = new Thread(max1, "Max-Worker-1");
        Thread t4 = new Thread(max2, "Max-Worker-2");
        long startTime = System.nanoTime();
        // Start all four threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        // Wait for all threads to finish
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        // Combine partial results
        double overallMin =
            Math.min(min1.getResult(), min2.getResult());
        double overallMax =
            Math.max(max1.getResult(), max2.getResult());
        long endTime = System.nanoTime();
        double timeMs = (endTime - startTime) / 1_000_000.0;
        // Display final results
        System.out.println("===== SENSOR GRID MONITOR =====");
        System.out.println("Total sensor readings: " + size);
        System.out.println("Worker threads: 4");
        System.out.println("Global minimum temperature: " + overallMin);
        System.out.println("Global maximum vibration: " + overallMax);
        System.out.printf("Execution time: %.3f ms%n", timeMs);
    }
}

