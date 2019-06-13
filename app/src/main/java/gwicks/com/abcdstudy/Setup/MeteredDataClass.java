package gwicks.com.abcdstudy.Setup;

//public class MeteredDataClass extends AppCompatActivity {
//
//    private static final String TAG = "MeteredDataClass";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.metered_data);
//        updateStatusBarColor("#07dddd");
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                appUsage();
//                //finish();
//            }
//        }, 6000);
//
//
//    }
//
//    public void appUsage(){
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
//                if (isAccessGranted()) {
//                    Log.d(TAG, "run: 3");
//                    Intent i = new Intent(MeteredDataClass.this, FinishInstallScreen.class);
//                    Log.d(TAG, "run: 42");
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    MeteredDataClass.this.startActivity(i);
//                    //Log.d(TAG, "the activity is: " + getActivity());
//                    return;
//                }
//
//                handler.postDelayed(this, 200);
//            }
//        };
//
//
//        Intent intent = new Intent();
//        String packageName = getPackageName();
//        intent.setAction(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS);
//        intent.setData(Uri.parse("package:" + packageName));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//        handler.postDelayed(checkOverlaySetting, 1000);
//        startActivity(intent);
//
//
//    }
//
//    public boolean isAccessGranted() {
//        try {
//            ConnectivityManager connMgr = (ConnectivityManager)
//                    getSystemService(Context.CONNECTIVITY_SERVICE);
//// Checks if the device is on a metered network
//            //connMgr.isActiveNetworkMetered() &&
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && connMgr.getRestrictBackgroundStatus() == ConnectivityManager.RESTRICT_BACKGROUND_STATUS_WHITELISTED) {
//                return true;
//
//
//            }
//            return false;
//
//
//        } catch (Exception e) {
//            Log.d(TAG, "isAccessGranted: name not found");
//            return false;
//        }
//    }
//
//
//
//
//
//
//    public void updateStatusBarColor(String color){// Color must be in hexadecimal fromat
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Log.d(TAG, "updateStatusBarColor: color change being called!");
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.parseColor(color));
//        }
//    }
//    @Override
//    public void onBackPressed() {
//        moveTaskToBack(true);
//    }
//}
