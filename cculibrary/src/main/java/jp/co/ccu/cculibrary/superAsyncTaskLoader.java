package jp.co.ccu.cculibrary;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
/* =================================================================================================
    AsyncTaskLoaderの面倒な挙動を吸収するための、abstractなスーパークラス
   ===============================================================================================*/
public abstract class SuperAsyncTaskLoader<D> extends AsyncTaskLoader<D> {
    private D mResult;
    private boolean mIsStarted = false;
    /* ---------------------------------------------------------------------------------------------
    コンストラクタ
    --------------------------------------------------------------------------------------------- */
    public SuperAsyncTaskLoader(Context context) {
        super(context);
    }
    /* ---------------------------------------------------------------------------------------------
    非同期処理の開始前
    --------------------------------------------------------------------------------------------- */
    @Override
    protected void onStartLoading() {
        if (mResult != null) {
            deliverResult(mResult);
            return;
        }
        if (!mIsStarted || takeContentChanged()) {
            forceLoad();
        }
    }
    /* ---------------------------------------------------------------------------------------------
    強制ロード要求時
    --------------------------------------------------------------------------------------------- */
    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        mIsStarted = true;
    }
    /* ---------------------------------------------------------------------------------------------
    登録してあるリスナー（LoaderCallbacksを実装したクラス）に結果を送信
    --------------------------------------------------------------------------------------------- */
    @Override
    public void deliverResult(D data) {
        mResult = data;
        super.deliverResult(data);
    }
}
