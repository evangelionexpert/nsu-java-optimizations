all: clean compile run-main run-javap run-recaf run-javap run-main 
compile:
	mkdir -p bin
	javac src/java/*.java -d bin/ 
run-main: 
	java -cp bin/ javaopt.Main 
run-javap: 
	mkdir -p decompiled
	javap -c -v -cp bin/ javaopt.Main > decompiled/Main.bytecode
	javap -c -v -cp bin/ javaopt.Util > decompiled/Util.bytecode
	javap -c -v -cp bin/ javaopt.SomeClass > decompiled/SomeClass.bytecode
run-recaf: 
	recaf --input=bin/javaopt/SomeClass.class
clean:
	rm -rf bin/
	rm -rf decompiled/
