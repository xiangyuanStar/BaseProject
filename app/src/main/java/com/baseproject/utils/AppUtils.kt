package com.jisuliuhecai.utils

import android.os.Build
import android.view.View
import com.blankj.utilcode.util.BarUtils

/**
 * Created by yuanxiang on 2019/1/10.
 */
class AppUtils {
    /**
     * 全屏显示，隐藏虚拟按钮
     *
     * @param view
     */
    companion object {
        fun fullScreenImmersive(view: View) {
            if (!BarUtils.isSupportNavBar()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_FULLSCREEN)
                    view.systemUiVisibility = uiOptions
                }
            }
        }
    }
}
