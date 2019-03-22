package com.baseproject.core.config;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import com.baseproject.utils.LogUtils;
import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.PathUtils;
import me.jessyan.autosize.AutoSizeConfig;
import rx_activity_result2.RxActivityResult;

import java.io.File;

/**
 * Created by yuanxiang on 2019/2/9.
 */
public class SystemApplication extends Application {
    private volatile static SystemApplication instance;

    public SystemApplication() {
    }

    public static SystemApplication getInstance() {
        if (instance == null) {
            synchronized (SystemApplication.class) {
                if (instance == null) {
                    instance = new SystemApplication();
                }
            }
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AutoSizeConfig.getInstance()
                // 如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
                .setExcludeFontScale(false)
                .setLog(true).setBaseOnWidth(true);

        LogUtils.init(true);

        // 设置全局的缓存路径
        CacheDiskStaticUtils.setDefaultCacheDiskUtils(CacheDiskUtils.getInstance(new File(PathUtils.getExternalAppFilesPath(), "cacheUtils")));

        RxActivityResult.register(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
