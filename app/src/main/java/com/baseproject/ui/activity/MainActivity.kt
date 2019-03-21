package com.baseproject.ui.activity

import android.support.v4.app.Fragment
import com.baseproject.base.BaseActivity
import com.baseproject.ui.fragment.MainFragment

class MainActivity : BaseActivity() {
    override fun setFragment(): Fragment {
        return MainFragment()
    }
}