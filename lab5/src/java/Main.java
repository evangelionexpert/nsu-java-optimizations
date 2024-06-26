package javaopt;

import java.util.Random;

public class Main {
    public final static int N = 10;
    public final static long SEED = 42;
    public final static int BOUND = 1000;

    public static void main(String[] args) {
        var arr = new SomeClass[N];
        var rand = new Random(SEED);

        for (int i = 0; i < N; i++) {
            arr[i] = new SomeClass("entry number " + String.valueOf(i), rand.nextInt(BOUND));
        }

        SomeClass.sort(arr);

        for (int i = 0; i < N; i++) {
            System.out.println(arr[i]);
        }
    }
}
