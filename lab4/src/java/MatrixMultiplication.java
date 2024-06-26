package javaopt;

import java.lang.Thread;
import java.util.Arrays;

public class MatrixMultiplication {
    public static native void nativeVariant(int a[], int b[], int res[], int m, int n, int k);

    public static void javaVariant(int a[], int b[], int res[], int m, int n, int k) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int sum = 0;
                for (int p = 0; p < k; p++) {
                    sum += a[i * k + p] * b[p * n + j];
                }

                res[i * n + j] = sum;
            }
        }
    }
}
