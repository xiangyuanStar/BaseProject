package com.baseproject.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.baseproject.R
import com.baseproject.widget.MyDialogFragment
import kotlinx.android.synthetic.main.dialog_base.*
import org.greenrobot.eventbus.EventBus

/**
 * Created by yuanxiang on 2019/1/24.
 */
abstract class BaseDialog : MyDialogFragment() {
    private var getSizeTag: Boolean = true
    private var isRegisterEventBus = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_base, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 去掉白色背景
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 去掉标题
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        // 设置样式
//        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.custom_dialog)
        // 加载子布局
        dialog.layoutInflater.inflate(inflateContentView(), flDialogBodyView)

        if (isRegisterEventBus) {
            EventBus.getDefault().register(this)
        }

        // 设置点击外部不消失
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        ivDialogClose.setOnClickListener {
            dismiss()
        }

        initData()
        initListener()
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null && dialog.window != null && getSizeTag) {
            val dm = DisplayMetrics()
            dialog.window.windowManager.defaultDisplay.getMetrics(dm)
            dialog.window.setLayout((dm.widthPixels * 0.75).toInt(), (dm.heightPixels * 0.8).toInt())
            getSizeTag = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRegisterEventBus) {
            EventBus.getDefault().unregister(this)
        }
    }

    /**
     * 填充内容
     */
    protected abstract fun inflateContentView(): Int

    protected abstract fun initData()

    protected abstract fun initListener()

    protected fun setEvenBusRegister(isRegisterEventBus: Boolean) {
        this.isRegisterEventBus = isRegisterEventBus
    }

    /**
     * 设置标题
     */
    protected fun setTitle(title: String) {
        tvDialogTitle.text = title
    }

    protected fun setCanceledOnTouchOutside(cancel: Boolean) {
        dialog.setCancelable(cancel)
        dialog.setCanceledOnTouchOutside(cancel)
    }

    protected fun hideTitleView(hide: Boolean) {
        if (hide) {
            rlDialogTitleView.visibility = View.GONE
        } else {
            rlDialogTitleView.visibility = View.VISIBLE
        }
    }

    protected fun hideCloseButton(hide: Boolean) {
        if (hide) {
            ivDialogClose.visibility = View.GONE
        } else {
            ivDialogClose.visibility = View.VISIBLE
        }
    }

    protected fun hideTitleContextView(hide: Boolean) {
        if (hide) {
            tvDialogTitle.visibility = View.GONE
        } else {
            tvDialogTitle.visibility = View.VISIBLE
        }
    }
}