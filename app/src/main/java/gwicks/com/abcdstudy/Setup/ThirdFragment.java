package gwicks.com.abcdstudy.Setup;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import gwicks.com.abcdstudy.R;

public class ThirdFragment extends Fragment {

    private static final String TAG = "ThirdFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.splash_two, container, false);
        //((SetupStepOne)getActivity()).updateStatusBarColor("#0075e1", this);
        Log.d(TAG, "onCreateView: update coulour in : 3");
        return v;
    }

    public static ThirdFragment newInstance(String text) {

        Log.d(TAG, "newInstance: third");

        ThirdFragment f = new ThirdFragment();
        return f;
    }

}
