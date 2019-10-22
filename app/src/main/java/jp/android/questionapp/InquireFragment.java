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


public class InquireFragment extends Fragment {

    private ConnectDb connectDb;
    private View root = null;
    private int procCode;
    private String ownProcCls = null;

    private enum Code {
        sendInquiry(1);

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

    public static InquireFragment newInstance(String procCls) {
        // InquireFragment インスタンス生成
        InquireFragment inquireFragment = new InquireFragment();

        // Bundleにパラメータを設定
        Bundle barg = new Bundle();
        barg.putString("PROC_CLS", procCls);
        inquireFragment.setArguments(barg);

        return inquireFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_login_inquire, container, false);
        Bundle args = getArguments();

        // タイトル設定
        getActivity().setTitle(R.string.title_inquire);
        // 返信先メールアドレス
        final TextView textReMailAddress = root.findViewById(R.id.inquire_input_mailAddress);
        textReMailAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        // お問い合わせ内容
        final TextView inquiryContent = root.findViewById(R.id.inquire_input_inquiry);
        inquiryContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        final TextView textMessage = root.findViewById(R.id.inquire_label_message1);
        // パスワード再発行申請 リンク
        final TextView textReissueRequest = root.findViewById(R.id.inquire_link_message1);
        final String procCls = args.getString("PROC_CLS");
        ownProcCls = procCls;
        // ログイン画面からの起動の場合
        if ("1".equals(procCls)) {
            textReissueRequest.setText(getString(R.string.msg_M013));
            textReissueRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getFragmentManager();

                    if (fragmentManager != null) {
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        // BackStackを設定
                        fragmentTransaction.addToBackStack(null);

                        fragmentTransaction.replace(R.id.container, new ReissueMenuFragment());
                        fragmentTransaction.commit();
                    }
                }
            });
        // ホーム画面からの起動の場合
        } else if ("2".equals(procCls)) {
            textReissueRequest.setText(getString(R.string.msg_M015));
        }
        // 送信 ボタン
        Button sendReturn= root.findViewById(R.id.inquire_button_send);
        sendReturn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                new AlertDialog.Builder(getActivity())
//                        .setTitle(getString(R.string.msg_M011))
                        .setMessage(getString(R.string.msg_M016))
                        // Yes押下時の処理
                        .setPositiveButton(getString(R.string.label_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String paramRemailAddress = textReMailAddress.getText().toString();
                                String paramInquiryContent = inquiryContent.getText().toString();
                                boolean errFlg = false;
                                // メールアドレス必須チェック
                                if (!errFlg && !CommonUtils.isEmpty(paramRemailAddress) ) {
                                    textMessage.setText(getString(R.string.msg_M031));
                                    textMessage.setTextColor(Color.RED);
                                    errFlg = true;
                                }
                                // 質問内容必須チェック
                                if (!errFlg && !CommonUtils.isEmpty(paramInquiryContent) ) {
                                    textMessage.setText(getString(R.string.msg_M032));
                                    textMessage.setTextColor(Color.RED);
                                    errFlg = true;
                                }

                                if (!errFlg) {
                                    // 処理コード設定
                                    procCode = Code.sendInquiry.getCode();
                                    // 画面間パラメータ取得
                                    ApplicationDataBean adb = (ApplicationDataBean)getActivity().getApplication();
                                    String userId = adb.getUserId();
                                    // パラメータ設定
                                    Map<String, String> phpFileName = new HashMap<>();
                                    phpFileName.put("phpFileName", "test_fetchAll.php");
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("pass", paramRemailAddress);
                                    params.put("inquiry", paramInquiryContent);
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
        Button buttonReturn= root.findViewById(R.id.inquire_button_return);
        buttonReturn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager != null){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // BackStackを設定
                    fragmentTransaction.addToBackStack(null);
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

    private ConnectDb.Listener createListener() {
        return new ConnectDb.Listener() {
            @Override
            public void onSuccess(List<Map<String, String>> result) {

                if (procCode == Code.sendInquiry.getCode()) {
                    afterSendInquiry();
                } else if (procCode == Code.sendInquiry.getCode()) {

                }

            }
        };
    }

    private void afterSendInquiry() {

        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.container, InquireFragment2.newInstance(ownProcCls));
            fragmentTransaction.commit();
        }
    }
}