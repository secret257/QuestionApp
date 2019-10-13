package jp.android.questionapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TopMenuFragment extends Fragment {

    private ConnectDb connectDb;
    private View root = null;

    @Override
    public void onDestroy() {
        connectDb.setListener(null);
        super.onDestroy();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.root = inflater.inflate(R.layout.fragment_login_topmenu, container, false);
        // ユーザーID
        final TextView textUserId = root.findViewById(R.id.login_input_userId);
        // パスワード
        final TextView textPass = root.findViewById(R.id.login_input_passWord);
        // メッセージ
        final TextView msg = root.findViewById(R.id.login_label_message1);
        msg.setText(getString(R.string.msg_M001));

        // パスワード再発行 リンク
        final TextView textReissuePass = root.findViewById(R.id.login_link_reIssuePassWord);
        textReissuePass.setOnClickListener(new View.OnClickListener(){
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
        // お問い合わせ リンク
        final TextView textInquire = root.findViewById(R.id.login_link_inquire);
        textInquire.setOnClickListener(new View.OnClickListener(){
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

        // ログイン ボタン
        Button loginButton = root.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String paramUserId = textUserId.getText().toString();
                String paramPass = textPass.getText().toString();
                if (paramUserId == null || "".equals(paramUserId)) {
                    msg.setText(getString(R.string.msg_M007));
                } else if (paramPass == null || "".equals(paramPass)) {
                    msg.setText(getString(R.string.msg_M026));
                } else {
                    // パラメータ設定
                    Map<String, String> phpFileName = new HashMap<String, String>();
                    phpFileName.put("phpFileName", "test.php");
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", paramUserId);
                    params.put("pass", paramPass);
                    // 非同期処理開始
                    connectDb = new ConnectDb();
                    connectDb.setListener(createListener());
                    connectDb.execute(phpFileName, params);
                }
            }
        });
        // 新規会員登録 ボタン
        Button signUpButton = root.findViewById(R.id.login_button_signUp);
        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager != null){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // BackStackを設定
                    fragmentTransaction.addToBackStack(null);

                    fragmentTransaction.replace(R.id.container, SignUpFragment.newInstance(0));
                    fragmentTransaction.commit();
                }
            }
        });
        return root;
    }

    private ConnectDb.Listener createListener() {
        return new ConnectDb.Listener() {
            @Override
            public void onSuccess(List<Map<String, String>> result) {
                // エラーメッセージ
                TextView errMsg = root.findViewById(R.id.login_label_message1);

                if ("0".equals(result.get(0).get("CNT"))) {
                    errMsg.setText(R.string.msg_M002);
                }
//                    for (int i=0; i < result.length(); i++) {
//                        JSONObject j_obj = result.getJSONObject(i);
//
//                    }

            }
        };
    }
}