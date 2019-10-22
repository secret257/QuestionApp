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
import androidx.lifecycle.ViewModelProviders;

import jp.android.questionapp.ui.login.ReissueMenuModel;

public class ReissueMenuFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_login_reissuemenu, container, false);
        // タイトル設定
        getActivity().setTitle(R.string.title_reIssuePassWordMenu);
        // パスワード再発行申請 リンク
        final TextView textReissueRequest= root.findViewById(R.id.reIssuePassWordMenu_link_requestReIssuePassWord);
        textReissueRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager != null){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // BackStackを設定
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.replace(R.id.container, new ReissueRequestFragment());
                    fragmentTransaction.commit();
                }
            }
        });

        // パスワード再設定 リンク
        final TextView textResetPass= root.findViewById(R.id.reIssuePassWordMenu_link_resetPassWord);
        textResetPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager != null){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // BackStackを設定
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.replace(R.id.container, new InputUseridFragment());
                    fragmentTransaction.commit();
                }
            }
        });

        // 戻る ボタン
        Button buttonReturn= root.findViewById(R.id.reIssuePassWordMenu_button_return);
        buttonReturn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FragmentManager fragmentManager = getFragmentManager();
                if(fragmentManager != null) {
                    fragmentManager.popBackStack();
                }
            }
        });

        return root;
    }
}