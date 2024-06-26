all: compile-java compile-so

compile-java:
	mkdir -p bin
	javac src/java/*.java -d bin/ -h src/c/
compile-so: compile-java
	mkdir -p bin/c
	gcc -I"$(JAVA_HOME)/include/" -I"$(JAVA_HOME)/include/linux/" -shared -fPIC -Ofast src/c/javaopt_MatrixMultiplication.c -DMAX_CRITICAL=128 -o bin/c/libnativemmul.so
run: compile-java compile-so
	java -Xss16M -XX:+PrintGCDetails -cp bin/ -Djava.library.path="bin/c/" javaopt.Main 2>&1 | tee bench.txt
