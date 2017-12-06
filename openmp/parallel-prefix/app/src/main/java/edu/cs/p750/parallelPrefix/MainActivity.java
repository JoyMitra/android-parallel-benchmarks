package edu.cs.p750.parallelPrefix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button getPiBtn = (Button) findViewById(R.id.serPrefix);
        final Button matrixMulBtn = (Button) findViewById(R.id.parPrefix);

        getPiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyIntentService.class);
                intent.putExtra("method","ser-prefix");
                startService(intent);
                Toast.makeText(getApplicationContext(),"serial-prefix in a separate worker thread ... ",Toast.LENGTH_LONG).show();
            }
        });

        matrixMulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyIntentService.class);
                intent.putExtra("method","par-prefix");
                startService(intent);
                Toast.makeText(getApplicationContext(),"par-prefix in a separate worker thread ... ",Toast.LENGTH_LONG).show();
            }
        });

        // Example of a call to a native method
        /*double pi = calcPipar();
        Double dpi = pi;
        //matrixMult();
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(Double.toString(dpi));*/

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();

    //public native double calcPi();

    //public native double calcPipar();

    //public native void matrixMult();
}
