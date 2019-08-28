package gwicks.com.abcdstudy;

import android.annotation.SuppressLint;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import gwicks.com.abcdstudy.Setup.FinishInstallScreen;

public class WorkManagerUsage extends Worker {

    private static final String TAG = "WorkManagerUsage";
    public Context mContext;
    TransferUtility transferUtility;
    String Uri;
    String encryptedUri;
    Encryption mEncryption;

    String userID;
    static String folder = "/AppUsageManager/";

    public WorkManagerUsage(@NonNull Context context, @NonNull WorkerParameters workerParams) {


        super(context, workerParams);
        mContext = context;
        Log.d(TAG, "WorkManagerUsage: in");
    }


    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "doWork: ");
        Calendar c = Calendar.getInstance();


        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        SimpleDateFormat df2 = new SimpleDateFormat("ddMMyyyy");
        String currentDate = df2.format(c.getTime());

        String path = mContext.getExternalFilesDir(null) + "/videoDIARY3/";

        File directory = new File(path);
        if(!directory.exists()){
            Log.d(TAG, "onStartJob: making directory");
            directory.mkdirs();
        }

        ArrayList<File> files1 = new ArrayList<>(Arrays.asList(directory.listFiles()));
        Log.d(TAG, "onStartJob: length/size of files1 is: " + files1.size());


        Log.d(TAG, "onReceive: we have started onrecieve");
        userID = FinishInstallScreen.secureID;
        mEncryption = new Encryption();
        transferUtility = Util.getTransferUtility(mContext);
        Log.d(TAG, "onReceive: transfer utility = " + transferUtility);

        Uri = UStats.printCurrentUsageStatus(mContext,"/videoDIARY3/");
        Log.d(TAG, "doWork: the uri is : " + Uri);
        String theName = Uri.substring(Uri.lastIndexOf('/') + 1);
        Log.d(TAG, "WORK MAN: the name is: " + theName);

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
        Log.d(TAG, "BEFORE WORK MANAGER");
        Util.uploadFilesToBucket(encryptedFiles, true,logUploadCallback, mContext, folder);
        Log.d(TAG, "AFTER WORK MANAGER");

        Data outputData = new Data.Builder().putString("Result", "Jobs Finished").build();

        return Result.success(outputData);

        //return null;
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
            path2 = mEncryption.encrypt(mFileName, mFilePath, "/videoDIARY3/");
            Log.d(TAG, "Encrypt: the path me get is mananger: " + path2);
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
