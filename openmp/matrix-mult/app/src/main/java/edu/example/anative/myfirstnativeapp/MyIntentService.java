package edu.example.anative.myfirstnativeapp;

import android.app.IntentService;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyIntentService extends IntentService {


    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String natMethod = intent.getStringExtra("method");
            switch (natMethod){
                case "pi-coll":
                    new MyNative().calcPipar();
                    break;
                case "matrix-mul":
                    new MyNative().matrixMult();
                    break;
                default:
                    break;
            }
        }
    }
}
