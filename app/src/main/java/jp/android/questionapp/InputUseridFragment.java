package jp.android.questionapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.android.questionapp.common.ApplicationDataBean;
import jp.android.questionapp.common.CommonUtils;

public class InputUseridFragment extends Fragment {

    private ConnectDb connectDb;
    private View root = null;
    private int procCode;

    private enum Code {
        chkUserStatus(1);

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
        root = inflater.inflate(R.layout.fragment_login_inputuserid, container, false);
        // タイトル設定
        getActivity().setTitle(R.string.title_inputUserId);
        // ユーザーID
        final TextView textUserId = root.findViewById(R.id.inputUserId_input_userId);
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
        // メッセージ
        final TextView msg = root.findViewById(R.id.inputUserId_label_message1);
        msg.setText(getString(R.string.msg_M007));

        // 次へ ボタン
        Button nextButton = root.findViewById(R.id.inputUserId_button_next);
        nextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String paramUserId = textUserId.getText().toString();
                boolean errFlg = false;
                // ユーザーID必須チェック
                if (!errFlg && !CommonUtils.isEmpty(paramUserId)) {
                    msg.setText(getString(R.string.msg_M007));
                    msg.setTextColor(Color.RED);
                    errFlg = true;
                }

                if (!errFlg) {
                    // 処理コード設定
                    procCode = Code.chkUserStatus.getCode();
                    // 画面間パラメータ設定
                    ApplicationDataBean adb = (ApplicationDataBean)getActivity().getApplication();
                    adb.setUserId(paramUserId);
                    // パラメータ設定
                    Map<String, String> phpFileName = new HashMap<>();
                    phpFileName.put("phpFileName", "test_fetchAll.php");
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", paramUserId);
                    // 非同期処理開始
                    connectDb = new ConnectDb();
                    connectDb.setListener(createListener());
                    connectDb.execute(phpFileName, params);
                }
            }
        });

        // 戻る ボタン
        Button buttonReturn= root.findViewById(R.id.inputUserId_button_return);
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

                if (procCode == Code.chkUserStatus.getCode()) {
                    chkUserStatus(result);
                } else if (procCode == Code.chkUserStatus.getCode()) {

                }

            }
        };
    }

    private void chkUserStatus(List<Map<String, String>> result) {
        // メッセージ
        TextView msg = root.findViewById(R.id.inputUserId_label_message1);
        // 状態コード = nullの場合
        if (result.get(0) == null || result.get(0).get("STATUS_CODE") == null) {
            // TODO TESTコード
            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager != null){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // BackStackを設定
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.container, new ResetPassFragment());
                fragmentTransaction.commit();
            }
        // 状態コード = "XX"(再発行申請受付済み)の場合
        } else if ("XX".equals(result.get(0).get("STATUS_CODE"))) {
            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager != null){
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // BackStackを設定
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.container, new ResetPassFragment());
                fragmentTransaction.commit();
            }
        // 状態コード = "XX"以外の場合
        } else if (!"XX".equals(result.get(0).get("STATUS_CODE"))) {
            msg.setText(getString(R.string.msg_M028));
            msg.setTextColor(Color.RED);
        }


    }
}