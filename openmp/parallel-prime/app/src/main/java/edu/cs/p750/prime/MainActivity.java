package edu.cs.p750.prime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "Parallel-SATOpenMp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume(){
        super.onResume();
        final Button btn = (Button) findViewById(R.id.parSat);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(getApplicationContext(),MyIntentService.class).putExtra("method","par-prime"));
                Toast.makeText(getApplicationContext(),"Computing sat in the background ... Check Log",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
