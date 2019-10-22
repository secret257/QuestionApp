package jp.android.questionapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ResetPassFragment2 extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login_resetpass2, container, false);
        // タイトル設定
        getActivity().setTitle(R.string.title_resetPassWord);
        // OK ボタン
        Button okButton = root.findViewById(R.id.resetPassWord2_button_ok);
        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // BackStackを設定
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.replace(R.id.container, new TopMenuFragment());
                    fragmentTransaction.commit();
                }
            }
        });

        return root;
    }
}