package com.baseproject.utils

import com.blankj.utilcode.util.ToastUtils

/**
 * @author xiaoyuan.
 * @date 2019/3/12.
 */
object ToastUtils {
    @JvmStatic
    fun showMessage(message: String) {
        ToastUtils.showLong(message)
    }
}