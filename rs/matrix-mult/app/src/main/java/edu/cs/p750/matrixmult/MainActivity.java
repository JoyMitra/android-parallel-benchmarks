package edu.cs.p750.matrixmult;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.renderscript.Element;
import android.renderscript.Int2;
import android.renderscript.RenderScript;
import android.renderscript.Allocation;
import android.util.Log;
import android.widget.ImageView;
import android.renderscript.*;

public class MainActivity extends AppCompatActivity {

    private RenderScript mRS;
    private Allocation allocationA; // allocation of A matrix
    private Allocation allocationB; // allocation of B matrix
    private Allocation allocationC; // allocation of C matrix
    private Allocation allocationKSize;; // allocation of Row Size of Matrix A;
    private Allocation allocationNSize; // allocation of Col Size of Matrix B;
    private Allocation allocationPosRow;

    private ScriptC_mono mScript;

    private float[] matA;
    private float[] matB;
    private float[] outMatrix;
    private int[] pos_row;
    private int[] nSize;
    private int[] kSize;
    private Random rand = new Random();

    private float[][] cpuMatA;
    private float[][] cpuMatB;
    private float[][] cpuMatC;
    private float elapsedTime;

    protected int A_m = 32;
    protected int A_k = 32;
    protected int B_n = 32;

    public float[] random_matrix(int m, int n)
    {
        int size = m*n;
        float[] r_matrix = new float[size];
        for(int i=0; i < size; i++) { r_matrix[i] = rand.nextFloat(); }
        return r_matrix;
    }

    public float[] init_matrix(int m, int n)
    {
        int size = m*n;
        float[] r_matrix = new float[size];
        return r_matrix;
    }

    public int[] generate_row_idx(int m)
    {
        int[] r_idx = new int[m];
        for(int i=0; i<m; i++) { r_idx[i] = i; }
        return r_idx;
    }

    public float[][] copyMatrix(float[] mat,int m, int n)
    {
        float[][] r_mat = new float[m][n];
        for(int i=0; i<m*n; i++)
        {
            int row_num = i/n;
            int col_num = i-n*row_num;
            r_mat[row_num][col_num] = mat[i];
        }
        return r_mat;
    }

    public float[][] CPU_matrix_multiplication()
    {
        float[][] matC = new float[A_m][B_n];
        cpuMatA = copyMatrix(matA,A_m,A_k);
        cpuMatB = copyMatrix(matB,A_k,B_n);
        for(int i=0; i<A_m; i++){
            for(int j=0; j<B_n; j++){
                for(int k=0; k<A_k; k++){
                    matC[i][j] += cpuMatA[i][k] * cpuMatB[k][j];
                }
            }
        }
        return matC;
    }

    public float error_between(float[] m, float[][] n)
    {
        float result = 0;
        float size = (float) (A_m * B_n);

        for(int i=0; i<A_m; i++){
            for(int j=0; j<B_n; j++){
                result += (n[i][j] - m[B_n*i+j]);
            }
        }

        result /= size;

        //Log.d("MatrixMult", "Error result between rs and CPU computation = " + result);

        return result;
    }

    void initMatrix()
    {
        matA = random_matrix(A_m, A_k);
        matB = random_matrix(A_k, B_n);
        outMatrix = init_matrix(A_m, B_n);
        pos_row = generate_row_idx(A_m);

        kSize = new int[1];
        nSize = new int[1];

        kSize[0] = A_k;
        nSize[0] = B_n;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMatrix();
        createScript();
    }

    private void createScript() {
        long startTime = System.currentTimeMillis();
        mRS = RenderScript.create(this);

        int sizeA = A_m * A_k;
        int sizeB = A_k * B_n;
        int sizeC = A_m * B_n;
        // memory allocation part
        allocationA = Allocation.createSized(mRS, Element.F32(mRS),sizeA);
        allocationB = Allocation.createSized(mRS, Element.F32(mRS),sizeB);
        allocationC = Allocation.createSized(mRS, Element.F32(mRS),sizeC);
        allocationNSize = Allocation.createSized(mRS, Element.I32(mRS), 1);
        allocationKSize = Allocation.createSized(mRS, Element.I32(mRS), 1);
        allocationPosRow = Allocation.createSized(mRS, Element.I32(mRS), A_m);

        allocationA.copyFrom(matA);
        allocationB.copyFrom(matB);
        allocationPosRow.copyFrom(pos_row);
        allocationNSize.copyFrom(nSize);
        allocationKSize.copyFrom(kSize);


        mScript = new ScriptC_mono(mRS);

        mScript.bind_matA(allocationA);
        mScript.bind_matB(allocationB);
        mScript.bind_outMatrix(allocationC);

        mScript.bind_kSize(allocationKSize);
        mScript.bind_nSize(allocationNSize);

        mScript.forEach_root(allocationPosRow, allocationPosRow);
        allocationC.copyTo(outMatrix);
        long endTime = System.currentTimeMillis();
        elapsedTime = ((endTime - startTime)/1000.10f);

        Log.d("MatrixMult", "Time to compute matrix multiplication with rs = " + elapsedTime);

        long startTime1 = System.currentTimeMillis();

        float[][] CPUMatC = CPU_matrix_multiplication();
        long endTime1 = System.currentTimeMillis();

        Log.d("MatrixMult", "Time to compute matrix multiplication with CPU = " + ((endTime1 - startTime1)/1000.10f));

        float error = error_between(outMatrix, CPUMatC);

        Log.d("MatrixMult", "Error between rs and CPU computation = " + error);



    }
}
