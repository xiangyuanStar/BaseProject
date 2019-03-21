package com.baseproject.ui.callback;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

/**
 * @author xiaoyuan.
 * @date 2019/3/10.
 */
public class ProgressDialogCallback extends StringCallback {

    private ProgressDialog dialog;

    protected ProgressDialogCallback(boolean is, Context context, String text) {
        try {
            if (null != context && is) {
                dialog = new ProgressDialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Response<String> response) {
    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        try {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
