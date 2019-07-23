package jp.co.ccu.cculibrary;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.net.HttpURLConnection;

/* =================================================================================================
    HTTPリクエスト結果(POST)
   ===============================================================================================*/
public class HttpPostResult {
//    private Activity activity;
    private Exception exception;
    private int HttpStatus = 0;
    private String HttpError = "";
    private int MrbStatus = 0;
    private String MrbMsg = "";
    private String data = "";

    // 例外
    public void setException(Exception exception) {
        this.exception = exception;
    }
    public Exception getException() {
        return exception;
    }
    // HTTP Status
    public void setHttpStatus(int HttpStatus) {
        this.HttpStatus = HttpStatus;
    }
    public int getHttpStatus() {
        return HttpStatus;
    }
    // HTTP Status
    public void setHttpError(String HttpError) { this.HttpError = HttpError; }
    public String getHttpError() { return HttpError; }
    // MRB Status
    public void setMrbStatus(int MrbStatus) {
        this.MrbStatus = MrbStatus;
    }
    public int getMrbStatus() {
        return MrbStatus;
    }
    // MRB Message
    public void setMrbMsg(String MrbMsg) {
        this.MrbMsg = MrbMsg;
    }
    public String getMrbMsg() {
        return MrbMsg;
    }
    // HTTP Responce
    public void setData(String data) {
        this.data = data.toString();
    }
    public String getData() {
        return data;
    }
    /* ---------------------------------------------------------------------------------------------
    HTTPリクエスト結果デバック表示
    --------------------------------------------------------------------------------------------- */
    public void DebugHttpPostResult() {
        Log.d("DebugHttpPostResult Exception", String.format("[%s]", exception));
        Log.d("DebugHttpPostResult HttpStatus", String.format("[%d]", HttpStatus));
        Log.d("DebugHttpPostResult MrbStatus", String.format("[%d]", MrbStatus));
        Log.d("DebugHttpPostResult MrbMsg", String.format("[%s]", MrbMsg));
        Log.d("DebugHttpPostResult data", String.format("[%s]", data));
    }
    /* ---------------------------------------------------------------------------------------------
    HTTPリクエスト結果エラー表示
    --------------------------------------------------------------------------------------------- */
    public int ErrorHttpPostResult(String [] Error) {
        int rtn = 0;

        if (exception != null) {
            // WIFI Off .... java.net.ConnectException: failed to connect to /192.168.0.135 (port 80) after 8000ms: connect failed: ENETUNREACH (Network is unreachable)
            // Web Server Not Found .... java.net.SocketTimeoutException: failed to connect to /192.168.2.135 (port 80) after 8000ms
            // IIS Not Run .... java.net.SocketTimeoutException: failed to connect to /192.168.0.135 (port 80) after 8000ms
            // IIS 起動直後 .... java.net.SocketTimeoutException
            // APPNAME Not Found .... java.net.SocketTimeoutException
            rtn = 40;
            Error[0] = "ネットワークエラー\n" + String.format("[%d] ネットワークにアクセスできません。[%s]\nネットワークの接続を確認した後、アプリを再度起動してください。", rtn, exception);
        } else if (HttpStatus != HttpURLConnection.HTTP_OK) {
            // URL Not Found .... 404 etc
            rtn = 20;
            Error[0] = "HTTPエラー\n" +  String.format("[%d] Status[%d:%s]\nネットワークの接続を確認した後、アプリを再度起動してください。", rtn, HttpStatus, HttpError );
        } else if (MrbStatus == -104) {
            rtn = 1;
            Error[0] = "Webサーバーエラー\n" +  String.format("[%d] MRBStatus[%d]\n再度カードをかざしてください。", rtn, MrbStatus);
        } else if (MrbStatus != 0) {
            // MRB not Run .... -102.Error: \"The Requester could not connect to the Broker\
            // PRGNAME Not Found .... -131.Error: \"Program not found\" (-131)
            rtn = 10;
            Error[0] = "Webサーバーエラー\n" + String.format("[%d] MRBStatus[%d]\nサーバーメンテナンス等で実行不可能な状態です。アプリを再度起動してください。", rtn, MrbStatus);
        } else {
            rtn = 0;
            Error[0] = "";
        }
//        Log.d("ErrorHttpPostResult", String.format("rtn[%d]", rtn));
        return rtn;
    }
}
