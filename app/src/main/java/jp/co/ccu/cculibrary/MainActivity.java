package jp.co.ccu.cculibrary;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/* =================================================================================================
    Activity
   ===============================================================================================*/
public class MainActivity extends AppCompatActivity {

//    private static final int LOADER_ID = 1;
    private static final String SAVE_INSTANCE_TASK_RESULT = "info.akkuma.loader.MainActivity.SAVE_INSTANCE_TASK_RESULT";
    private static final String ARG_EXTRA_PARAM = "ARG_EXTRA_PARAM";

    private String [] Server = new String[1];
    private String [] Application = new String[1];
    private String [] APPNAME = new String[1];
    private String [] Company = new String[1];

    // アダプタを扱うための変数
    private NfcAdapter mNfcAdapter;

    private String mTaskResult;

    private TextView Time;
    private TextView Message1;
    private TextView Message2;

    Handler mHandler = new Handler();
//    boolean f_end = false;
    /* ---------------------------------------------------------------------------------------------
    onCreate
    --------------------------------------------------------------------------------------------- */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ICCardDakoku");
        toolbar.setLogo(android.R.drawable.sym_def_app_icon);

        // アダプタのインスタンスを取得
        mNfcAdapter = android.nfc.NfcAdapter.getDefaultAdapter(this);
//        Log.d("mNfcAdapter", String.format("[%b]",mNfcAdapter.isEnabled()));

        Time = findViewById(R.id.Time);
        Message1 = findViewById(R.id.Message1);
        Message2 = findViewById(R.id.Message2);
        Message1.setText("");
        Message2.setText("");

//        new Common().PreferencesPut(this, "Server", "http://192.168.0.135");
//        new Common().PreferencesPut(this, "Application", "uni18Scripts/MGrqispi018.dll");
//        new Common().PreferencesPut(this, "Company", "001");
//        new Common().PreferencesPut(this,"APPNAME","Fellowship");
        new Common().PreferencesGet(this, "Server", Server);
        new Common().PreferencesGet(this, "Application", Application);
        new Common().PreferencesGet(this, "APPNAME", APPNAME);
        new Common().PreferencesGet(this, "Company", Company);
//        Log.d("Server", Server[0]);
//        Log.d("Application", Application[0]);
//        Log.d("APPNAME", APPNAME[0]);
//        Log.d("Company", Company[0]);

        if (mNfcAdapter.isEnabled()){
            if (savedInstanceState != null) {
                mTaskResult = savedInstanceState.getString(SAVE_INSTANCE_TASK_RESULT);
            }

            if (mTaskResult != null) {
//             TextView textView = (TextView) findViewById(R.id.text_view);
//                textView.setText(mTaskResult);
            }
            // Activityが結果を持っていない場合ロードを行う
            //
            // 結果を保持しない性質のリクエストの場合
            if (mTaskResult == null) {
                Bundle args = new Bundle();
                args.putString(ARG_EXTRA_PARAM, "サンプルパラメータ");
                getSupportLoaderManager().initLoader(1, args, mCallback);
            }
        }
        else{
            Message2.setText("NFCが無効です。設定でNFCを有効にしてアプリを再度起動してください。");
            Message2.setTextColor(Color.RED);
        }

        Timer mTimer = new Timer(true);
        mTimer.schedule(new TimerTask(){
            @Override
            public void run() {
                // mHandlerを通じてUI Threadへ処理をキューイング
                mHandler.post( new Runnable() {
                    public void run() {
                        Calendar cal = Calendar.getInstance();       //カレンダーを取得
                        SimpleDateFormat fhms = new SimpleDateFormat ("MM月dd日 HH:mm:ss", Locale.JAPANESE);
                        Time.setText(fhms.format(cal.getTime()));
                    }
                });
            }
        }, 0, 1000);

        /*
        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                // UIスレッド
                Calendar cal = Calendar.getInstance();       //カレンダーを取得
                SimpleDateFormat fhms = new SimpleDateFormat ("yyyy/MM/dd HH:mm  ss", Locale.JAPANESE);
                Time.setText(fhms.format(cal.getTime()));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(r);
        */
    }
    /* ---------------------------------------------------------------------------------------------
    onCreateOptionsMenu
    --------------------------------------------------------------------------------------------- */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /* ---------------------------------------------------------------------------------------------
    onOptionsItemSelected
    --------------------------------------------------------------------------------------------- */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_settings1_1:
                break;
            case R.id.action_settings1_2:
                //処理2
                break;
        }
        return false;
    }
    /* ---------------------------------------------------------------------------------------------
    onSaveInstanceState
    --------------------------------------------------------------------------------------------- */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_INSTANCE_TASK_RESULT, mTaskResult);
    }
    /** --------------------------------------------------------------------------------------------
     * onResumeメソッド(Activityが表示された時)
     * @param args 使用しない
    -------------------------------------------------------------------------------------------- */
    @Override
    protected void onResume(){
        super.onResume();

        // NFCがかざされたときの設定
        Intent intent = new Intent(this, this.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // ほかのアプリを開かないようにする
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }
    /** --------------------------------------------------------------------------------------------
     * onPauseメソッド(Activityの初期化)
     * @param Bundle savedInstanceState
    -------------------------------------------------------------------------------------------- */
    @Override
    protected void onPause(){
        super.onPause();

        // Activityがバックグラウンドになったときは、受け取らない
        mNfcAdapter.disableForegroundDispatch(this);

    }
    /** --------------------------------------------------------------------------------------------
     *
     * @param intent
    -------------------------------------------------------------------------------------------- */
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        try {
            // NFCのUIDを取得
            byte[] uid = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            // 表示
            String hex = "";
            for (int idx = 0; idx < uid.length; idx++) {
                String buff = "00" + Integer.toHexString(uid[idx] & 0xff).toUpperCase();
                hex += buff.substring(buff.length() - 2);
            }
            Toast.makeText(this, hex, Toast.LENGTH_SHORT).show();

            Bundle args = new Bundle();
            args.putString("UID", hex);
            getSupportLoaderManager().initLoader(2, args, mCallback);
        }
        catch (Exception e)
        {
            Log.d("onNewIntent", "", e);
        }
    }
    /* ---------------------------------------------------------------------------------------------
    LoaderCallbacks
    --------------------------------------------------------------------------------------------- */
    private final LoaderManager.LoaderCallbacks<HttpPostResult> mCallback = new LoaderManager.LoaderCallbacks<HttpPostResult>() {
        /* -----------------------------------------------------------------------------------------
        新しいローダを作成
        ----------------------------------------------------------------------------------------- */
        //  http://tomoyamkung.net/2014/02/24/android-loader-execute/
        @Override
        public Loader<HttpPostResult> onCreateLoader(int id, Bundle args) {
            String extraParam;
            if (args != null) {
                extraParam = args.getString(ARG_EXTRA_PARAM);
            }

            String url = String.format("%s/%s", Server[0], Application[0]);

            Map<String, String> map = new HashMap<String,String>();
            map.put("APPNAME",APPNAME[0]);
            if (id==1) {
                map.put("PRGNAME", "SCStart");
                map.put("ARGUMENTS","-A"+Company[0]);
            } else {
                map.put("PRGNAME", "SCDakoku");
                map.put("ARGUMENTS",String.format("-A%s,A%s",Company[0], args.getString("UID")));
            }
            return new HttpPost(MainActivity.this, url, map);
        }
        /* -----------------------------------------------------------------------------------------
        ロードを完了した
        ----------------------------------------------------------------------------------------- */
        @Override
        public void onLoadFinished(Loader<HttpPostResult> loader, HttpPostResult data) {
            String [] Error = new String[1];
            int rtn = data.ErrorHttpPostResult(Error);

            if (rtn == 0)
            {
                getSupportLoaderManager().destroyLoader(loader.getId());
//                Log.d("onLoadFinished" , data.getData().toString());
                // 結果は data に出てくる
                mTaskResult = data.getData().toString();

                ICCard_Setting set1 = new ICCard_Setting();
                set1.GetStartResponce(data.getData().toString());
//                set1.DebugAppSetting();

                Message1.setText(set1.WaitingMessage);
                Message2.setText(set1.PunchingMessage);
                if (set1.Result != 0)
                    Message2.setTextColor(Color.RED);
                if (set1.PunchingDispSec != 0){
                    Timer mTimer = new Timer(true);
                    mTimer.schedule(new TimerTask(){
                        @Override
                        public void run() {
                            // mHandlerを通じてUI Threadへ処理をキューイング
                            mHandler.post( new Runnable() {
                                public void run() {
                                    Message2.setText("");
                                }
                            });
                        }
                    },  set1.PunchingDispSec * 1000);

                }
            }
            else {
                Message2.setText(Error[0]);
                Message2.setTextColor(Color.RED);
            }
        }
        @Override
        public void onLoaderReset(Loader<HttpPostResult> loader) {
            // do nothing
        }
    };
}
