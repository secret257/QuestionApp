package jp.android.questionapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.android.questionapp.common.CommonUtils;


public class TopMenuFragment extends Fragment {

    private ConnectDb connectDb;
    private View root = null;
    private int procCode;

    private enum Code {
        chkExistLoginMst(1), chkExistAccountTrn(2);

        private int code;

        Code(int code) {
            this.code = code;
        }
        public int getCode() {
            return this.code;
        }
    }

    @Override
    public void onDestroy() {
        if (connectDb != null) {
            connectDb.setListener(null);
        }
        super.onDestroy();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.root = inflater.inflate(R.layout.fragment_login_topmenu, container, false);
        // タイトル設定
        getActivity().setTitle(R.string.title_login);
        // ユーザーID
        final TextView textUserId = root.findViewById(R.id.login_input_userId);
        textUserId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // EditTextのフォーカスが外れた場合
                if (hasFocus == false) {
                    // ソフトキーボードを非表示にする
                    MainActivity activity = (MainActivity)getActivity();
                    activity.hideKeybord(v);
                }
            }
        });
        // パスワード
        final TextView textPass = root.findViewById(R.id.login_input_passWord);
        textPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // EditTextのフォーカスが外れた場合
                if (hasFocus == false) {
                    // ソフトキーボードを非表示にする
                    MainActivity activity = (MainActivity)getActivity();
                    activity.hideKeybord(v);
                }
            }
        });
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

                    fragmentTransaction.replace(R.id.container, InquireFragment.newInstance("1"));
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
                boolean errFlg = false;
                // ユーザーID必須チェック
                if (!errFlg && !CommonUtils.isEmpty(paramUserId)) {
                    msg.setText(getString(R.string.msg_M007));
                    msg.setTextColor(Color.RED);
                    errFlg = true;
                }
                // パスワード必須チェック
                if (!errFlg && !CommonUtils.isEmpty(paramPass)) {
                    msg.setText(getString(R.string.msg_M026));
                    msg.setTextColor(Color.RED);
                    errFlg = true;
                }
                // パスワード半角英数字チェック
                if (!errFlg && !CommonUtils.isHalfAlphanumeric(paramPass)) {
                    msg.setText(getString(R.string.msg_M004));
                    msg.setTextColor(Color.RED);
                    errFlg = true;
                }

                if (!errFlg) {
                    // 処理コード設定
                    procCode = Code.chkExistLoginMst.getCode();
                    // パラメータ設定
                    Map<String, String> phpFileName = new HashMap<>();
                    phpFileName.put("phpFileName", "program_select_subroutine.php");
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", paramUserId);
                    params.put("password", paramPass);
                    params.put("parm_id", "SELECT");
                    params.put("sql_id", "selectUserExistCnt");

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

                    fragmentTransaction.replace(R.id.container, new SignUpFragment());
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

                if (procCode == Code.chkExistLoginMst.getCode()) {
                    chkExistLoginMst(result);
                } else if (procCode == Code.chkExistAccountTrn.getCode()) {

                }

            }
        };
    }

    private void chkExistLoginMst(List<Map<String, String>> result) {
        // メッセージ
        TextView msg = root.findViewById(R.id.login_label_message1);
        // 存在なしの場合
        if ("0".equals(result.get(0).get("CNT"))) {
            msg.setText(R.string.msg_M002);
            msg.setTextColor(Color.RED);
            // 存在ありの場合
        } else {

        }
    }
}