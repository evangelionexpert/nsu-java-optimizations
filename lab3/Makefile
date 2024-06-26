all: compile-java compile-so

compile-java:
	mkdir -p bin
	javac src/java/*.java -d bin/ -h src/c/
compile-so: compile-java
	mkdir -p bin/c
	gcc -I"$(JAVA_HOME)/include/" -I"$(JAVA_HOME)/include/linux/" -shared -fPIC src/c/javaopt_NativeMethods.c -o bin/c/libnatives.so
run: compile-java compile-so
	java -cp bin/ -Djava.library.path="bin/c/" -Xss4M javaopt.Main
