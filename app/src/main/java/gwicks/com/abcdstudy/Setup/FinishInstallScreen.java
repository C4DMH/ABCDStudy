package gwicks.com.abcdstudy.Setup;

import android.app.DialogFragment;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.TimeUnit;

import gwicks.com.abcdstudy.AnyApplication;
import gwicks.com.abcdstudy.R;
import gwicks.com.abcdstudy.StatsJobService;

public class FinishInstallScreen  extends AppCompatActivity {

    private static final String TAG = "FinishInstallScreen";
    public static final String secureID = Settings.Secure.getString(
            AnyApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_base);

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: the activity is being recreated!");
        }

        updateStatusBarColor("#1281e8");

        if(isAlreadySet(this) == false){
            Log.d(TAG, "onCreate: already done email upload skipping");
            launchSendEmailDialog();
        }

        FirebaseMessaging.getInstance().subscribeToTopic(secureID);
        FirebaseMessaging.getInstance().subscribeToTopic("ABCD");

//        FirebaseMessaging.getInstance().subscribeToTopic("PITTS");
//        FirebaseMessaging.getInstance().subscribeToTopic(secureID);
//        FirebaseMessaging.getInstance().subscribeToTopic("UPMC");


        final JobInfo job = new JobInfo.Builder(1, new ComponentName(this, StatsJobService.class))
                //.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                //.setRequiresCharging(true)
                //.setMinimumLatency(10000)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15))
                .setPersisted(true)
                //.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build();
        final JobScheduler jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//
        jobScheduler.schedule(job);
        Log.d(TAG, "onCreate: Job Scehduled");

        setSettingsDone(this);




    }




    public void launchSendEmailDialog(){
        DialogFragment newFragment = new EmailSecureDeviceID();
        newFragment.setCancelable(false);

        newFragment.show(getFragmentManager(), "email");
    }

    public void updateStatusBarColor(String color) {// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "updateStatusBarColor: color change being called!");
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    public static void setSettingsDone(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences("YourPref", 0);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("AlreadySetPref", true);
        editor.commit();
    }

    public static boolean isAlreadySet(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences("YourPref", 0);
        return prefs.getBoolean("AlreadySetPref", false);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }




}
