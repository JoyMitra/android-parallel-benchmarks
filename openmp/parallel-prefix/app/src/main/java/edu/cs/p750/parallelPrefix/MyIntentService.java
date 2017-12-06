package edu.cs.p750.parallelPrefix;

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
                case "ser-prefix":
                    new MyNative().serialPrefix();
                    break;
                case "par-prefix":
                    new MyNative().parPrefix();
                    break;
                default:
                    break;
            }
        }
    }
}
