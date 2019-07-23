package jp.co.ccu.cculibrary;

import android.content.Context;
import android.util.Log;

import java.util.Map;
/* =================================================================================================
    MagicRequestBrokerを介したHTTP通信
   ===============================================================================================*/
public class MagicRequestBroker {
    private httppost hp = null;
    /* ---------------------------------------------------------------------------------------------
    POSTリクエスト送信
    --------------------------------------------------------------------------------------------- */
    public void SendRequest(Context context, String url, Map<String, String> map){
        hp = new httppost(context, url, map);
    }
    /* ---------------------------------------------------------------------------------------------
    レスポンス解析
    --------------------------------------------------------------------------------------------- */
    public int CheckMRBResponce(String Responce, String[] MRBMessage){
        MagicRequestBroker mrb = new MagicRequestBroker();
        int [] MrbStatus = new int[1];
        mrb.CheckMRB(Responce, MrbStatus, MRBMessage);
        Log.d("GetResponce" , String.format("MrbStatus[%d] MRBMessage[%s]", MrbStatus[0], MRBMessage[0]));
        return MrbStatus[0];
    }
    /* ---------------------------------------------------------------------------------------------
    ＭＲＢエラー取得
    --------------------------------------------------------------------------------------------- */
    public int CheckMRB(String Responce, int[] MrbStatus, String[] MRBMessage)
    {
        // 出力引数クリア
        MrbStatus[0] = 0;
        MRBMessage[0] = "";

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
            Log.d("MRBErrorCode p1, p2" , String.format("p1[%d] p2[%d]",p1, p2));
            String errcode = Responce.substring(p1+1, p2);
            MrbStatus[0] = Integer.parseInt(errcode);

            if ((p1 = Responce.indexOf("MG_V_ErrorText =", 0)) == -1)
                break;
            if ((p1 = Responce.indexOf("\"", p1)) == -1)
                break;
            if ((p2 = Responce.indexOf("if (typeof user.MG_Server==\"object\")", p1+1)) == -1)
                break;
            if ((p2 = Responce.lastIndexOf("\"", p2+1)) == -1)
                break;
            Log.d("MRBErrorText p1, p2" , String.format("p1[%d] p2[%d]",p1, p2));
            MRBMessage[0] = Responce.substring(p1+1, p2);
            Log.d("MRBError" , String.format("code[%d] msg[%s]", MrbStatus[0], MRBMessage[0]));
        }while(false);
        return 0;
    }
}
