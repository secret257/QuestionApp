package jp.android.questionapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class SignUpFragment extends Fragment {

    public static SignUpFragment newInstance(int count) {
        // ReissueMenuFragment インスタンス生成
        SignUpFragment reissueMenuFragment = new SignUpFragment();

        // Bundleにパラメータを設定
        Bundle barg = new Bundle();
        barg.putInt("Counter", count);
        reissueMenuFragment.setArguments(barg);

        return reissueMenuFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_login_signup, container, false);


        return root;
    }
}