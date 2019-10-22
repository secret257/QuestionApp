package jp.android.questionapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.android.questionapp.common.CommonUtils;


public class InquireFragment2 extends Fragment {

    private View root = null;

    public static InquireFragment2 newInstance(String procCls) {
        // InquireFragment インスタンス生成
        InquireFragment2 inquireFragment2 = new InquireFragment2();

        // Bundleにパラメータを設定
        Bundle barg = new Bundle();
        barg.putString("PROC_CLS", procCls);
        inquireFragment2.setArguments(barg);

        return inquireFragment2;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_login_inquire2, container, false);
        Bundle args = getArguments();
        // タイトル設定
        getActivity().setTitle(R.string.title_inquire);

        final String procCls = args.getString("PROC_CLS");
        // OK ボタン
        Button buttonReturn= root.findViewById(R.id.inquire2_input_ok);
        buttonReturn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager != null){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // ログイン画面からの起動の場合
                    if ("1".equals(procCls)) {
                        // ログイン画面(トップメニュー)に戻る
                        fragmentTransaction.replace(R.id.container, new TopMenuFragment());
                    // ホーム画面からの起動の場合
                    } else if ("2".equals(procCls)) {
                        // ホーム画面に戻る // TODO
                        fragmentTransaction.replace(R.id.container, new TopMenuFragment());
                    }
                    fragmentTransaction.commit();
                }
            }
        });

        return root;
    }

}