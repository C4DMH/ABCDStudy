package gwicks.com.abcdstudy;

import android.app.Application;
import android.util.Log;

public class AnyApplication extends Application {

    //private static Context context;
    private static final String TAG = "AnyApplication";
    private static AnyApplication instance;

    //private String secureID;

    public void onCreate(){
        Log.d(TAG, "onCreate: anyapplication oncreate");

        super.onCreate();

//        secureID = Settings.Secure.getString(
//                AnyApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        //context = this ;
        instance = this;
        Log.d(TAG, "onCreate: instance = " + instance);
    }

    public static AnyApplication getInstance() {
        Log.d(TAG, "getInstance: getting instance");
        Log.d(TAG, "getInstance: instance = " + instance);
        return instance;
    }

//    public String getSecureID(){
//        return secureID;
//    }
}
