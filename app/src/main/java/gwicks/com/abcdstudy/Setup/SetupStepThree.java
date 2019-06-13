package gwicks.com.abcdstudy.Setup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import gwicks.com.abcdstudy.R;

public class SetupStepThree extends AppCompatActivity {

    private static final String TAG = "StepSeven";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_seven );
        updateStatusBarColor("#1281e8");
        moveToNextStep();



        // 27th  Feb 2019. tyr to fix the background data problem

//        if(checkBackgroundData()){
//            Intent intent = new Intent(SetupStepThree.this, MeteredDataClass.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            SetupStepThree.this.startActivity(intent);
////            Intent intent = new Intent();
////            String packageName = getPackageName();
////            intent.setAction(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS);
////            intent.setData(Uri.parse("package:" + packageName));
////            startActivity(intent);
//        }else{
//            moveToNextStep();
//        }





    }

    public void updateStatusBarColor(String color){// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "updateStatusBarColor: color change being called!");
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    public void moveToNextStep(){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SetupStepThree.this, FinishInstallScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                SetupStepThree.this.startActivity(intent);
            }
        }, 4000);

    }

    public boolean checkBackgroundData(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
// Checks if the device is on a metered network
        //connMgr.isActiveNetworkMetered() &&
        if ((android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && connMgr.getRestrictBackgroundStatus() == ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED) ){           //|| connMgr.getBackgroundDataSetting() == false) {
            return true;


            }
            return false;
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}