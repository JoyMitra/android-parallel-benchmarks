//
// Created by Joydeep Mitra on 12/1/17.
//

#include <omp.h>
#include <stdio.h>
#include <android/log.h>

#define LOG_TAG "FindMax"
#define SIZE 1000

void Java_edu_example_anative_myfirstnativeapp_MyNative_findMax(){
    int   i;
    int a[SIZE], maxm;
    double start, delta;

    /* Some initializations */
    //n = 100;
    maxm = 0;
    for (i=0; i < SIZE; i++) {
        a[i] = i;
    }
    omp_set_num_threads(3);
    start = omp_get_wtime();

#pragma omp parallel for      \
   default(shared) private(i)  \
   reduction(max:maxm)
    for (i=0; i < SIZE; i++){
        if(a[i] > maxm){
            maxm = a[i];
        }
    }
    delta = omp_get_wtime() - start;
    __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,"Maximum = %d computed in %.4f seconds\n",maxm,delta);
    printf("Final result= %d\n",maxm);
}
