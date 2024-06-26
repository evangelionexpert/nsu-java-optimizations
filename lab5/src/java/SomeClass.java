package javaopt;

public class SomeClass {
    private final String name;
    private final int value;

    public SomeClass(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public static void sort(SomeClass[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            var tmp = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j].value > tmp.value) {
                j--;
            }

            System.arraycopy(arr, j + 1, arr, j + 2, i - j - 1);
            arr[j + 1] = tmp;
        }
    }

    public String toString() {
        return "{name: " + name + ", value: " + value + "}";
    }
}
