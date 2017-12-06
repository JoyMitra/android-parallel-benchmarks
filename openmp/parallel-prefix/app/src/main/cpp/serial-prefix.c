/*
 Created by Joydeep Mitra on 12/5/17.

Problem : Given a sequence of numbers X1,X2,X3, ..., Xn,
 compute the products X1+X2+X3+...+Xn for all k, such that 1<=k<=n
*/

#include <omp.h>
#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>
#include <time.h>

#define LOG_TAG "ser-prefix"
#define N 10000

void Java_edu_cs_p750_parallelPrefix_MyNative_serialPrefix(){
    int i,k;
    long sequence[N];
    double start, delta;
    srand((unsigned int)time(NULL));
    /*
     * Initialize sequence with random numbers between 0 and 50
     */
    for(i=0;i<N;i++){
        sequence[i] = (long)(rand() % 50);
    }

    start = omp_get_wtime();
    for(k=0;k<N;k++){
        long sum = 0;
        for(i=0;i<=k;i++){
            sum = sum + sequence[i];
        }
        __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "Sum for k %d = %ld...\n", k,sum);
    }
    delta = omp_get_wtime() - start;
    __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,"Result computed in %.4f seconds\n",delta);
}


