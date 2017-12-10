/*
This problem assumes that we are given a logical circuit of AND, OR and NOT gates,
with N binary inputs and a single output.
We are to determine all inputs which produce a 1 as the output.
 */

package edu.cs.p750.satisfy;

import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private static int N = 8;
    private static String TAG = "Parallel-SAT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();
        int SIZE = (int)Math.pow(2,N)*N;
        int[] input = new int[SIZE];
        int[] rowIds = new int[(int)Math.pow(2,N)];
        int[] sat = new int[((int)Math.pow(2,N))];
        int k=0;
        /*
        generating all possible inputs
         */
        for(int i=0;i<input.length;i=i+N){
            int[] tmp = decimalToBinary(k);
            for(int j=0;j<tmp.length;j++){
                input[i+j] = tmp[j];
            }
            k++;
        }
        k=0;
        for(int i=0;i<SIZE;i=i+N){
            rowIds[k] = i;
            k++;
        }

        /*
        initializing output sat formula
         */
        for(int i=0;i<sat.length;i++){
            sat[i] = 5;
        }
        RenderScript mRS = RenderScript.create(this);
        Allocation rowAllocation = Allocation.createSized(mRS, Element.I32(mRS),((int)Math.pow(2,N)));
        rowAllocation.copyFrom(rowIds);
        Allocation inputAllocation = Allocation.createSized(mRS, Element.I32(mRS),SIZE);
        inputAllocation.copyFrom(input);
        Allocation varNumAllocation = Allocation.createSized(mRS, Element.I32(mRS),1);
        varNumAllocation.copyFrom(new int[]{N});
        Allocation satAllocation = Allocation.createSized(mRS, Element.I32(mRS),((int)Math.pow(2,N)));

        long startTime = System.currentTimeMillis();
        ScriptC_mono mScript = new ScriptC_mono(mRS);
        mScript.bind_input(inputAllocation);
        mScript.bind_var_num(varNumAllocation);
        mScript.forEach_satisfy(rowAllocation, satAllocation);
        long endTime = System.currentTimeMillis();
        float elapsedTime = ((endTime - startTime)/1000.10f);
        Log.d(TAG, "Time to compute sat with rs = " + elapsedTime);

        satAllocation.copyTo(sat);

        String satFormula = "Output: ";
        for(int i=0;i<sat.length;i++){
            satFormula = satFormula + String.valueOf(sat[i]) + ",";
        }
        Log.d(TAG,satFormula);


    }

    private int[] decimalToBinary(int n){
        int[] binary = new int[N];
        int i = binary.length-1;
        while(n != 0){
            int r = n%2;
            n = n/2;
            binary[i] = r;
            i--;
        }
        return binary;
    }


}
