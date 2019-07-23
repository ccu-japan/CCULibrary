package jp.co.ccu.cculibrary;

import android.graphics.Color;
import android.util.Log;
/* =================================================================================================
    ICCard2タイプHTTPプロトコル解析
   ===============================================================================================*/
public class ICCard2_Setting {
    public String windowtitle = "Fellowship (ICカード打刻処理)";				        		    // ウィンドウタイトル						WindowTitle
    public String guidance_wait = "ICカードリーダーに、ICカードをかざしてください。";	        // 待受ガイダンス							GuidanceWait
    public int guidance_wait_color = Color.argb(255,0,0,0);          // 待受ガイダンス色							GuidanceWaitColor
    public boolean guidance_wait_flush = false;		        									// 待受ガイダンス点滅						GuidanceWaitFlush
    public boolean guidance_wait_bold = true;					        							// 待受ガイダンス太字						GuidanceWaitBold
    public String guidance_communicate = "通信中...";					        				    // 通信中ガイダンス							GuidanceCommunicate
    public int guidance_communicate_color = Color.argb(255,0,0,0);  // 通信中ガイダンス色						GuidanceCommunicateColor
    public boolean guidance_communicate_bold = true;		        								// 通信中ガイダンス太字						GuidanceCommunicateBold
    public int message_nomal_show_second = 5;						        					    // 打刻正常メッセージ表示時間_秒数			MessageNomalShowSecond
    public int message_nomal_color = Color.argb(255,0,0,0);     		// 打刻正常メッセージ色						MessageNomalColor
    public boolean message_nomal_flush = false;					        						// 打刻正常メッセージ点滅					MessageNomalFlush
    public boolean message_nomal_bold = true;								        				// 打刻正常メッセージ太字					MessageNomalBold
    public int message_abnomal_show_second = 15;									        	    // 打刻異常メッセージ表示時間_秒数			MessageAbnomalShowSecond
    public int message_abnomal_color = Color.argb(255,255,0,0);		// 打刻異常メッセージ色						MessageAbnomalColor
    public boolean message_abnomal_flush = false;				        							// 打刻異常メッセージ点滅					MessageAbnomalFlush
    public boolean message_abnomal_bold = true;						        					// 打刻異常メッセージ太字					MessageAbnomalBold
    public String [] message_normal_sound = new String[6];						        		    // 打刻正常メッセージ音コード				MessageNomalSound1-6
    public String message_abnormal_sound = "";								        			    // 打刻異常メッセージ音コード				MessageAbnomalSound
    public boolean [] pushbutton_visible = new boolean[4];				        				// ボタン可視(出勤、退勤、外出、育休)		PushbuttonVisible1-4
    public boolean pushbutton_entry_visible = false;						        				// ICカード登録ボタン可視					PushbuttonEntryVisible
    public boolean cardentry_auto = false;									        				// 登録画面自動遷移							CardEntryAuto
    public boolean pushbutton_query_visible = false;							        			// 履歴ボタン可視							PushbuttonQueryVisible
    public String windowtitle_query = "Fellowship (ICカード打刻履歴)";				        	// ウィンドウタイトル(履歴)					WindowTitle_Query
    public String guidance_query = "ICカードリーダーに、ICカードをかざしてください。";	        // 履歴照会ガイダンス						GuidanceQuery
    public boolean loginid_dakoku_visible = false;		        								// ログインID打刻							LoginIDDakokuVisible
    public boolean password_active = false;						        						// ログインID打刻パスワード表示				LoginIDDakokuPasswordActive
    /* ---------------------------------------------------------------------------------------------
    レスポンス文字列解析
    --------------------------------------------------------------------------------------------- */
    public void GetStartResponce(String Response)
    {
        Response = Response.replace('|', '\t');
        String [] csv = Response.split("\t");

        if (csv[0].compareTo("0000") == 0)
        {
            int i = 0;

            windowtitle = csv[1];
            guidance_wait = csv[2];
            if (csv[3].length() == 6)
                guidance_wait_color = Color.argb(255, Integer.parseInt(csv[3].substring(0,2), 16), Integer.parseInt(csv[3].substring(2,2),16), Integer.parseInt(csv[3].substring(4,2),16));
            else
                guidance_wait_color = Color.argb(255,0,0,0);
            guidance_wait_flush = csv[4] != "0";
            guidance_wait_bold = csv[5] != "0";
            guidance_communicate = csv[6];
            if (csv[7].length() >= 6)
                guidance_communicate_color = Color.argb(255, Integer.parseInt(csv[7].substring(0,2),16), Integer.parseInt(csv[7].substring(2,2),16),Integer.parseInt(csv[7].substring(4,2),16));
            else
                guidance_communicate_color = Color.argb(255,0,0,0);
            guidance_communicate_bold = csv[8] != "0";
            message_nomal_show_second = Integer.parseInt(csv[9]);
            if (csv[10].length() >= 6)
                message_nomal_color = Color.argb(255,Integer.parseInt(csv[10].substring(0,2),16), Integer.parseInt(csv[10].substring(2,2),16),Integer.parseInt(csv[10].substring(4,2),16));
            else
                message_nomal_color = Color.argb(255,0,0,0);
            message_nomal_flush = csv[11] != "0";
            message_nomal_bold = csv[12] != "0";
            message_abnomal_show_second = Integer.parseInt(csv[13]);
            if (csv[14].length() >= 6)
                message_abnomal_color = Color.argb(255,Integer.parseInt(csv[14].substring(0,2),16), Integer.parseInt(csv[14].substring(2,2),16),Integer.parseInt(csv[14].substring(4,2),16));
            else
                message_abnomal_color = Color.argb(255,0,0,0);
            message_abnomal_flush = csv[15] != "0";
            message_abnomal_bold = csv[16] != "0";
            message_abnormal_sound = csv[17];
            pushbutton_visible[0] = csv[18] != "0";
            message_normal_sound[0] = csv[19];
            pushbutton_visible[2] = csv[20] != "0";
            message_normal_sound[1] = csv[21];
            message_normal_sound[2] = csv[22];
            pushbutton_visible[3] = csv[23] != "0";
            message_normal_sound[3] = csv[24];
            message_normal_sound[4] = csv[25];
            pushbutton_visible[1] = csv[26] != "0";
            message_normal_sound[5] = csv[27];
            pushbutton_entry_visible = csv[28] != "0";
            cardentry_auto = csv[29] != "0";
            pushbutton_query_visible = csv[30] != "0";
            windowtitle_query = csv[31];
            guidance_query = csv[32];
            loginid_dakoku_visible = csv[33] != "0";
            password_active = csv[34] != "0";
            // システム日時設定
//            DateTime dt = DateTime.Parse(response[35]);
//            Microsoft.VisualBasic.DateAndTime.TimeOfDay = dt;
//            Microsoft.VisualBasic.DateAndTime.Today = dt;
//            SaveAppSetting();
        }
        else
        {
            pushbutton_visible[0] = false;
            pushbutton_visible[1] = false;
            pushbutton_visible[2] = false;
            pushbutton_visible[3] = false;
            pushbutton_entry_visible = false;
        }
    }
    /* ---------------------------------------------------------------------------------------------
    レスポンス文字列解析結果デバック表示
    --------------------------------------------------------------------------------------------- */
    public void DebugAppSetting()
    {
        Log.d("WindowTitle",  String.format("[%s]", windowtitle));
        Log.d("GuidanceWait",  String.format("[%s]", guidance_wait));
        Log.d("GuidanceWaitColor",  String.format("[%x]", guidance_wait_color));
        Log.d("GuidanceWaitFlush", String.format("[%b]",guidance_wait_flush));
        Log.d("GuidanceWaitBold", String.format("[%b]",guidance_wait_bold));
        Log.d("GuidanceCommunicate", String.format("[%s]",guidance_communicate));
        Log.d("GuidanceCommunicateColor", String.format("[%x]",guidance_communicate_color));
        Log.d("GuidanceCommunicateBold", String.format("[%b]",guidance_communicate_bold));
        Log.d("MessageNomalShowSecond", String.format("[%d]",message_nomal_show_second));
        Log.d("MessageNomalColor", String.format("[%x]",message_nomal_color));
        Log.d("MessageNomalFlush", String.format("[%b]",message_nomal_flush));
        Log.d("MessageNomalBold", String.format("[%b]",message_nomal_bold));
        Log.d("MessageAbnomalShowSecond", String.format("[%d]",message_abnomal_show_second));
        Log.d("MessageAbnomalColor", String.format("[%x]",message_abnomal_color));
        Log.d("MessageAbnomalFlush", String.format("[%b]",message_abnomal_flush));
        Log.d("MessageAbnomalBold", String.format("[%b]",message_abnomal_bold));
        Log.d("MessageNomalSound0", String.format("[%s]",message_normal_sound[0]));
        Log.d("MessageNomalSound1", String.format("[%s]",message_normal_sound[1]));
        Log.d("MessageNomalSound2", String.format("[%s]",message_normal_sound[2]));
        Log.d("MessageNomalSound3", String.format("[%s]",message_normal_sound[3]));
        Log.d("MessageNomalSound4", String.format("[%s]",message_normal_sound[4]));
        Log.d("MessageNomalSound5", String.format("[%s]",message_normal_sound[5]));
        Log.d("MessageAbnomalSound", String.format("[%s]",message_abnormal_sound));
        Log.d("PushbuttonVisible0", String.format("[%b]",pushbutton_visible[0]));
        Log.d("PushbuttonVisible1", String.format("[%b]",pushbutton_visible[1]));
        Log.d("PushbuttonVisible2", String.format("[%b]",pushbutton_visible[2]));
        Log.d("PushbuttonVisible3", String.format("[%b]",pushbutton_visible[3]));
        Log.d("PushbuttonEntryVisible", String.format("[%b]",pushbutton_entry_visible));
        Log.d("CardEntryAuto", String.format("[%b]",cardentry_auto));
        Log.d("PushbuttonQueryVisible", String.format("[%b]",pushbutton_query_visible));
        Log.d("WindowTitle_Query", String.format("[%s]",windowtitle_query));
        Log.d("GuidanceQuery", String.format("[%s]",guidance_query));
        Log.d("LoginIDDakokuVisible", String.format("[%b]",loginid_dakoku_visible));
        Log.d("LoginIDDakokuPasswordActive", String.format("[%b]",password_active));
    }
}
