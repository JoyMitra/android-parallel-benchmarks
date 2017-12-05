//
// Created by Joydeep Mitra on 10/24/17.
//

#include <omp.h>
#include <jni.h>
#include <android/log.h>

#define LOG_TAG "pi-coll"

static long steps = 10000;
double step;

jdouble Java_edu_example_anative_myfirstnativeapp_MyNative_calcPipar(){
    int i,j;
    int	tid;
    double x;
    double pi, sum = 0.0;
    double start, delta;

    step = 1.0/(double) steps;

    sum = 0.0;

    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "Max Threads %d...\n", omp_get_max_threads());
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "No of Processors %d...\n", omp_get_num_procs());
    //omp_set_num_threads(omp_get_max_threads());
    omp_set_dynamic(0);
    start = omp_get_wtime();
#pragma omp parallel for private(x, tid) reduction(+:sum)
        for (i = 0; i < steps; i++) {
            //__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "Thread id %d...\n", omp_get_thread_num());
            x = (i + 0.5) * step;
            sum += 4.0 / (1.0 + x * x);
        }

    delta = omp_get_wtime() - start;
// Out of the parallel region, finialize computation
    pi = step * sum;
    //printf("PI = %.16g computed in %.4g seconds\n", pi, delta);
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "PI = %.16g computed in %.4g seconds\n", pi, delta);
    return pi;
}
