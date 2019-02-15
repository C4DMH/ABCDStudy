package gwicks.com.abcdstudy;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
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


    static String folder = "/videoDIARY/";

    @Override
    public boolean onStartJob(JobParameters params) {

        Calendar c = Calendar.getInstance();



        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        SimpleDateFormat df2 = new SimpleDateFormat("ddMMyyyy");
        String currentDate = df2.format(c.getTime());

        String path = this.getExternalFilesDir(null) + "/videoDIARY/Location/";

        File directory = new File(path);
        if(!directory.exists()){
            Log.d(TAG, "onStartJob: making directory");
            directory.
                    mkdirs();
        }

        File location = new File(directory, currentDate +".txt");


        Log.d(TAG, "onReceive: we have started onrecieve");
        userID = FinishInstallScreen.secureID;
        mEncryption = new Encryption();
        transferUtility = Util.getTransferUtility(this);
        Log.d(TAG, "onReceive: transfer utility = " + transferUtility);

        Uri = UStats.printCurrentUsageStatus(this);
        System.out.println("The uri is: " + Uri);


//        Log.d(TAG, "onReceive: uri2 = " + Uri2);
        String theName = Uri.substring(Uri.lastIndexOf('/') + 1);
        Log.d(TAG, "onReceive: the name is: " + theName);
        encryptedUri = Encrypt(theName, Uri);
        beginUpload2(theName, encryptedUri);





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

        }


        try {

            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
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
            path2 = mEncryption.encrypt(mFileName, mFilePath, "/videoDIARY/");
            Log.d(TAG, "Encrypt: the path me get is: " + path2);
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

}

