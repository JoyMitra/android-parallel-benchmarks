//
// Created by Joydeep Mitra on 10/23/17.
//
#include <stdio.h>
#include <stdlib.h>
#include <jni.h>

static long steps = 1000;
double step;

jdouble Java_edu_example_anative_myfirstnativeapp_MainActivity_calcPi(){
    int i;
    double x;
    double pi, sum = 0.0;

    step = 1.0/(double) steps;

    for (i=0; i < steps; i++) {
        x = (i+0.5)*step;
        sum += 4.0 / (1.0+x*x);
    }

    pi = step * sum;
    //printf("PI = %.16g computed\n", pi);
    return pi;
}
