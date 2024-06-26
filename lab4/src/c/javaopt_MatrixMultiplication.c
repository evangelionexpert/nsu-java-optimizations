#include "javaopt_MatrixMultiplication.h"

// https://www.iitk.ac.in/esc101/05Aug/tutorial/native1.1/implementing/array.html
// https://docs.oracle.com/javase/6/docs/technotes/guides/jni/spec/functions.html#GetPrimitiveArrayCritical

#ifndef MAX_CRITICAL
    #error "pls define MAX_CRITICAL"
#endif

#include <stdlib.h>
#include <signal.h>
#include <string.h>
#include <alloca.h>
#include <assert.h>

inline static void
mult(jint *a, jint *b, jint *res, jint m, jint n, jint k) {
    for (jint i = 0; i < m; i++) {
        for (jint j = 0; j < n; j++) {
            jint sum = 0;
            for (jint p = 0; p < k; p++) {
                sum += a[i * k + p] * b[p * n + j];
            }

            res[i * n + j] = sum;
        }
    }
}

JNIEXPORT void JNICALL 
Java_javaopt_MatrixMultiplication_nativeVariant(
    JNIEnv *env, jclass, 
    jintArray a_arr, jintArray b_arr, jintArray res_arr, 
    jint m, jint n, jint k) 
{

    if (m <= MAX_CRITICAL && n <= MAX_CRITICAL && k <= MAX_CRITICAL) { // get...critical блокирует смещения указателей и gc, так что можно и скопировать
        jint *a = (*env)->GetPrimitiveArrayCritical(env, a_arr, 0);
        jint *b = (*env)->GetPrimitiveArrayCritical(env, b_arr, 0);
        jint *res = (*env)->GetPrimitiveArrayCritical(env, res_arr, 0);

        mult(a, b, res, m, n, k);

        (*env)->ReleasePrimitiveArrayCritical(env, a_arr, a, 0);
        (*env)->ReleasePrimitiveArrayCritical(env, b_arr, b, 0);
        (*env)->ReleasePrimitiveArrayCritical(env, res_arr, res, 0);
    } else {
        // jint *arr = calloc(m * n + n * k + m * k, sizeof(jint));
        jint *arr = alloca((m * n + n * k + m * k) * sizeof(jint));
        assert(arr != NULL);
        memset(arr, 0, (m * n + n * k + m * k) * sizeof(jint));
    
        jint *a = arr;
        jint *b = a + (m * k);
        jint *res = b + (n * k);

        (*env)->GetIntArrayRegion(env, a_arr, 0, m * k, a);
        (*env)->GetIntArrayRegion(env, b_arr, 0, n * k, b);

        mult(a, b, res, m, n, k);

        (*env)->SetIntArrayRegion(env, res_arr, 0, m * n, res);

        // free(arr);
    }
}
