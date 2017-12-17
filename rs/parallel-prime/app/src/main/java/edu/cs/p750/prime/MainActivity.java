/*
Find the no. of prime numbers between 1 and N.
 */

package edu.cs.p750.prime;

import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private static int N = 100000;
    private static String TAG = "Parallel-Prime";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();
        int count = 0;
        int[] input = new int[N];
        int[] output = new int[N];

        for(int i=0;i<input.length;i++){
            input[i] = i+1;
        }

        RenderScript mRS = RenderScript.create(this);
        Allocation inputAllocation = Allocation.createSized(mRS,Element.I32(mRS),N);
        inputAllocation.copyFrom(input);
        Allocation outputAllocation = Allocation.createSized(mRS,Element.I32(mRS),N);
        ScriptC_mono mScript = new ScriptC_mono(mRS);
        long startTime = System.currentTimeMillis();
        mScript.forEach_isPrime(inputAllocation,outputAllocation);
        outputAllocation.copyTo(output);
        long endTime = System.currentTimeMillis();

        for(int i=0;i<N;i++){
            if(output[i] == 0){
                count++;
            }
        }
        float elapsedTime = ((endTime - startTime)/1000.10f);
        Log.d(TAG, "Time to compute parallel prime with rs = " + elapsedTime);
        Log.d(TAG,"Prime nos. between 1 and "+ N + " = " + count);

        /*
        serial version
         */

        count = 0;
        startTime = System.currentTimeMillis();
        for(int i=0;i<input.length;i++){
            int flag = 0;
            for(int j=2;j<input[i];j++){
                if(input[i]%j == 0){
                    flag = 1;
                    break;
                }
            }
            if(flag == 0) count++;
        }
        endTime = System.currentTimeMillis();
        elapsedTime = ((endTime - startTime)/1000.10f);
        Log.d(TAG, "Time to compute parallel prime with CPU = " + elapsedTime);
        Log.d(TAG,"CPU : Prime nos. between 1 and "+ N + " = " + count);


    }


}
