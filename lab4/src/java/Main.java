package javaopt;

import java.lang.Runtime;
import java.util.Arrays;
import java.util.logging.Logger;

public class Main {
    static {
        System.loadLibrary("nativemmul");
    }

    private static final Logger log = Logger.getLogger("main");
    
    public final static int WARMUPS = 12;
    public final static int RUNS = 12;

    private static void benchBoth(int n) { // matrix is n*n
        System.err.println("BENCH WITH " + n + "x" + n + " MATRIX!");
        int[] a = new int[n*n];
        int[] b = new int[n*n];
        int[] res = new int[n*n];

        for (int i = 0; i < n*n; i++) {
            a[i] = 3 * (i - 500);
            b[i] = 2 * i + 1023;
        }


        // JAVA VARIANT 
        System.err.println("--- JAVA ---");
        System.err.println("(java) Warmup " + WARMUPS + " times");
        for (int i = 0; i < WARMUPS; i++) {
            MatrixMultiplication.javaVariant(a, b, res, n, n, n);
            System.err.print("+");
        }

        System.err.println("\n(java) Measuring " + RUNS + " times");
        long sumJava = 0;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();

            MatrixMultiplication.javaVariant(a, b, res, n, n, n);

            long finish = System.nanoTime();
            long timeElapsed = finish - start;
            sumJava += timeElapsed;

            System.err.println("Try " + i + ": " + timeElapsed + " * 10^-9 sec   (" + (double) timeElapsed / 1_000_000_000 + "sec)");
        }
        System.err.println("(java) Avg: " + sumJava/RUNS + " * 10^-9 sec   (" + (double) sumJava / RUNS / 1_000_000_000 + "sec)");
        System.err.println("");
        

        // NATIVE
        System.err.println("--- NATIVE ---");
        System.err.println("(native) Warmup " + WARMUPS + " times");
        for (int i = 0; i < WARMUPS; i++) {
            MatrixMultiplication.nativeVariant(a, b, res, n, n, n);
            System.err.print("+");
        }

        System.err.println("\n(native) Measuring " + RUNS + " times");
        long sumNative = 0;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();

            MatrixMultiplication.nativeVariant(a, b, res, n, n, n);

            long finish = System.nanoTime();
            long timeElapsed = finish - start;
            sumNative += timeElapsed;

            System.err.println("Try " + i + ": " + timeElapsed + " * 10^-9 sec   (" + (double) timeElapsed / 1_000_000_000 + "sec)");
        }
        System.err.println("(native) Avg: " + sumNative/RUNS + " * 10^-9 sec    (" + (double) sumNative / RUNS / 1_000_000_000 + "sec)");
        System.err.println("");
        System.err.println("");


        System.err.println("Java is " + (double) sumJava/sumNative + " times slower than native :)");
        System.err.println("");
        System.err.println("");
    }

    public static void main(String[] args) {
        benchBoth(16);
        benchBoth(128);
        benchBoth(1024);
    }
}
