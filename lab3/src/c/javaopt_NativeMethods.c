#include "javaopt_NativeMethods.h"

#include <stdio.h>
#include <assert.h>
#include <stdlib.h>
#include <stdint.h>
#include <execinfo.h>

#define LOG(...) \
    fprintf(stderr, __VA_ARGS__); \
    fflush(stderr);

void
print_stack_trace(void) {
    void *array[10];

    int size = backtrace(array, 10);
    char **strings = backtrace_symbols(array, size);

    if (strings != NULL) {
        LOG("Obtained %d stack frames.\n", size);
        for (int i = 0; i < size; i++) {
            LOG("%s\n", strings[i]);
        }

        free(strings);
    }
}

#define DIE_SIZE 16384

static void 
die_stack(int sum) {
    uint8_t arr[DIE_SIZE];
    LOG("sp %p, stack size (approx) ~ %dK\n", arr, sum/1024); 
    // будем считать, что адрес возврата/аргумент/регистры
    // расходуют пренебрежимо мало памяти

    // макс размер стека можно настроить -Xss

    // стек частично жрет джава еще до обращения к jni
    // https://stackoverflow.com/questions/66093387/live-monitoring-of-total-thread-stack-size-in-java

    die_stack(sum + DIE_SIZE);
}

JNIEXPORT void JNICALL 
Java_javaopt_NativeMethods_dieStackOverflow(JNIEnv *env, jobject obj) {
    die_stack(0);
}

/*
 * Class:     javaopt_NativeMethods
 * Method:    mallocNBytes
 * Signature: (I)V
 */
JNIEXPORT void JNICALL 
Java_javaopt_NativeMethods_mallocNBytes(JNIEnv *, jobject, jint nBytes) {
    void *allocatedPtr = malloc(nBytes);
    assert(allocatedPtr != NULL);
}

/*
 * Class:     javaopt_NativeMethods
 * Method:    dieFirstMethod
 * Signature: ()V
 */
JNIEXPORT void JNICALL 
Java_javaopt_NativeMethods_dieFirstMethod(JNIEnv *, jobject) {
    print_stack_trace();
    assert(0);
}

void 
fun2() {
    print_stack_trace();
    assert(0);
}

void 
fun1() {
    fun2();
}

/*
 * Class:     javaopt_NativeMethods
 * Method:    dieNestedMethod
 * Signature: ()V
 */
JNIEXPORT void JNICALL 
Java_javaopt_NativeMethods_dieNestedMethod(JNIEnv *, jobject) {
    fun1();
}

/*
 * Class:     javaopt_NativeMethods
 * Method:    showStackTrace
 * Signature: ()V
 */
JNIEXPORT void JNICALL 
Java_javaopt_NativeMethods_showStackTrace(JNIEnv *env, jobject obj) {
    LOG("\nc:\n")
    print_stack_trace();

    LOG("\njava:\n")
    jclass bean_clazz = (*env)->GetObjectClass(env, obj);
    jmethodID method_id = (*env)->GetMethodID(env, bean_clazz, "printJvmStackTrace", "()V");
    (*env)->CallVoidMethod(env, obj, method_id);
}

/*
 * Class:     javaopt_NativeMethods
 * Method:    strlen
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL 
Java_javaopt_NativeMethods_strlen(JNIEnv *env, jobject, jstring str) {
    return (*env)->GetStringUTFLength(env, str);
}

/*
 * Class:     javaopt_NativeMethods
 * Method:    printBeanNum
 * Signature: (Ljavaopt/Bean;)V
 */
JNIEXPORT void JNICALL 
Java_javaopt_NativeMethods_printBeanNum(JNIEnv *env, jobject, jobject bean) {
    jclass bean_clazz = (*env)->GetObjectClass(env, bean);
    jmethodID method_id = (*env)->GetMethodID(env, bean_clazz, "getNum", "()I");
    jint call_res = (*env)->CallIntMethod(env, bean, method_id);
    LOG("C: getNum() returns %d\n", call_res);
}

/*
 * Class:     javaopt_NativeMethods
 * Method:    changeBeanNum
 * Signature: (Ljavaopt/Bean;I)V
 */
JNIEXPORT void JNICALL 
Java_javaopt_NativeMethods_changeBeanNum(JNIEnv *env, jobject, jobject bean, jint num) {
    jclass bean_clazz = (*env)->GetObjectClass(env, bean);
    jfieldID field_id = (*env)->GetFieldID(env, bean_clazz, "num", "I");
    (*env)->SetIntField(env, bean, field_id, num);
}


typedef struct Secret {
    int num;
} Secret;

/*
 * Class:     javaopt_NativeMethods
 * Method:    secretStructInit
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL 
Java_javaopt_NativeMethods_secretStructInit(JNIEnv *, jobject, jint num) {
    Secret *ptr = malloc(sizeof(Secret));
    ptr->num = num;
    return (jlong) ptr;
}

/*
 * Class:     javaopt_NativeMethods
 * Method:    secretStructValue
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL 
Java_javaopt_NativeMethods_secretStructValue(JNIEnv *, jobject, jlong ptr) {
    assert(ptr != 0);
    return ((Secret *) ptr)->num;
}

/*
 * Class:     javaopt_NativeMethods
 * Method:    secretStructFree
 * Signature: (J)V
 */
JNIEXPORT void JNICALL 
Java_javaopt_NativeMethods_secretStructFree(JNIEnv *, jobject, jlong ptr) {
    free((void *) ptr);
    LOG("freed")
}

#undef LOG
