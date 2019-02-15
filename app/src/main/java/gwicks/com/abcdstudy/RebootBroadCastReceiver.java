package gwicks.com.abcdstudy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import gwicks.com.abcdstudy.Setup.FinishInstallScreen;

public class RebootBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Intent myIntent = new Intent(context, FinishInstallScreen.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }

}