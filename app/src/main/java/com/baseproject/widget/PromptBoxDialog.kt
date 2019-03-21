package com.baseproject.widget

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import com.baseproject.R
import com.baseproject.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_showtext.*

/**
 * 提示对话框
 * Created by xiaoyuan on 2019/1/29.
 */
@SuppressLint("ValidFragment")
class PromptBoxDialog @SuppressLint("ValidFragment") constructor(showTextString: String) : BaseDialog() {
    private val showTextString = showTextString

    override fun inflateContentView(): Int {
        return R.layout.dialog_showtext
    }

    override fun initData() {
        hideTitleContextView(true)
        tvInfo.text = showTextString
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null && dialog.window != null) {
            val dm = DisplayMetrics()
            dialog.window.windowManager.defaultDisplay.getMetrics(dm)
            dialog.window.setLayout((dm.widthPixels * 0.6).toInt(), (dm.heightPixels * 0.65).toInt())
        }
    }

    override fun initListener() {

    }
}