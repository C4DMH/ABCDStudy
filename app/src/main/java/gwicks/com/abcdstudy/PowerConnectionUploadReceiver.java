package gwicks.com.abcdstudy;

//public class PowerConnectionUploadReceiver extends BroadcastReceiver {

//    private static final String TAG = "PowerConnUpReceiver";
//
//    TransferUtility mTransferUtility;
//    Encryption mEncryption;
//    Context mContext;
//    String encryptedPath;
//
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        mContext = context;
//        mEncryption = new Encryption();
//        mTransferUtility = Util.getTransferUtility(mContext);
//
//        String path = mContext.getExternalFilesDir(null) + "/SCREEN/";
//
//        File directory = new File(path);
//
//        if(!directory.exists()){
//            directory.mkdirs();
//        }
//
//        ArrayList<File> files = new ArrayList<>(Arrays.asList(directory.listFiles()));
//        int i = 1;
//        for(File each : files){
//
//            Log.d(TAG, "onReceive: path = " + each.getAbsolutePath());
////            Encrypt(formattedDate + "_" + i, each.getAbsolutePath(), sensor);
//            Encrypt(each.getName(), each.getAbsolutePath());
//
//            i = i + 1;
//            Log.d(TAG, "onReceive: i is: " + i);
//            try{
//                each.delete();
//            }catch (Exception e){
//                Log.d(TAG, "onReceive: error deleting: " + e);
//            }
//        }
//        ArrayList<File> encryptedFiles = new ArrayList<>(Arrays.asList(directory.listFiles()));
//        Util.uploadFilesToBucket(encryptedFiles, true,logUploadCallback, mContext, "/SCREEN/");
//
//
//
//
//    }
//
//
//
//    public String Encrypt(String name, String path){
//        Log.d(TAG, "Encrypt: 1");
//        Log.d(TAG, "Encrypt: name = " + name);
//        Log.d(TAG, "Encrypt: path = " + path);
//        String mFileName = name;
//        Log.d(TAG, "Encrypt: mFIleName is: " + mFileName);
//        String mFilePath = path;
//
//        String path2 = null;
//        try {
//            //com.anysoftkeyboard.utils.Log.d(TAG, "We are starting encrytopn 1 - in doInBackgound AsyncTask ENCRYTPTION!");
//            path2 = mEncryption.encrypt(mFileName, mFilePath, "/SCREEN/");
//            Log.d(TAG, "Encrypt: the path me get is: " + path2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        }
//        Log.d(TAG, "Encrypt: 2");
//        Log.d(TAG, "Encrypt: path2 is: " + path2);
//        //beginUpload2("STATS", path2);
//        return path2;
//
//    }
//
//    final Util.FileTransferCallback logUploadCallback = new Util.FileTransferCallback() {
//        @SuppressLint("DefaultLocale")
//
//        private String makeLogLine(final String name, final int id, final TransferState state) {
//            Log.d("LogUploadTask", "This is AWSBIT");
//            return String.format("%s | ID: %d | State: %s", name, id, state.toString());
//        }
//
//        @Override
//        public void onCancel(int id, TransferState state) {
//            Log.d(TAG, makeLogLine("Callback onCancel()", id, state));
//        }
//
//        @Override
//        public void onStart(int id, TransferState state) {
//            Log.d(TAG, makeLogLine("Callback onStart()", id, state));
//
//        }
//
//        @Override
//        public void onComplete(int id, TransferState state) {
//            Log.d(TAG, makeLogLine("Callback onComplete()", id, state));
//        }
//
//        @Override
//        public void onError(int id, Exception e) {
//            Log.d(TAG, makeLogLine("Callback onError()", id, TransferState.FAILED), e);
//        }
//    };

//}
