#!/usr/bin/bash
set -e -x

gradle jar
java -jar -Xmx200M build/libs/optimizatcia-java.jar &
PID=$!

sleep 2

jmap -dump:file=heap_pid$PID.hprof $PID
jstack $PID > threads_dump_pid$PID.txt

# https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/tooldescr006.html
jcmd $PID
#jcmd $PID VM.system_properties
jcmd $PID VM.flags
#jcmd $PID Thread.print

jconsole $PID

kill $PID

java -jar -Xmx200M -XX:+HeapDumpOnOutOfMemoryError build/libs/optimizatcia-java.jar --vredno &
BAD_PID=$!

sleep 5

kill $BAD_PID

# а как определить
file java_pid$BAD_PID.hprof
