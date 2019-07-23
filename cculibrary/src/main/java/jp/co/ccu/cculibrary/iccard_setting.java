package jp.co.ccu.cculibrary;

import android.util.Log;
/* =================================================================================================
    ICCard1タイプHTTPプロトコル解析
   ===============================================================================================*/
public class ICCard_Setting {
    public int Result = 0;
    public int WaitingDispSec = 0;
    public String WaitingTitle = "Fellowship (ICカード打刻処理) 受付中,,,";
    public String WaitingMessage = "ICカードをかざしてください。";
    public int PunchingDispSec = 0;
    public String PunchingTitle = "Fellowship (ICカード打刻処理) 結果";
    public String PunchingMessage = "";
    /* ---------------------------------------------------------------------------------------------
    レスポンス文字列解析
    --------------------------------------------------------------------------------------------- */
    public void GetStartResponce(String Response) {
//        Log.d("GetStartResponce 1", Response);
        Response = Response.replace('|', '\t');
//        Log.d("GetStartResponce 2", Response);
        String[] csv = Response.split("\t", 0);
//        Log.d("csv.length", String.format("%d",csv.length));

        Result = 0;
        WaitingDispSec = 0;
        WaitingTitle = "";
        WaitingMessage = "";
        PunchingDispSec = 0;
        PunchingTitle = "";
        PunchingMessage = "";

        if (csv.length > 0)
            Result = Integer.parseInt(csv[0]);
        if (csv.length > 1)
            WaitingDispSec = Integer.parseInt(csv[1]);
        if (csv.length > 2)
            WaitingTitle = csv[2];
        if (csv.length > 3)
            WaitingMessage = csv[3];
        if (csv.length > 4)
            PunchingDispSec = Integer.parseInt(csv[4]);
        if (csv.length > 5)
            PunchingTitle = csv[5];
        if (csv.length > 6)
            PunchingMessage = csv[6];
    }
    /* ---------------------------------------------------------------------------------------------
    レスポンス文字列解析結果デバック表示
    --------------------------------------------------------------------------------------------- */
    public void DebugAppSetting() {
        Log.d("【DebugAppSetting WaitingDispSec】", String.format("[%d]", WaitingDispSec));
        Log.d("【DebugAppSetting WaitingTitle】", String.format("[%s]", WaitingTitle));
        Log.d("【DebugAppSetting WaitingMessage】", String.format("[%s]", WaitingMessage));
        Log.d("【DebugAppSetting PunchingDispSec】", String.format("[%d]", PunchingDispSec));
        Log.d("【DebugAppSetting PunchingTitle】", String.format("[%s]", PunchingTitle));
        Log.d("【DebugAppSetting PunchingMessage】", String.format("[%s]", PunchingMessage));
    }
}