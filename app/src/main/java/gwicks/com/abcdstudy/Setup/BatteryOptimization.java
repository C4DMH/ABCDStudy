package gwicks.com.abcdstudy.Setup;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import gwicks.com.abcdstudy.R;

public class BatteryOptimization extends AppCompatActivity {

    private static final String TAG = "BatteryOptimization";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery_opt);
        updateStatusBarColor("#1281e8");
    }

    public void askForBatteryOptimization(View v){
        Log.d(TAG, "askForBatteryOptimization: 1");



        final Handler handler = new Handler();

        Runnable checkOverlaySetting = new Runnable() {

            @Override
            //@TargetApi(23)
            public void run() {
                Log.d(TAG, "run: 1");
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Log.d(TAG, "run: 2");
                    return;
                }

                // 18th Jan 2018, below works, trying to stop using the intent ( ie try back button below).
                if (checkPowerBattery()) {
                    Log.d(TAG, "run: 3");
                    //You have the permission, re-launch MainActivity
                    //dismiss();

                    Log.d(TAG, "run: you have the permission, lauching next");

                    Intent installIntent = new Intent(BatteryOptimization.this, SetupStepThree.class);
                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    BatteryOptimization.this.startActivity(installIntent);
                    finish();


//                    Log.d(TAG, "run: have the permission, move on now");
//                    Intent i = new Intent(mContext, MainActivity.class);
//                    Log.d(TAG, "run: 4");
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    mContext.startActivity(i);
                    return;
                }
                Log.d(TAG, "run: 5");

                handler.postDelayed(this, 200);
            }
        };

        //Old way, working 7th August 2019
        Log.d(TAG, "askForBatteryOptimization: 2");
        Intent intent = new Intent();
        String packageName = getPackageName();
//        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:" + packageName));
        // startActivity(intent);


        try {
            Log.d(TAG, "onClick: 5");
            startActivity(intent);
            //startActivity(startSettings);
            handler.postDelayed(checkOverlaySetting, 1000);
        } catch (ActivityNotFoundException notFoundEx) {
            //weird.. the device does not have the IME setting activity. Nook?
            // Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }

        // New way

        int permissionRequestCode = 201;

        if(Build.VERSION.SDK_INT > 23){
            //String perms =
            //requestPermissions(perms, permissionRequestCode);
            Log.d(TAG, "askForBatteryOptimization: about to request");
            ActivityCompat.requestPermissions(this, new String[]{Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS}, permissionRequestCode);

            Intent intentNew = new Intent();
            String packageNameNew = getPackageName();
//        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            intentNew.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intentNew.setData(Uri.parse("package:" + packageName));
        // startActivity(intent);


        try {
            Log.d(TAG, "onClick: 5");
            startActivity(intent);
            //handler.postDelayed(checkOverlaySetting, 1000);
        } catch (ActivityNotFoundException notFoundEx) {
            //weird.. the device does not have the IME setting activity. Nook?
            // Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }


        }







    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        switch(requestCode){
            case 201:
            {

                String title = "EARS is more reliable with this battery permission";
                String body ="Ears runs in the background collecting App usage habits. Because you never see it, the phone can sometimes think it is no longer needed and stop it from running. Giving us this permission helps prevent that. Are you sure you want Deny?";
                Log.d(TAG, "onRequestPermissionsResult: lenght: " + grantResults.length + " and the seulsts: " + grantResults[0]);
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: permission is granted :)");
                }else{
                    Log.d(TAG, "onRequestPermissionsResult: permission is NOT granted");

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(body)
                            .setTitle(title)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "Skipping Permission");
                                    Intent installIntent = new Intent(BatteryOptimization.this, SetupStepThree.class);
                                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    BatteryOptimization.this.startActivity(installIntent);
                                    finish();
                                }
                            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            askForBatteryOptimization(null);
                            Log.d(TAG, "onClick: NO");
                        }
                    })
                            .show();


                    //AlertDialog dialog = builder.create();
                  //  dialog.

                }
            }
        }
        
        
        
        
    }

    //    public void askForBatteryOptimization(View v){
//        final Handler handler = new Handler();
//
//        Runnable checkOverlaySetting = new Runnable() {
//
//            @Override
//            //@TargetApi(23)
//            public void run() {
//                Log.d(TAG, "run: 1");
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                    Log.d(TAG, "run: 2");
//                    //return;
//                }
//
//                // 18th Jan 2018, below works, trying to stop using the intent ( ie try back button below).
//                if (checkPowerBattery()) {
//                    Log.d(TAG, "run: 3");
//                    //You have the permission, re-launch MainActivity
//                    //dismiss();
//
//                    Log.d(TAG, "run: you have the permission, lauching next");
//
//                    Intent installIntent = new Intent(BatteryOptimization.this, SetupStepThree.class);
//                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    BatteryOptimization.this.startActivity(installIntent);
//                    finish();
//
//
////                    Log.d(TAG, "run: have the permission, move on now");
////                    Intent i = new Intent(mContext, MainActivity.class);
////                    Log.d(TAG, "run: 4");
////                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                    mContext.startActivity(i);
//                    return;
//                }
//                Log.d(TAG, "run: 5");
//
//                handler.postDelayed(this, 200);
//            }
//        };
//
//        Intent intent = new Intent();
//        intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
//        //intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
//        // context.startActivity(intent);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
////        this.startActivity(intent)
//        try {
//            Log.d(TAG, "onClick: 5");
//            startActivity(intent);
//            //startActivity(startSettings);
//            handler.postDelayed(checkOverlaySetting, 1000);
//        } catch (ActivityNotFoundException notFoundEx) {
//            //weird.. the device does not have the IME setting activity. Nook?
//            // Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
//        }
//    }

    public void updateStatusBarColor(String color) {// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "updateStatusBarColor: color change being called!");
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    public Boolean checkPowerBattery(){

        Boolean status = false;
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            status = pm.isIgnoringBatteryOptimizations(packageName);
        }



        return status;

    }
}