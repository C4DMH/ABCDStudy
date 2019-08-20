package gwicks.com.abcdstudy;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.crypto.NoSuchPaddingException;

import gwicks.com.abcdstudy.Setup.FinishInstallScreen;

public class StatsJobService  extends JobService {

    private static final String TAG = "StatsJobService";
    TransferUtility transferUtility;
    String Uri;
    String encryptedUri;
    Encryption mEncryption;

    String userID;

    String meteredNetworkData;


    //static String folder = "/EveryFifteenMin/";
    static String folder = "/AppUsageService/";

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: on destroy being called");

        super.onDestroy();
    }



    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "onStartJob: starting job");

        Calendar c = Calendar.getInstance();


        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
// Checks if the device is on a metered network
        //connMgr.isActiveNetworkMetered() &&
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            Log.d(TAG, "onCreate: isactivenetworkmetered: " + connMgr.isActiveNetworkMetered());
            Log.d(TAG, "onCreate: getActiveNetworkInfor: " + connMgr.getActiveNetworkInfo());
        }




        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        SimpleDateFormat df2 = new SimpleDateFormat("ddMMyyyy");
        String currentDate = df2.format(c.getTime());

        String path = this.getExternalFilesDir(null) + "/videoDIARY/";

        File directory = new File(path);
        if(!directory.exists()){
            Log.d(TAG, "onStartJob: making directory");
            directory.mkdirs();
        }

        ArrayList<File> files1 = new ArrayList<>(Arrays.asList(directory.listFiles()));
        Log.d(TAG, "onStartJob: length/size of files is: " + files1.size());


        Log.d(TAG, "onReceive: we have started onrecieve");
        userID = FinishInstallScreen.secureID;
        mEncryption = new Encryption();
        transferUtility = Util.getTransferUtility(this);
        Log.d(TAG, "onReceive: transfer utility = " + transferUtility);

        Uri = UStats.printCurrentUsageStatus(this,"/videoDIARY/" );
        System.out.println("The uri is: " + Uri);
        String theName = Uri.substring(Uri.lastIndexOf('/') + 1);
        Log.d(TAG, "onStartJob: the name is: " + theName);

        ArrayList<File> files = new ArrayList<>(Arrays.asList(directory.listFiles()));
        Log.d(TAG, "onStartJob: length/size of files is: " + files.size());
        int i = 1;
        for(File each : files){
            Log.d(TAG, "onReceive: path = " + each.getAbsolutePath());
            Encrypt(theName+ "_" + i, each.getAbsolutePath());
            i = i + 1;
            Log.d(TAG, "onReceive: i is: " + i);
            try{
                Log.d(TAG, "onStartJob: deleting");
                each.delete();
            }catch (Exception e){
                Log.d(TAG, "onReceive: error deleting: " + e);
            }

        }
        Log.d(TAG, "onStartJob: 1");

        ArrayList<File> encryptedFiles = new ArrayList<>(Arrays.asList(directory.listFiles()));
        Log.d(TAG, "onStartJob: 2");
        Util.uploadFilesToBucket(encryptedFiles, true,logUploadCallback, this, folder);
        Log.d(TAG, "onStartJob: 3");


//        commented out 26th Feb 2019 to try and fix upload issue
//        String theName = Uri.substring(Uri.lastIndexOf('/') + 1);
//        Log.d(TAG, "onReceive: the name is: " + theName);
//        encryptedUri = Encrypt(theName, Uri);
//        beginUpload2(theName, encryptedUri);
//
//        Util.uploadFilesToBucket(encryptedUri, true, logUploadCallback, this, folder   );





//
//        Log.d(TAG, "onStartJob: Time: " + formattedDate + "  Latitude: " + latitude + "  Longitude: " + longitude);
//        writeToFile(location, "Time: " + formattedDate + "  Latitude: " + latitude + "  Longitude: " + longitude +"\n");

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "onStopJob: in on stop job");
        return false;
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
            path2 = mEncryption.encrypt(mFileName, mFilePath, "/videoDIARY/");
            Log.d(TAG, "Encrypt: the path me get SERVICE is: " + path2);
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
        if (filePath == null) {
            //Toast.makeText(this, "Could not find the filepath of the selected file", Toast.LENGTH_LONG).show();
            Log.d(TAG, "beginUpload2: no file path found");
            return;
        }

        Log.d(TAG, "beginUpload2: middle");

        File file = new File(filePath);
        //TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME, name,
        transferUtility.upload(Constants.BUCKET_NAME,  userID + "/UsageStats/" + name,
                file);

        Log.d(TAG, "beginUpload2: end");
    }

    final Util.FileTransferCallback logUploadCallback = new Util.FileTransferCallback() {
        @SuppressLint("DefaultLocale")

        private String makeLogLine(final String name, final int id, final TransferState state) {
            Log.d("LogUploadTask", "This is AWSBIT");
            return String.format("%s | ID: %d | State: %s", name, id, state.toString());
        }

        @Override
        public void onCancel(int id, TransferState state) {
            Log.d(TAG, makeLogLine("Callback onCancel()", id, state));
        }

        @Override
        public void onStart(int id, TransferState state) {
            Log.d(TAG, makeLogLine("Callback onStart()", id, state));

        }

        @Override
        public void onComplete(int id, TransferState state) {
            Log.d(TAG, makeLogLine("Callback onComplete()", id, state));
            Log.d(TAG, "onComplete: should I delete here?");
        }

        @Override
        public void onError(int id, Exception e) {
            Log.d(TAG, makeLogLine("Callback onError()", id, TransferState.FAILED), e);
        }
    };

}

