package edu.cs.p750.findMax;

import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int inputArray[] = new int[10000];//{9,8,7,6,5,4,3,2};
        Random r = new Random();
        for(int i=0;i<inputArray.length;i++){
            inputArray[i] = r.nextInt(1024);
        }
        getMax_rs(inputArray);
        getMax_cpu(inputArray);
    }

    private void getMax_rs(int[] inputArray){
        RenderScript rs = RenderScript.create(this);

        int outputArray[] = null;

        long startTime = System.currentTimeMillis();
        while(inputArray.length > 1){
            int lastElem = 0;
            boolean odd = false;
            Allocation inputAllocation = null;
            if(inputArray.length % 2 != 0){
                odd = true;
                lastElem = inputArray[inputArray.length-1];
                inputAllocation = Allocation.createSized(rs, Element.I32(rs),inputArray.length-1);
                //inputArray = new int[inputArray.length-1];
            }
            else{
                inputAllocation = Allocation.createSized(rs, Element.I32(rs),inputArray.length);
            }
            inputAllocation.copyFrom(inputArray);

            Allocation outputAllocation = Allocation.createSized(rs, Element.I32(rs), (inputArray.length)/2);

            ScriptC_mono myScript = new ScriptC_mono(rs);
            myScript.bind_in_arr(inputAllocation);
            myScript.bind_out_arr(outputAllocation);
            myScript.forEach_max2(inputAllocation);

            outputArray = new int[(inputArray.length)/2];
            outputAllocation.copyTo(outputArray);
            if(odd){
                inputArray = new int[(outputArray.length)+1];
                //outputAllocation.copyTo(inputArray);
                outputAllocation.copy1DRangeTo(0,(outputArray.length),inputArray);
                inputArray[inputArray.length-1] = lastElem;
            }
            else{
                inputArray = new int[(outputArray.length)];
                outputAllocation.copyTo(inputArray);
            }

        }
        long endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime);

        Log.d("MAX","Time to compute max in RS : " + elapsedTime + "ms");

        String debugString = "Output_RS : ";
        if(outputArray != null) {
            for (int i = 0; i < outputArray.length; i++) {
                debugString = debugString + String.valueOf(outputArray[i]);
            }
        }

        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(debugString);
    }

    private void getMax_cpu(int[] inputArray){
        int min = Integer.MIN_VALUE;
        long startTime = System.currentTimeMillis();
        for(int i=0;i<inputArray.length;i++){
            if(inputArray[i] > min){
                min = inputArray[i];
            }
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = (endTime - startTime);
        Log.d("MAX","Time to compute max in RS : " + elapsedTime + "ms");
        String res = "Output_CPU : " + String.valueOf(min);
        TextView textView = (TextView) findViewById(R.id.cpuResult);
        textView.setText(res);
    }
}
