package javaopt;

import java.lang.Runtime;

public class Main {
    static {
        System.loadLibrary("natives");
    }

    static void println(Object o) {
        System.err.println(o);
    }

    public static void main(String[] args) {
        var methods = new NativeMethods();
        
        // println("\n~~~ dieStackOverflow()"); 
        // methods.dieStackOverflow();

        println("\n~~~ mallocNBytes(4096)"); 
        println("freeMemory() before = " + Runtime.getRuntime().freeMemory()); 
        println("totalMemory() before = " + Runtime.getRuntime().totalMemory()); 
        println("maxMemory() before = " + Runtime.getRuntime().maxMemory()); 
        methods.mallocNBytes(4096); 
        // видимо выделяется в куче jvm-ного процесса, отдельно от jvm-ного куска
        println("freeMemory() after = " + Runtime.getRuntime().freeMemory());
        println("totalMemory() after = " + Runtime.getRuntime().totalMemory());
        println("maxMemory() after = " + Runtime.getRuntime().maxMemory());

        //  println("\n~~~ dieFirstMethod()"); 
        // methods.dieFirstMethod();

        println("\n~~~ dieNestedMethod()"); 
        methods.dieNestedMethod();

        println("\n~~~ showStackTrace()"); 
        methods.showStackTrace();

        println("\n~~~ strlen(\"hello world\")"); 
        println("Result is " + methods.strlen("hello world"));

        println("\n~~~ printBeanNum(Bean(100500))"); 
        var bean = new Bean(100500);
        methods.printBeanNum(bean);

        println("\n~~~ changeBeanNum(100500 -> -1)"); 
        println("before: " + bean.getNum());
        methods.changeBeanNum(bean, -1);
        println("after: " + bean.getNum());

        println("\n~~~ secretStructInit(42)"); 
        long ptr = methods.secretStructInit(42);

        println("\n~~~ secretStructValue()"); 
        println("Result is " + methods.secretStructValue(ptr));

        println("\n~~~ secretStructFree()"); 
        methods.secretStructFree(ptr);

        println("\n~~~ secretStructValue() use-after-free"); 
        println("Result is "+ methods.secretStructValue(ptr));
        println("\n\nGoodbye");
    }
}
