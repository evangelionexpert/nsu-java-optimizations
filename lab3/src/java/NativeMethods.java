package javaopt;

import java.lang.Thread;
import java.util.Arrays;

public class NativeMethods {
    // Метод который жрет память внутри C и роняет все
    public native void dieStackOverflow();

    // Метод который аллоцирует 1 Кб памяти. Потом показать, как это видно из методов Runtime.xxxMemory()
    public native void mallocNBytes(int n);

    // Метод, который внутри C вызывает друг за другом кучу других методов и где-то внутри ломается.
    public native void dieFirstMethod();
    public native void dieNestedMethod();

    // Показать stack trace, рассказать как найти место поломки в коде
    public native void showStackTrace();

    // Получить строку String и вернуть длину строки
    public native int strlen(String s);

    // Получить объект и вызвать у него метод
    public native void printBeanNum(Bean bean);

    // Получить объект и поменять значение его java-поля
    public native void changeBeanNum(Bean bean, int num);


    // Сделать три метода для работы с нативными структурами данных:
    // Аллоцировать какую-то свою структуру, вернуть указатель
    public native long secretStructInit(int num);

    // Получить указатель на структуру, вернуть значение изнутри структуры
    public native int secretStructValue(long ptr);

    // Получить указатель и освободить память
    public native void secretStructFree(long ptr);

    public void printJvmStackTrace() {
        Arrays.stream(Thread.currentThread().getStackTrace())
            .forEachOrdered(System.err::println);
    } 
}
