import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinPortfolioApp {

    public static void main(String[] args) {
        int size = 10000;
        double riskCap = 1200.0;
        double[] assets = new double[size];

        Random random = new Random(42);

        for (int i = 0; i < size; i++) {
            assets[i] = 500 + random.nextDouble() * 1000;
        }

        ForkJoinPool pool = new ForkJoinPool();

        long totalStart = System.nanoTime();

        // Apply the 5% stress-test penalty
        PortfolioThresholdTask stressTask =
                new PortfolioThresholdTask(
                    assets, 0, assets.length, riskCap);

        pool.invoke(stressTask);

        // Calculate risk score in parallel
        long parallelStart = System.nanoTime();

        ParallelRiskCalculator riskTask =
                new ParallelRiskCalculator(
                    assets, 0, assets.length);

        double parallelRisk = pool.invoke(riskTask);

        long parallelEnd = System.nanoTime();

        // Calculate the same risk score sequentially
        long serialStart = System.nanoTime();

        double serialRisk = 0;
        for (int i = 0; i < assets.length; i++) {
            serialRisk += assets[i];
        }

        long serialEnd = System.nanoTime();
        long totalEnd = System.nanoTime();

        double parallelMs =
                (parallelEnd - parallelStart) / 1_000_000.0;

        double serialMs =
                (serialEnd - serialStart) / 1_000_000.0;

        double totalMs =
                (totalEnd - totalStart) / 1_000_000.0;

        double speedup = serialMs / parallelMs;

        System.out.println("Total assets: " + size);
        System.out.println("Risk cap: " + riskCap);
        System.out.printf("Parallel risk score: %.2f%n", parallelRisk);
        System.out.printf("Serial risk score: %.2f%n", serialRisk);
        System.out.printf("Parallel calculation time: %.3f ms%n",
                          parallelMs);
        System.out.printf("Serial calculation time: %.3f ms%n",
                          serialMs);
        System.out.printf("Total processing time: %.3f ms%n", totalMs);
        System.out.printf("Speedup ratio: %.2fx%n", speedup);

        pool.shutdown();
    }
}