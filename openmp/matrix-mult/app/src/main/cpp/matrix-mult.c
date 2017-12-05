//
// Created by Joydeep Mitra on 11/29/17.
//

#include <omp.h>
#include <stdio.h>
#include <android/log.h>

#define LOG_TAG "matrix-mult"

#define NRA 256                 /* number of rows in matrix A */
#define NCA 256                 /* number of columns in matrix A */
#define NCB 256                  /* number of columns in matrix B */

void Java_edu_example_anative_myfirstnativeapp_MyNative_matrixMult()
{
    int	tid, nthreads, i, j, k, chunk;
    int	a[NRA][NCA],           /* matrix A to be multiplied */
            b[NCA][NCB],           /* matrix B to be multiplied */
            c[NRA][NCB];           /* result matrix C */

    chunk = 10;                    /* set loop iteration chunk size */
    double start, delta;

    start = omp_get_wtime();
/*** Spawn a parallel region explicitly scoping all variables ***/
#pragma omp parallel shared(a,b,c,nthreads,chunk) private(tid,i,j,k)
    {
        tid = omp_get_thread_num();
        if (tid == 0)
        {
            nthreads = omp_get_num_threads();
            __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "Starting matrix multiple example with %d threads\n", nthreads);
            //__android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "Max no of threads %d threads\n", omp_get_max_threads());
            //printf("Starting matrix multiple example with %d threads\n",nthreads);
            //printf("Initializing matrices...\n");
        }
        /*** Initialize matrices ***/
#pragma omp for schedule (static, chunk)
        for (i=0; i<NRA; i++)
            for (j=0; j<NCA; j++)
                a[i][j]= i+j;
#pragma omp for schedule (static, chunk)
        for (i=0; i<NCA; i++)
            for (j=0; j<NCB; j++)
                b[i][j]= i*j;
#pragma omp for schedule (static, chunk)
        for (i=0; i<NRA; i++)
            for (j=0; j<NCB; j++)
                c[i][j]= 0;

        /*** Do matrix multiply sharing iterations on outer loop ***/
        /*** Display who does which iterations for demonstration purposes ***/
        __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "Thread %d starting matrix multiply...\n", tid);
        //printf("Thread %d starting matrix multiply...\n",tid);
#pragma omp for schedule (static, chunk)
        for (i=0; i<NRA; i++)
        {
            printf("Thread=%d did row=%d\n",tid,i);
            for(j=0; j<NCB; j++)
                for (k=0; k<NCA; k++)
                    c[i][j] += a[i][k] * b[k][j];
        }
    }   /*** End of parallel region ***/

    delta = omp_get_wtime() - start;

/*** Print results ***/
    printf("******************************************************\n");
    printf("Result Matrix:\n");
    for (i=0; i<NRA; i++)
    {
        for (j=0; j<NCB; j++)
            printf("%6.2f   ", c[i][j]);
        printf("\n");
    }
    printf("******************************************************\n");
    printf ("Done.\n");

    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "Matrix mult took %.4g seconds\n", delta);

}