package com.baseproject.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import com.jisuliuhecai.utils.AppUtils;

/**
 * 重写 show() 方法，实现弹出 dialog 隐藏虚拟按键
 * Created by xiaoyuan on 2019/1/27.
 */
public class MyDialog extends Dialog {
    public MyDialog(Context context) {
        super(context);
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public MyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void show() {
        // Dialog 在初始化时会生成新的 Window，先禁止 Dialog Window 获取焦点，等 Dialog 显示后对 Dialog Window 的 DecorView 设置 setSystemUiVisibility ，接着再获取焦点。 这样表面上看起来就没有退出沉浸模式。
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        super.show();
        AppUtils.Companion.fullScreenImmersive(getWindow().getDecorView());

        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }
}
