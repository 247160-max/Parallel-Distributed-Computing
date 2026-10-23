
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
public class InventoryCatalogManager {
    private static final int THREADS = 10;
    private static final int OPERATIONS = 50000;
    private static final int PRODUCTS = 100;
    public static void benchmarkSynchronizedMap() throws Exception {
        Map<String, Integer> stock =
                Collections.synchronizedMap(new HashMap<>());
        runBenchmark("Collections.synchronizedMap", stock);
    }
    public static void benchmarkConcurrentMap() throws Exception {
        Map<String, Integer> stock = new ConcurrentHashMap<>();
        runBenchmark("ConcurrentHashMap", stock);
    }
    private static void runBenchmark(
            String mapName, Map<String, Integer> stock) throws Exception {
        // Initialize product stock
        for (int i = 0; i < PRODUCTS; i++) {
            stock.put("Product-" + i, 100);
        }
        ExecutorService executor =
                Executors.newFixedThreadPool(THREADS);
        int operationsPerThread = OPERATIONS / THREADS;
        Future<?>[] tasks = new Future<?>[THREADS];
        long startTime = System.nanoTime();
        // Start worker threads
        for (int t = 0; t < THREADS; t++) {
            final int workerId = t;
            tasks[t] = executor.submit(() -> {
                for (int i = 0; i < operationsPerThread; i++) {
                    int productNumber =
                            (i + workerId) % PRODUCTS;
                    String productId = "Product-" + productNumber;
                    if (i % 2 == 0) {
                        // Read stock quantity
                        Integer quantity = stock.get(productId);
                    } else {
                        // Safely update stock quantity
                        stock.compute(productId,
                                (key, quantity) -> quantity + 1);
                    }
                }
            });
        }
        // Wait for all worker tasks to finish
        for (Future<?> task : tasks) {
            task.get();
        }
        long endTime = System.nanoTime();
        executor.shutdown();
        double seconds =
                (endTime - startTime) / 1_000_000_000.0;
        double milliseconds = seconds * 1000.0;
        double throughput = OPERATIONS / seconds;
        System.out.println("\nMap type: " + mapName);
        System.out.println("Worker threads: " + THREADS);
        System.out.println("Total operations: " + OPERATIONS);
        System.out.printf(
                "Execution time: %.3f ms%n", milliseconds);
        System.out.printf(
                "Throughput: %.2f operations/second%n", throughput);
    }
    public static void main(String[] args) throws Exception {
        System.out.println("=== Inventory Concurrency Benchmark ===");
        benchmarkSynchronizedMap();
        benchmarkConcurrentMap();
        System.out.println("\nBenchmark completed.");
    }
}