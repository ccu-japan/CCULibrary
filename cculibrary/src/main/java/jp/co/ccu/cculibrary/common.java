package jp.co.ccu.cculibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import static android.content.ContentValues.TAG;

/* =================================================================================================
    共通関数
   ===============================================================================================*/
public class Common {
    /* ---------------------------------------------------------------------------------------------
    トースト表示
    --------------------------------------------------------------------------------------------- */
    public void ShowToast(Context context, String msg) {

        Toast.makeText(context, msg,Toast.LENGTH_SHORT).show();
    }
    /* ---------------------------------------------------------------------------------------------
    ダイアログ表示
    --------------------------------------------------------------------------------------------- */
    public void ShowDialog(Context context, String title , String msg)
    {
        // androidのダイアログを使いこなそう
        // https://allabout.co.jp/gm/gc/80655/2/

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        builder.show();
    }

    
    /* ---------------------------------------------------------------------------------------------
    終了確認表示
    --------------------------------------------------------------------------------------------- */
    public void ExitDialog(Context context, String title , String msg)
    {
        // androidのダイアログを使いこなそう
        // https://allabout.co.jp/gm/gc/80655/2/

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
        builder.show();
    }
    /* ---------------------------------------------------------------------------------------------
    IPアドレス取得
    --------------------------------------------------------------------------------------------- */
    public boolean isWifiIp(byte[] ipAddr)
    {
        try{
            InetAddress addr = InetAddress.getLocalHost();
            InetAddress addr2 = InetAddress.getLocalHost();
//            if (addr.equals(addr2)) Log.d("","addrとaddr2は同じインスタンス");
//            Log.d("Local Host Name" , addr.getHostName());
//            Log.d("IP Address", addr.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }
    /* ---------------------------------------------------------------------------------------------
    設定値取得
    --------------------------------------------------------------------------------------------- */
    public boolean PreferencesGet(Context context, String Key, String [] Value)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Value[0] = sharedPref.getString(Key, "");
        return false;
    }
    /* ---------------------------------------------------------------------------------------------
    設定値保存
    --------------------------------------------------------------------------------------------- */
    public boolean PreferencesPut(Context context, String Key, String Value)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Key, Value);
        editor.commit();
        return false;
    }
}
