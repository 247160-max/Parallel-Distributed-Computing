
public class CacheLocalityBenchmark {
    private static final int N = 2000;
    private static final int REPETITIONS = 10;
    private static final double[][] matrix = new double[N][N];
    private static volatile double result;
    // Initialize the matrix
    public static void initializeMatrix() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = i + j * 0.5;
            }
        }
    }
    // Traverse row by row
    public static double traverseRowMajor() {
        double sum = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }
    // Traverse column by column
    public static double traverseColumnMajor() {
        double sum = 0;
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < N; i++) {
                sum += matrix[i][j];
            }
        }

        return sum;
    }
    public static void main(String[] args) {
        initializeMatrix();
        // Warm-up before measuring
        result = traverseRowMajor();
        result = traverseColumnMajor();
        long rowTotal = 0;
        long columnTotal = 0;
        for (int i = 0; i < REPETITIONS; i++) {
            long start = System.nanoTime();
            result = traverseRowMajor();
            long end = System.nanoTime();

            rowTotal += end - start;
        }
        for (int i = 0; i < REPETITIONS; i++) {
            long start = System.nanoTime();
            result = traverseColumnMajor();
            long end = System.nanoTime();
            columnTotal += end - start;
        }
        double rowAverage =
                rowTotal / (double) REPETITIONS / 1_000_000.0;
        double columnAverage =
                columnTotal / (double) REPETITIONS / 1_000_000.0;
        double speedup = columnAverage / rowAverage;
        System.out.println("=== Cache Locality Benchmark ===");
        System.out.println("Matrix size: " + N + " x " + N);
        System.out.println("Repetitions: " + REPETITIONS);

        System.out.printf(
                "Average Row-Major time: %.3f ms%n", rowAverage);
        System.out.printf(
                "Average Column-Major time: %.3f ms%n", columnAverage);
        System.out.printf(
                "Row-Major speedup ratio: %.2fx%n", speedup);
        System.out.println("Verification sum: " + result);
    }
}
