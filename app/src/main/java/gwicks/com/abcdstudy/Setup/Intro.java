package gwicks.com.abcdstudy.Setup;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import gwicks.com.abcdstudy.R;

public class Intro extends AppCompatActivity {


    Context mContext;

    private static final String TAG = "Intro";

    public boolean appUsage = false;
    public boolean keyboardInstalled = false;
    public boolean keyboardSelected = false;
    public boolean notificationListener = false;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_step_one);
        updateStatusBarColor("#07dddd");
        mContext = this;

        Calendar cal = Calendar.getInstance();
        int doy = cal.get(Calendar.DAY_OF_YEAR);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("doy", doy);
        editor.apply();







    }

    @Override
    protected void onResume() {
        super.onResume();

//        String path = getExternalFilesDir(null) + "/InstallInfo/";
//
//        File directory = new File(path);
//
//        if(!directory.exists()){
//            directory.mkdirs();
//        }

//        String desination = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sleep/";
//        File destination = new File(desination);
//
//        if(!destination.exists()){
//            destination.mkdirs();
//        }

//        File installFile = new File(path + "InstallFile.txt");
//
//
//        String deviceMan = MANUFACTURER;
//
//        Log.d(TAG, "onResume: deviceMan = " + deviceMan);
//
//        String deviceModel = MODEL;
//        int versionCode = BuildConfig.VERSION_CODE;
//        String versionName = BuildConfig.VERSION_NAME;
//        String versionFlavor = BuildConfig.FLAVOR;
//
//        writeToFile(installFile, deviceMan + "," + deviceModel +"," + versionCode +"," + versionName + "," + versionName + "," + versionName + "," + versionFlavor);

//
//        if(deviceMan.equals("LGE")){ // LG bug, so cant step through to end
//            moveToNextStep();
//        }
        // If we have access to usage stats, move to final step as app has previously been installed
        if(isAccessGranted()) {
            moveToFinalStep();
        }
        // Otherwise, install from the beginning
        else {
            Intent i = new Intent(Intro.this, BatteryOptimization.class);
            Log.d(TAG, "run: 42");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Intro.this.startActivity(i);
            //moveToNextStep();
        }
    }

    public void moveToNextStep(){
        Log.d(TAG, "moveToNextStep: ");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(Intro.this, SetupStepOne.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                Intro.this.startActivity(intent);
                finish();
            }
        }, 6000);

    }

    public void moveToFinalStep(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(Intro.this, FinishInstallScreen.class);
                Intro.this.startActivity(intent);
            }
        }, 6000);

    }

    public void updateStatusBarColor(String color){// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "updateStatusBarColor: color change being called!");
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    public boolean isAccessGranted() {
        try {
            Log.d(TAG, "isAccessGranted: in isaccessgranted");

            PackageManager packageManager = this.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(this.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) this.getSystemService(Context.APP_OPS_SERVICE);
            int mode = 1;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                Log.d(TAG, "isAccessGranted: usage stats = " + AppOpsManager.MODE_ALLOWED );
                Log.d(TAG, "isAccessGranted: ??");
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            Log.d(TAG, "isAccessGranted: mode = " + mode);
            Log.d(TAG, "isAccessGranted: mode : " + mode + "appopsmanager = " + AppOpsManager.MODE_ALLOWED);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "isAccessGranted: name not found");
            return false;
        }catch(Exception e){
            return false;
        }
    }


    public boolean checkNotificationEnabled() {
        try{
            Log.d(TAG, "checkNotificationEnabled: in try");
            if(Settings.Secure.getString(this.getContentResolver(),
                    "enabled_notification_listeners").contains(this.getApplication().getPackageName()))
            {
                Log.d(TAG, "checkNotificationEnabled: in true");

                Log.d(TAG, "checkNotificationEnabled: true");
                return true;
            } else {

                Log.d(TAG, "checkNotificationEnabled: ruturn false");
                return false;
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "checkNotificationEnabled: Did not get into settings?");
        return false;
    }

    private static void writeToFile(File file, String data) {

        FileOutputStream stream = null;
        //Log.d(TAG, "The state of the media is: " + Environment.getExternalStorageState());
        //Log.d(TAG, "writeToFile: file location is:" + file.getAbsolutePath());

        //OutputStreamWriter stream = new OutputStreamWriter(openFileOutput(file), Context.MODE_APPEND);
        try {
            //Log.e("History", "In try");
            //Log.d(TAG, "writeToFile: ");
            stream = new FileOutputStream(file, true);
            //Log.d(TAG, "writeToFile: 2");
            stream.write(data.getBytes());
            //Log.d(TAG, "writeToFile: 3");
        } catch (FileNotFoundException e) {
            Log.e("History", "In catch");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }


        try {

            stream.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
