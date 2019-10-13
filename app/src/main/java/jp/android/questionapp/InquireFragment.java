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


public class InquireFragment extends Fragment {

    public static InquireFragment newInstance(int count) {
        // ReissueMenuFragment インスタンス生成
        InquireFragment reissueMenuFragment = new InquireFragment();

        // Bundleにパラメータを設定
        Bundle barg = new Bundle();
        barg.putInt("Counter", count);
        reissueMenuFragment.setArguments(barg);

        return reissueMenuFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_login_inquire, container, false);

        // パスワード再発行申請 リンク
        final TextView textReissueRequest= root.findViewById(R.id.text_label_reissueRequest);
        textReissueRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager != null){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // BackStackを設定
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.replace(R.id.container, new ReissueMenuFragment());
                    fragmentTransaction.commit();
                }
            }
        });

        // 戻る ボタン
        Button buttonReturn= root.findViewById(R.id.button_returnFromReissuemenu);
        buttonReturn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager != null){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // BackStackを設定
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.replace(R.id.container, InquireFragment.newInstance(0));
                    fragmentTransaction.commit();
                }
            }
        });

        return root;
    }
}