package gwicks.com.abcdstudy.Setup;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import gwicks.com.abcdstudy.AnyApplication;
import gwicks.com.abcdstudy.R;
import gwicks.com.abcdstudy.StatsAlarmReceiver;
import gwicks.com.abcdstudy.StatsJobService;

//import gwicks.com.abcdstudy.PowerConnectionUploadReceiver;

public class FinishInstallScreen  extends AppCompatActivity {

    private static final String TAG = "FinishInstallScreen";
    public static final String secureID = Settings.Secure.getString(
            AnyApplication.getInstance().getContentResolver(), Settings.Secure.ANDROID_ID);

    //private PendingIntent DailyEMAIntent;
    //private PendingIntent startDailyEMAIntent;

    private PendingIntent statsIntent;
    private PendingIntent powerIntent;

    BroadcastReceiver screenStateChange;

    File directory;




    @SuppressLint("SwitchIntDef")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_base);

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: the activity is being recreated!");
        }

//
//        String datePath = this.getExternalFilesDir(null) + "/SCREEN/";
//        directory = new File(datePath);
//        if(!directory.exists()){
//            Log.d(TAG, "onStartJob: making directory");
//            directory.mkdirs();
//        }
//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_SCREEN_ON);
//        filter.addAction(Intent.ACTION_SCREEN_OFF);
//        filter.addAction("android.intent.action.LOCKED_BOOT_COMPLETED");
//
//        screenStateChange = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d(TAG, "onReceive: gettubg somthing");
//                Log.d(TAG, "onReceive:  intent = " + intent.getAction() + ", " + intent.getData() );
//
//                Calendar cal = Calendar.getInstance();
//                long endTime = cal.getTimeInMillis();
//                SimpleDateFormat dateOnly = new SimpleDateFormat("ddMMyyyy");
//                String theDate = dateOnly.format(cal.getTime());
//
//                File location = new File(directory, theDate +".txt");
//
//                writeToFile(location,endTime + "," + intent.getAction() + "\n");
//
//
//
//
//
//
//
//
//            }
//        };
//        registerReceiver(screenStateChange, filter);



//        EnterpriseDeviceManager edm = (EnterpriseDeviceManager) getSystemService
//                (EnterpriseDeviceManager.ENTERPRISE_POLICY_SERVICE);
//        ApplicationPolicy appPolicy = edm.getApplicationPolicy();
//        AppIdentity appIdentity = new AppIdentity("com.abc.xyz", "appSignature");
//        try {
//            boolean result = appPolicy.addPackageToBatteryOptimizationWhiteList(appIdentity);
//        } catch (SecurityException e) {
//            Log.w(TAG, "SecurityException: " + e);
//        }




        updateStatusBarColor("#1281e8");

        if(isAlreadySet(this) == false){
            Log.d(TAG, "onCreate: already done email upload skipping");
            //launchSendEmailDialog();
        }

//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Log.d(TAG, "onCreate: starting battery optimization");
//            Intent intent = new Intent();
//            String packageName = getPackageName();
//            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
//            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                intent.setData(Uri.parse("package:" + packageName));
//                startActivity(intent);
//            }
//        }

        Log.d(TAG, "onCreate: secure device: " + secureID);

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
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                //.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build();
        final JobScheduler jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//
        jobScheduler.schedule(job);
        Log.d(TAG, "onCreate: Job Scehduled");

       // startDailyEMAAlarm();
        //startDailyEMAIntent();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startStatsAlarm();
            }
        }, 10000);


        setSettingsDone(this);

        //startChargingUploadAlarm();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
// Checks if the device is on a metered network
        //connMgr.isActiveNetworkMetered() &&
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//
//            Log.d(TAG, "onCreate: isactivenetworkmetered: " + connMgr.isActiveNetworkMetered());
//            Log.d(TAG, "onCreate: getActiveNetworkInfor: " + connMgr.getActiveNetworkInfo());
//            // Checks user’s Data Saver settings.
//            switch (connMgr.getRestrictBackgroundStatus()) {
//                case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_DISABLED:
//
//                    Log.d(TAG, "backfround status: " + connMgr.getRestrictBackgroundStatus());
//                    Log.d(TAG, "metered network: " + connMgr.isActiveNetworkMetered());
//                    // Background data usage is blocked for this app. Wherever possible,
//                    // the app should also use less data in the foreground.
//                    Log.d(TAG, "restrict background: 1");
//                    Log.d(TAG, "onCreate: RESTRICT_BACKGROUND_STATUS_DISABLED");
//
//                case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_WHITELISTED:
//                    Log.d(TAG, "backfround status: " + connMgr.getRestrictBackgroundStatus());
//                    Log.d(TAG, "restrict background: 2");
//                    Log.d(TAG, "onCreate: RESTRICT_BACKGROUND_STATUS_WHITELISTED ");
//                    // The app is whitelisted. Wherever possible,
//                    // the app should use less data in the foreground and background.
//
//                case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED:
//                    Log.d(TAG, "backfround status: " + connMgr.getRestrictBackgroundStatus());
//                    Log.d(TAG, "restrict background: 3");
//                    Log.d(TAG, "onCreate: RESTRICT_BACKGROUND_STATUS_ENABLED");
//
//                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        Log.d(TAG, "onCreate: starting data whitelist optimization");
//
//                    }
//
//                    // Data Saver is disabled. Since the device is connected to a
//                    // metered network, the app should use less data wherever possible.
//            }
//        } else {
//            Log.d(TAG, "restrict background: 4");
//            // The device is not on a metered network.
//            // Use data as required to perform syncs, downloads, and updates.
//        }




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

//    public void startDailyEMAIntent() {
//        Log.d(TAG, "Daily EMA upload in start alarm");
//
//        Calendar cal = Calendar.getInstance();
//        long when = cal.getTimeInMillis();
//        String timey = Long.toString(when);
//
//        //System.out.println("The time changed into nice format is: " + theTime);
//
//        Log.d("the time is: ", when + " ");
//
//        cal.setTimeInMillis(System.currentTimeMillis());
//        cal.set(Calendar.HOUR_OF_DAY, 23);
//        cal.set(Calendar.MINUTE, 45);
//
//        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, DailyEMAUploadReceiver.class);
//        //statsIntent = PendingIntent.getBroadcast(this, 3, intent, 0);
//        DailyEMAIntent = PendingIntent.getBroadcast(this, 27, intent, 0);
//        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, DailyEMAIntent);
//
//
//    }
//
//    public void startDailyEMAAlarm(){
//        Log.d(TAG, "startDailyEMAAlarm: in start ema alarm");
//
//        boolean alarmUp = (PendingIntent.getBroadcast(this, 22,
//                new Intent(FinishInstallScreen.this, DailyEMAAlarmReceiver.class),
//                PendingIntent.FLAG_NO_CREATE) != null);
//
//        Log.d(TAG, "Daily Ema alarm boolean alarm up is: " + alarmUp);
//
//        if(alarmUp){
//            Log.d(TAG, "startDailyEMAAlarm: alarm already up, skipping");
//            return;
//        }
//
//
//        Calendar cal = Calendar.getInstance();
//        long when = cal.getTimeInMillis();
//
//        cal.setTimeInMillis(System.currentTimeMillis());
//        //cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
//        cal.set(Calendar.HOUR_OF_DAY, 8);
//        cal.set(Calendar.MINUTE, 00);
//
//        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, DailyEMAAlarmReceiver.class);
//        intent.putExtra("EMA", "EMA1");
//        startDailyEMAIntent = PendingIntent.getBroadcast(this, 22, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        //alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),alarmMgr.INTERVAL_DAY * 7 , startEMAIntent);
//        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, startDailyEMAIntent);
//        Log.d(TAG, "Daily");
//        //alarmStarted = true;
//
//
//    }

    public void startStatsAlarm() {
        Log.d(TAG, "startStatsAlarm: in start alarm");

        Calendar cal = Calendar.getInstance();
        long when = cal.getTimeInMillis();
        String timey = Long.toString(when);

        //System.out.println("The time changed into nice format is: " + theTime);

        Log.d("the time is: ", when + " ");

        cal.setTimeInMillis(System.currentTimeMillis());
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 15);

//        cal.set(Calendar.HOUR_OF_DAY, 16);
//        cal.set(Calendar.MINUTE, 00);

        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, StatsAlarmReceiver.class);
        statsIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, statsIntent);

    }


//    public void startChargingUploadAlarm() {
//        Log.d(TAG, "startStatsAlarm: in start alarm");
//
//        Calendar cal = Calendar.getInstance();
//        long when = cal.getTimeInMillis();
//        String timey = Long.toString(when);
//
//        //System.out.println("The time changed into nice format is: " + theTime);
//
//        Log.d("the time is: ", when + " ");
//
//        cal.setTimeInMillis(System.currentTimeMillis());
//        cal.set(Calendar.HOUR_OF_DAY, 22);
//        cal.set(Calendar.MINUTE, 7);
//
////        cal.set(Calendar.HOUR_OF_DAY, 16);
////        cal.set(Calendar.MINUTE, 00);
//
//        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, PowerConnectionUploadReceiver.class);
//        powerIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
//        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, powerIntent);
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (screenStateChange != null)
            unregisterReceiver(screenStateChange);
    }


    private static void writeToFile(File file, String data) {

        FileOutputStream stream = null;
        //System.out.println("The state of the media is: " + Environment.getExternalStorageState());
        Log.d(TAG, "writeToFile: file location is:" + file.getAbsolutePath());

        //OutputStreamWriter stream = new OutputStreamWriter(openFileOutput(file), Context.MODE_APPEND);
        try {
            Log.e("History", "In try");
            Log.d(TAG, "writeToFile: ");
            stream = new FileOutputStream(file, true);
            Log.d(TAG, "writeToFile: 2");
            stream.write(data.getBytes());
            Log.d(TAG, "writeToFile: 3");
        } catch (FileNotFoundException e) {
            Log.e("History", "In catch");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }



}
