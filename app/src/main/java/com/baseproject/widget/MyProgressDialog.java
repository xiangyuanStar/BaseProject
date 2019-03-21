package com.baseproject.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

/**
 * 统一加载对话框样式
 *
 * @author xiaoyuan.
 * @date 2019/3/14.
 */
public class MyProgressDialog extends ProgressDialog {
    public MyProgressDialog(Context context, String text) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setMessage(text);
        show();
    }
}