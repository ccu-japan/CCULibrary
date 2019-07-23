package jp.co.ccu.cculibrary;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;
/* =================================================================================================
    HTTPリクエスト(POST)
   ===============================================================================================*/
public class HttpPost extends SuperAsyncTaskLoader<HttpPostResult>{
    private boolean bRunnning = false;
    private Context l_context;
    private String l_url = "";
    private Map<String, String> l_map = null;
    private int HttpStatus = 0;
    private StringBuilder src = new StringBuilder();
    private String HostName = "";
    private String HostAddress = "";
    /* ---------------------------------------------------------------------------------------------
    コンストラクタ
    --------------------------------------------------------------------------------------------- */
    public HttpPost(Context context, String Url, Map<String,String> Map) {
        super(context);
        l_context = context;
        l_url = Url;
        l_map = Map;
        bRunnning = false;
    }
    /* ---------------------------------------------------------------------------------------------
    登録してあるリスナー（LoaderCallbacksを実装したクラス）に結果を送信
    --------------------------------------------------------------------------------------------- */
    public void deliverResult(HttpPostResult data) {
        // Loderが処理した結果を返す。(メインスレッドで実行される)
        super.deliverResult(data);
    }
    /* ---------------------------------------------------------------------------------------------
    バックグラウンドで実行する処理
    --------------------------------------------------------------------------------------------- */
    public HttpPostResult loadInBackground() {
        bRunnning = true;

        //[TODO] Loderが実行するバックグラウンド処理
        HttpURLConnection connection = null;
        HttpPostResult result = new HttpPostResult();
        final int TIMEOUT_MILLIS = 8000;
        try
        {
            HttpStatus = 0;
            URL url = new URL(l_url.replace(" ", "%20"));
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(TIMEOUT_MILLIS);// 接続にかかる時間
            connection.setReadTimeout(TIMEOUT_MILLIS);// データの読み込みにかかる時間
            connection.setRequestProperty("User-Agent", "Android");
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=Shift-jis");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 接続
            connection.connect();
            Log.d("【loadInBackground RequestMethod】", connection.getRequestMethod());
            // POST送信
            OutputStream outStream = null;
            try {
                outStream = connection.getOutputStream();
                if (l_map != null) {
                    int cnt = 0;
                    for (String key : l_map.keySet()) {
                        if (cnt==0) {
                            outStream.write((key + "=" + l_map.get(key)).getBytes("shift-jis"));
                        }
                        else {
                            outStream.write(("&" + key + "=" + l_map.get(key)).getBytes("shift-jis"));
                        }
                        cnt++;
                    }
                }
                outStream.flush();
            } catch (IOException e) {
                result.setException(e);
                Log.d("【loadInBackground IOException】", e.getMessage());
            } finally {
                if (outStream != null) {
                    outStream.close();
                }
            }
            // レスポンス受信
            HttpStatus = connection.getResponseCode();
            result.setHttpStatus(HttpStatus);
            result.setHttpError(connection.getResponseMessage());
            Log.d("【loadInBackground HTTP Status】" , String.valueOf(HttpStatus));
            if (HttpStatus == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                while (true)
                {
                    byte[] line = new byte[1024];
                    int size = is.read(line);
                    if (size <= 0)
                        break;
                    src.append(new String(line, "shift-jis"));
                }
                CheckMRBResponce(src.toString(), result);
                if (result.getMrbStatus() == 0) {
//                    Log.d("InputStream" , src.toString());
//                    Log.d("InputStream" , src.toString().trim());
                    result.setData(src.toString().trim());
                }
                else {
                    Log.d("【loadInBackground MRBResponce】", String.valueOf(result.getMrbMsg()));
                }
            }
            else{
            }
        } catch (IOException e)
        {
            result.setException(e);
            Log.d("【loadInBackground IOException】", e.getMessage());
        } finally
        {
//            result.DebugHttpPostResult();
            connection.disconnect();
        }

        try {
            InetAddress addr = InetAddress.getLocalHost();
            HostName = addr.getHostName();
            HostAddress = addr.getHostAddress();
            InetAddress []adrs = InetAddress.getAllByName(HostName);
//            for(int i = 0; i < adrs.length; i++){
//                Log.d("Local IPS", adrs[i].getHostAddress());
//            }
            Log.d("【loadInBackground() Local IP】", HostName);
            Log.d("【loadInBackground() IP Address】", HostAddress);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
            Log.d("【loadInBackground UnknownHostException】", e.getMessage());
        }

        bRunnning = false;
        return result;
    }
    /* ---------------------------------------------------------------------------------------------
    ＭＲＢ状態取得
    --------------------------------------------------------------------------------------------- */
    public int CheckMRBResponce(String Responce, HttpPostResult result)
//                                int[] MrbStatus, String[] MRBMessage)
    {
        // 出力引数クリア
        result.setMrbStatus(0);
        result.setMrbMsg("");

        do {
            int p1 = 0;
            int p2 = 0;

            if ((p1 = Responce.indexOf("MG_V_ErrorCode =",0)) == -1)
                break;
            if ((p1 = Responce.indexOf("\"", p1)) == -1)
                break;
            if ((p2 = Responce.indexOf("\"", p1+1)) == -1)
                break;
            // 文字列から指定の文字列にはさまれた文字列を取得(GetBetweenString)
//            Log.d("MRBErrorCode p1, p2" , String.format("p1[%d] p2[%d]",p1, p2));
            String errcode = Responce.substring(p1+1, p2);
            result.setMrbStatus(Integer.parseInt(errcode));

            if ((p1 = Responce.indexOf("MG_V_ErrorText =", 0)) == -1)
                break;
            if ((p1 = Responce.indexOf("\"", p1)) == -1)
                break;
            if ((p2 = Responce.indexOf("if (typeof user.MG_Server==\"object\")", p1+1)) == -1)
                break;
            if ((p2 = Responce.lastIndexOf("\"", p2+1)) == -1)
                break;
            result.setMrbMsg(Responce.substring(p1+1, p2));
        }while(false);
        return 0;
    }
    /* ---------------------------------------------------------------------------------------------
    HTTP通信中
    --------------------------------------------------------------------------------------------- */
    public boolean isRunning(){
        return bRunnning;
    }
}
