package edu.cs.p750.parPrefixRS;

import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptC;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Random;

import edu.cs.p750.parPrefixRS.ScriptC_mono;

public class MainActivity extends AppCompatActivity {

    private static int N = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int sequence[] = new int[N];//{9,8,7,6,5,4,3,2};
        int kvalues[] = new int[N];
        Random r = new Random();
        for(int i=0;i<sequence.length;i++){
            sequence[i] = r.nextInt()%20;
        }

        for(int i=0;i<kvalues.length;i++){
            kvalues[i] = i;
        }
        getpar_prefix_rs(sequence,kvalues);
        getpar_prefix_cpu(sequence);
    }

    private void getpar_prefix_rs(int[] inputArray, int[] kvalues){
        RenderScript rs = RenderScript.create(this);

        int outputArray[] = new int[N];

        Allocation inputAllocation = Allocation.createSized(rs, Element.I32(rs),inputArray.length);
        inputAllocation.copyFrom(inputArray);
        Allocation kAllocation = Allocation.createSized(rs, Element.I32(rs),kvalues.length);
        kAllocation.copyFrom(kvalues);
        Allocation outputAllocation = Allocation.createSized(rs, Element.I32(rs),N);
        ScriptC_mono myScript = new ScriptC_mono(rs);
        myScript.bind_sequence(inputAllocation);
        myScript.bind_output(outputAllocation);
        long startTime = System.currentTimeMillis();
        myScript.forEach_parPrefix(kAllocation);
        long endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime);
        outputAllocation.copyTo(outputArray);

        Log.d("MAX","Time to compute max in RS : " + elapsedTime + "ms");

        String debugString = "Output_RS : ";
        if(outputArray != null) {
            for (int i = 0; i < outputArray.length; i++) {
                debugString = debugString + String.valueOf(outputArray[i])+ ",";
            }
        }

        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(debugString);
    }

    private void getpar_prefix_cpu(int[] inputArray){
        long startTime = System.currentTimeMillis();
        String debugString = "Output_CPU : ";
        for(int i=0;i<N;i++){
            int sum = 0;
            for(int j=0;j<=i;j++){
                sum = sum + inputArray[j];
            }
            debugString = debugString + String.valueOf(sum)+ ",";
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = (endTime - startTime);
        Log.d("MAX","Time to compute max in CPU : " + elapsedTime + "ms");
        TextView textView = (TextView) findViewById(R.id.cpuResult);
        textView.setText(debugString);
    }
}
