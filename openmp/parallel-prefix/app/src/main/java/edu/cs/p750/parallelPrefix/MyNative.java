package edu.cs.p750.parallelPrefix;

/**
 * Created by Joy on 12/1/17.
 */

public class MyNative {

    static {
        System.loadLibrary("native-lib");
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();

    //public native double calcPi();

    public native void parPrefix();

    public native void serialPrefix();
}
