package gwicks.com.abcdstudy.Setup;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import gwicks.com.abcdstudy.R;

public class StudyCodeVerification extends AppCompatActivity {

    private static final String TAG = "StudyCodeVerification";
    EditText studyCode;
    String code;
    String code2 = "95d51fb621fb7343";
    public Boolean result = false;
    String informedConsent;

//    public interface MyInterface{
//        public void myMethod(boolean result);
//    }

    //public int

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.study_code);
        updateStatusBarColor("#1281e8");
        studyCode = (EditText)findViewById(R.id.studyCode);
        informedConsent = getString(R.string.informed_consent);



    }

    public void updateStatusBarColor(String color){// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "updateStatusBarColor: color change being called!");
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
        }
    }

    public void informedConsent(View v) {

        //Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show();
        code = studyCode.getText().toString();
        Log.d(TAG, "informedConsent: the study code is: " + code);
        validateStudyCode(code);


    }

    public void validateStudyCode(String code){
//        String url = "http://date.jsontest.com";
//        new MyAsyncTask().execute(url);
        Log.d(TAG, "validateStudyCode: ");
        Log.d(TAG, "validateStudyCode: code entered: " + code);

        if(code.equals(code2)){
            Log.d(TAG, "validateStudyCode: code: " + code + " code2: " + code2);
            showDialog();
        }else{
            Toast.makeText(this, "You have entered an incorrect study code. This app is only for approved participants", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "You have entered an incorrect study code. This app is only for approved participants", Toast.LENGTH_LONG).show();


        }


        // This works for real app, removing for test ABCD
//        GetRawData getRawData = new GetRawData(this);
//        getRawData.execute("https://u8x53zf4i5.execute-api.us-east-1.amazonaws.com/default/EARS-study-code-verification?code=" + code);

        Log.d(TAG, "validateStudyCode: returning false");

    }



    public void onDownloadComplete(String data, DownloadStatus status){
        if(status == DownloadStatus.OK){
            Log.d(TAG, "onDownloadComplete: data is : " + data);
            result = true;
            if(data.contains("claimed")){
                showDialog();
            }else{
                Toast.makeText(this, "You have entered an incorrect study code. This app is only for approved participants", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "You have entered an incorrect study code. This app is only for approved participants", Toast.LENGTH_LONG).show();

            }


        }else{
            Log.d(TAG, "onDownloadComplete: failed with status: " + status);
            Toast.makeText(this, "Your study code is incorrect",Toast.LENGTH_LONG).show();
        }
    }

//    class MyAsyncTask extends AsyncTask<String,Void,JSONObject> {
//        @Override
//        protected JSONObject doInBackground(String... strings) {
//            return null;
//        }
//
//
////        @Override
////        protected JSONObject doInBackground(String... urls) {
////            return RestService.doGet(urls[0]);
////        }
////
////        @Override
////        protected void onPostExecute(JSONObject jsonObject) {
////            TextView tv = (TextView) findViewById(R.id.txtView);
////            tv.setText(jsonObject.toString());
////        }
//    }

    public void startInstall()

    {

        Log.d(TAG, "startInstall: ");
        Intent installIntent = new Intent(StudyCodeVerification.this, SetupStepTwo.class);
        StudyCodeVerification.this.startActivity(installIntent);
        finish();

    }

    public void showDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(StudyCodeVerification.this).create();
        //alertDialog.setTitle("7 Cups EARS: Informed Consent & Terms of Service Agreement");
        alertDialog.setTitle("EARS: Informed Consent & Terms of Service Agreement");
        alertDialog.setMessage(Html.fromHtml(informedConsent));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "I Disagree",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"I Agree",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startInstall();
            }
        });
        alertDialog.show();

    }
}

