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

import jp.android.questionapp.common.ApplicationDataBean;
import jp.android.questionapp.common.CommonUtils;

public class ResetPassFragment extends Fragment {

    private ConnectDb connectDb;
    private View root = null;
    private int procCode;

    private enum Code {
        updatePassword(1);

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
        root = inflater.inflate(R.layout.fragment_login_resetpass, container, false);
        // タイトル設定
        getActivity().setTitle(R.string.title_resetPassWord);
        // パスワード1
        final TextView textPass1 = root.findViewById(R.id.resetPassWord_input_passWord);
        textPass1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        // パスワード2
        final TextView textPass2 = root.findViewById(R.id.resetPassWord_input_passWord2);
        textPass2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        final TextView msg = root.findViewById(R.id.resetPassWord_label_message1);
        msg.setText(getString(R.string.msg_M008));
        // 決定 ボタン
        Button nextButton = root.findViewById(R.id.resetPassWord_button_confirm);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
//                        .setTitle(getString(R.string.msg_M011))
                        .setMessage(getString(R.string.msg_M011))
                        // Yes押下時の処理
                        .setPositiveButton(getString(R.string.label_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String paramPass1 = textPass1.getText().toString();
                                String paramPass2 = textPass2.getText().toString();
                                boolean errFlg = false;
                                // パスワード必須チェック
                                if (!errFlg && (!CommonUtils.isEmpty(paramPass1) || !CommonUtils.isEmpty(paramPass2)) ) {
                                    msg.setText(getString(R.string.msg_M026));
                                    msg.setTextColor(Color.RED);
                                    errFlg = true;
                                }
                                // パスワード半角英数字チェック
                                if (!errFlg && (!CommonUtils.isHalfAlphanumeric(paramPass1) || !CommonUtils.isHalfAlphanumeric(paramPass2))) {
                                    msg.setText(getString(R.string.msg_M004));
                                    msg.setTextColor(Color.RED);
                                    errFlg = true;
                                }
                                // パスワード1 & パスワード2 等価チェック
                                if (!errFlg && paramPass1.equals(paramPass2)) {
                                    msg.setText(getString(R.string.msg_M030));
                                    msg.setTextColor(Color.RED);
                                    errFlg = true;
                                }

                                if (!errFlg) {
                                    // 処理コード設定
                                    procCode = Code.updatePassword.getCode();
                                    // 画面間パラメータ取得
                                    ApplicationDataBean adb = (ApplicationDataBean)getActivity().getApplication();
                                    String userId = adb.getUserId();
                                    // パラメータ設定
                                    Map<String, String> phpFileName = new HashMap<>();
                                    phpFileName.put("phpFileName", "test_fetchAll.php");
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("pass", paramPass1);
                                    params.put("userId", userId);
                                    // 非同期処理開始
                                    connectDb = new ConnectDb();
                                    connectDb.setListener(createListener());
                                    connectDb.execute(phpFileName, params);

                                }

                            }
                        })
                        // No押下時の処理
                        .setNegativeButton(getString(R.string.label_no), null)
                        .show();
            }
        });

        // 戻る ボタン
        Button buttonReturn= root.findViewById(R.id.resetPassWord_button_return);
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

    public ConnectDb.Listener createListener() {
        return new ConnectDb.Listener() {
            @Override
            public void onSuccess(List<Map<String, String>> result) {

                if (procCode == Code.updatePassword.getCode()) {
                    afterUpdatePassword();
                }

            }
        };
    }

    private void afterUpdatePassword() {

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // BackStackを設定
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.replace(R.id.container, new ResetPassFragment2());
            fragmentTransaction.commit();
        }
    }
}