all: 
compile:
	mkdir -p bin
	javac src/java/*.java -d bin/ 
	echo "Main-Class: javaopt.Main" > bin/manifest.mf
	jar cvmf bin/manifest.mf bin/gagaga.jar -C bin javaopt
run-main: 
	java -jar bin/gagaga.jar
run-proguard:
	@for rule in rules/*.pro; do \
		echo "Processing $$rule..."; \
        proguard @$${rule}  -dontwarn  -injars bin/gagaga.jar -outjars bin/$$(basename $$rule .pro).jar; \
		java -jar bin/$$(basename $$rule .pro).jar; \
    done
clean:
	rm -rf bin/
	rm -rf decompiled/
