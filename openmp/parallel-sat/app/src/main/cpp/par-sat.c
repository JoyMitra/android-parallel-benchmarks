/*
 Created by Joydeep Mitra on 12/5/17.

Problem : Given a sequence of numbers X1,X2,X3, ..., Xn,
 compute the products X1+X2+X3+...+Xn for all k, such that 1<=k<=n
*/

#include <omp.h>
#include <android/log.h>
#include <math.h>

#define LOG_TAG "parSat"
#define N 8


void Java_edu_cs_p750_parallelSat_MyNative_parsat(){
    int i,j,k;
    int rows = ((int) pow(2,N));
    int size = rows*N;
    int input[2048];
    int output[256];
    int binary[N];
    double start, delta;

    for(i=0;i<N;i++){
        binary[i] = 0;
    }

    /*
        generating all possible inputs
         */
    k=0;
    for(i=0;i<size;i=i+N){
        int b = N-1;
        int n = k;
        while(n != 0){
            int r = n%2;
            n = n/2;
            binary[b] = r;
            b--;
        }
        for(j=0;j<N;j++){
            input[i+j] = binary[j];
        }
        k++;
    }

    start = omp_get_wtime();
#pragma omp parallel for private(i,j)
    for(i=0;i<size;i=i+N){
        int issat = 1;
        __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,"Thread %d executing ... ", omp_get_thread_num());
        for(j=i;j<i+N;j++){
            issat = issat && input[j];
        }
        output[i/N] = issat;
    }
    delta = omp_get_wtime() - start;
    __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,"Parallel SAT computed in %.4f seconds\n",delta);
    __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,"Output-------");
    for(k=0;k<rows;k++){
        __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,"%d",output[k]);
    }



}