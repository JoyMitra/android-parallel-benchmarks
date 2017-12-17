/*
 Created by Joydeep Mitra on 12/5/17.

Problem : Given a sequence of numbers X1,X2,X3, ..., Xn,
 compute the products X1+X2+X3+...+Xn for all k, such that 1<=k<=n
*/

#include <omp.h>
#include <android/log.h>
#include <math.h>

#define LOG_TAG "parPrime"
#define N 1008


void Java_edu_cs_p750_prime_MyNative_parprime(){
    int i,j,count;
    double start, delta;
    int input[N];
    int output[N];

    for(i=0;i<N;i++){
        input[i] = i+1;
    }

    omp_set_num_threads(6);
    start = omp_get_wtime();
#pragma omp parallel for
    for(i=0;i<N;i++){
        int flag = 0;
        int n = input[i];
        for(j=2;j<n;j++){
            if(n%j == 0){
                flag = 1;
                break;
            }
        }
        output[i] = flag;
    }
    delta = omp_get_wtime() - start;
    count = 0;
    for(i=0;i<N;i++){
        if(output[i] == 0){
            count++;
        }
    }

    __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,"Parallel Prime computed in %.4f seconds\n",delta);
    __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,"No. of primes = %d\n",count);




}