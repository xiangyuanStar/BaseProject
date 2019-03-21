package com.baseproject.core.remote;

import android.app.Activity;
import com.baseproject.bean.GeneralBean;
import com.baseproject.utils.ToastUtils;
import com.baseproject.widget.MyProgressDialog;
import com.blankj.utilcode.util.GsonUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.jetbrains.annotations.NotNull;

/**
 * @author xiaoyuan.
 * @date 2019/3/14.
 */
public abstract class CommonlyObserver<T> implements Observer<T> {
    private Activity activity;
    private MyProgressDialog progressDialog;
    private boolean isShowDialog = true;
    private String loadingText = "加载中";
    private Disposable mDisposable;

    public CommonlyObserver(Activity activity) {
        this.activity = activity;
    }

    /**
     * @param activity
     * @param isShowDialog 是否显示加载对话框，默认显示
     */
    public CommonlyObserver(Activity activity, boolean isShowDialog) {
        this.activity = activity;
        this.isShowDialog = isShowDialog;
    }

    /**
     * @param activity
     * @param loadingText 加载对话框提示内容
     */
    public CommonlyObserver(Activity activity, String loadingText) {
        this.activity = activity;
        this.loadingText = loadingText;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (isShowDialog) {
            progressDialog = new MyProgressDialog(activity, loadingText);
        }
    }

    @Override
    public void onNext(T t) {
        try {
            if (t == null) {
                failure("暂无数据");
            } else {
                String json = GsonUtils.toJson(t);
                GeneralBean generalBean = GsonUtils.fromJson(json, GeneralBean.class);
                String code = String.valueOf(generalBean.getCode());

                // TODO: 2019/3/20  根据返回的code，处理其它情况，比如未登录，跳转到登录界面

                // 成功
                if ("".equals(code)) {
                    success(t);
                } else {
                    failure(generalBean.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dissmissDialog();
    }

    @Override
    public void onError(Throwable e) {
        failure(e.getMessage());
        dissmissDialog();
    }

    @Override
    public void onComplete() {
        dissmissDialog();
    }

    private void dissmissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 关闭订阅
     */
    private void closeSubscription() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    protected abstract void success(@NotNull T t);

    protected void failure(String failureMsg) {
        ToastUtils.showMessage(failureMsg);
    }
}
