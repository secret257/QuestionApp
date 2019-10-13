package jp.android.questionapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ConnectDb extends AsyncTask<Map<String, String>, Void, List<Map<String, String>>> {

    private Listener listener;

    // 非同期処理
    @Override
    protected List<Map<String, String>> doInBackground(Map<String, String>... params) {

        // secretドメイン + 使用phpを指定
        String urlSt = "http://secretp.starfree.jp/" + params[0].get("phpFileName");

        HttpURLConnection httpConn = null;
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

        try{
            // URL設定
            URL url = new URL(urlSt);
            // HttpURLConnection
            httpConn = (HttpURLConnection) url.openConnection();
            // request POST
            httpConn.setRequestMethod("POST");
            // no Redirects
            httpConn.setInstanceFollowRedirects(false);
            // データを書き込む
            httpConn.setDoOutput(true);
            // 時間制限
            httpConn.setReadTimeout(10000);
            httpConn.setConnectTimeout(20000);
            // 接続
            httpConn.connect();

            try(// POSTデータ送信処理
                OutputStream outStream = httpConn.getOutputStream()) {
                StringBuilder sb;
                boolean flg = false;
                for(Map.Entry<String, String> entry : params[1].entrySet()) {
                    sb = new StringBuilder();
                    if (flg) {
                        sb.append("&");
                    }
                    sb.append(entry.getKey());
                    sb.append("=");
                    sb.append(entry.getValue());
                    outStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
                    outStream.flush();

                    flg = true;
                }
                Log.d("debug","flush");
            } catch (IOException e) {
                // POST送信エラー
                e.printStackTrace();
            }

            final int status = httpConn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                // レスポンスを受け取る処理
                InputStream stream = httpConn.getInputStream();
                StringBuffer sb = new StringBuffer();
                String line;
                int dataCnt = 0;
                BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    dataCnt++;
                }
                // JSON解析：json_arrayにデータを格納。
                try{
                    // 取得データが0件の場合
                    if (dataCnt == 0) {
                        Log.e("Error","JSONデータがありません");
                    // 取得データが1件のみの場合
                    } else if(dataCnt == 1) {
                        JSONObject jsonObj = new JSONObject(sb.toString().trim());
                        Iterator<String> keysItr = jsonObj.keys();
                        // 取得データのカラム分だけループ
                        while (keysItr.hasNext()) {
                            Map<String, String> resultData = new HashMap<String, String>();
                            String key = keysItr.next();
                            resultData.put(key, String.valueOf(jsonObj.get(key)));
                            resultList.add(resultData);
                        }
                    // 取得データが複数件の場合
                    } else {
                        JSONArray jsonArray = new JSONArray(sb.toString().trim());
                        // 取得データの件数分だけループ
                        for (int i=0; i<jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            Iterator<String> keysItr = jsonObj.keys();
                            // 取得データのカラム分だけループ
                            while (keysItr.hasNext()) {
                                Map<String, String> resultData = new HashMap<String, String>();
                                String key = keysItr.next();
                                resultData.put(key, String.valueOf(jsonObj.get(key)));
                                resultList.add(resultData);
                            }
                        }
                    }

//                    json_array = new JSONArray(str);
//                    for (int i=0; i < json_array.length(); i++) {
//                        JSONObject j_obj = json_array.getJSONObject(i);
//                        Log.i( "Info", j_obj.getString("USER_ID"));
//                        Log.i( "Info", j_obj.getString("USER_NAME"));
//                        Log.i( "Info", j_obj.getString("SEQ"));
//                        Log.i( "Info", j_obj.getString("CREATE_DATE"));
//                    }
                }catch(JSONException e){
                    Log.e("Error","JSONデータが不正");
                    e.printStackTrace();
                }
            } else {
                Log.e("Error","ネットワークエラー");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
        return resultList;
    }

    // 非同期処理が終了後、結果をメインスレッドに返す
    @Override
    protected void onPostExecute(List<Map<String, String>> result) {
        super.onPostExecute(result);

        if (listener != null) {
            listener.onSuccess(result);
        }
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onSuccess(List<Map<String, String>> result);
    }
}