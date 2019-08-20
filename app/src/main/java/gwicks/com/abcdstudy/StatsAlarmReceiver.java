package gwicks.com.abcdstudy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import gwicks.com.abcdstudy.Setup.FinishInstallScreen;

public class StatsAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "StatsAlarmReceiver";
    TransferUtility transferUtility;
    String Uri;
    String encryptedUri;
    Encryption mEncryption;
    Context mContext;
    String userID;


    @Override
    public void onReceive(Context context, Intent intent) {

        mContext = context;
        Log.d(TAG, "onReceive: we have started onrecieve");
        userID = FinishInstallScreen.secureID;
        mEncryption = new Encryption();
        transferUtility = Util.getTransferUtility(mContext);
        Log.d(TAG, "onReceive: transfer utility = " + transferUtility);

        Uri = UStats.printCurrentUsageStatus(mContext, "/videoDIARY2/");
        System.out.println("The uri alarm is: " + Uri);

//        String Uri2 = UStats.printCurrentUsageStatus2(mContext);
//        Log.d(TAG, "onReceive: uri2 = " + Uri2);
        String theName = Uri.substring(Uri.lastIndexOf('/') + 1);
        Log.d(TAG, "onReceive: the name is: " + theName);
        encryptedUri = Encrypt(theName, Uri);
        beginUpload2(theName, encryptedUri);
        deleteOriginalFile(Uri);
    }

    public void deleteOriginalFile(String path){

        Log.d(TAG, "deleteOriginalFile: the file to be delted is: " + path);

        File file = new File(path);


        try{
            Log.d(TAG, "onStartJob: deleting");
            file.delete();
        }catch (Exception e){
            Log.d(TAG, "onReceive: error deleting: " + e);
        }



    }

    public String Encrypt(String name, String path){
        Log.d(TAG, "Encrypt: 1");
        Log.d(TAG, "Encrypt: name = " + name);
        Log.d(TAG, "Encrypt: path = " + path);
        String mFileName = name;
        Log.d(TAG, "Encrypt: mFIleName is: " + mFileName);
        String mFilePath = path;

        String path2 = null;
        try {
            //com.anysoftkeyboard.utils.Log.d(TAG, "We are starting encrytopn 1 - in doInBackgound AsyncTask ENCRYTPTION!");
            path2 = mEncryption.encrypt(mFileName, mFilePath, "/videoDIARY2/");
            Log.d(TAG, "Encrypt: the path me get ALARM is: " + path2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Encrypt: 2");
        Log.d(TAG, "Encrypt: path2 is: " + path2);
        //beginUpload2("STATS", path2);
        return path2;
    }

    private void beginUpload2(String name, String filePath) {
        Log.d(TAG, "beginUpload2: start of beginupload2");
        Log.d(TAG, "beginUpload2: the filepath is: " + filePath);
        Log.d(TAG, "beginUpload2: USERID: " + userID);
        if (filePath == null) {
            //Toast.makeText(this, "Could not find the filepath of the selected file", Toast.LENGTH_LONG).show();
            Log.d(TAG, "beginUpload2: no file path found");
            return;
        }

        Log.d(TAG, "beginUpload2: middle");

        File file = new File(filePath);
        //TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME, name,
        transferUtility.upload(Constants.BUCKET_NAME,  userID + "/EveryHour/" + name,
                file);
        Log.d(TAG, "beginUpload2: end");
    }
}