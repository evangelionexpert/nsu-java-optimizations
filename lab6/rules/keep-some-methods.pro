-keep class javaopt.Main
-keepclassmembers public class javaopt.Main {
    public static void main(java.lang.String[]);
}
-keep class javaopt.EmptyOne
-keepclassmembers class javaopt.EmptyOne {
    public void one();
    public void one(int);
}
-keepclassmembers class javaopt.SomeClass {
    public java.lang.String toString();
}
