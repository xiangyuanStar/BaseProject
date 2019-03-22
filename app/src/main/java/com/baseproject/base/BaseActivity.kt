package com.baseproject.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.baseproject.R
import com.baseproject.core.config.ActivityContainer
import com.baseproject.utils.ToastUtils
import com.blankj.utilcode.util.BarUtils
import java.util.*

/**
 * Created by yuanxiang on 2019/1/6.
 */
abstract class BaseActivity : FragmentActivity() {
    private var isExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 隐藏状态栏，如果不添加这行代码，在跳转到下一个activity时候，状态栏会闪一下，没有实现无缝全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (!BarUtils.isSupportNavBar()) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            window.decorView.setOnSystemUiVisibilityChangeListener {
                var uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                uiOptions = if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions or 0x00001000
                } else {
                    uiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE
                }
                window.decorView.systemUiVisibility = uiOptions
            }
        }

        setContentView(R.layout.activity_base)
        window.setBackgroundDrawable(null)
        ActivityContainer.getInstance().addActivity(this)
        showFragment(setFragment())
    }

    /**
     * 填充内容
     */
    private fun inflateContentView(layoutResource: Int) {
        val contentView = this.findViewById<View>(R.id.contentView) as ViewGroup
        this.layoutInflater.inflate(layoutResource, contentView)
    }

    protected abstract fun setFragment(): Fragment

    fun nextActivity(activity: Class<out Activity>) {
        this.nextActivity(activity, null)
    }

    fun nextActivity(activity: Class<out Activity>, intent: Intent?) {
        val intent1 = if (intent == null) Intent() else Intent(intent)
        intent1.setClass(this, activity)
        this.startActivity(intent1)
    }

    private fun showFragment(fragment: Fragment) {
        inflateContentView(R.layout.activity_fragment_contanier)
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.contanier, fragment)
        ft.commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        @SuppressLint("RestrictedApi") val lf = fm.fragments
        if (lf.size <= 0) {
            finish()
            return
        }
        if (fm.backStackEntryCount == 0 && lf.size > 0) {
            if (lf[0].isHidden) {
                for (i in lf.size - 1 downTo 1) {
                    if (lf[i] == null) continue
                    fm.beginTransaction().remove(lf[i]).commit()
                }
                fm.beginTransaction().show(lf[0]).commit()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityContainer.getInstance().removeActivity(this)
    }

    /**
     * 双击退出 </br>
     * 在需要双击退出的activity里面重新 onKeyDown()，并设置返回值为false
     */
    protected fun exitByDoubleClick(keyCode: Int) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true
                ToastUtils.showMessage("再按一次退出程序")
                val tExit = Timer()
                tExit.schedule(object : TimerTask() {
                    override fun run() {
                        isExit = false//取消退出
                    }
                }, 2000)// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
            } else {
                finish()
                System.exit(0)
            }
        }
    }
}