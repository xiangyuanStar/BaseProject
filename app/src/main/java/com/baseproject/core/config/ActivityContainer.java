package com.baseproject.core.config;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity管理器
 * Created by xiaoyuan on 2019/1/6.
 */
public class ActivityContainer {
    private static List<Activity> activityStack = new ArrayList<>();

    private ActivityContainer() {
    }

    private volatile static ActivityContainer instance = null;

    public static ActivityContainer getInstance() {
        if (instance == null) {
            synchronized (ActivityContainer.class) {
                if (instance == null) {
                    instance = new ActivityContainer();
                }
            }
        }
        return instance;
    }

    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }

    /**
     * @param activity
     */
    public void finishOtherActivity(Activity activity) {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i) && activityStack.get(i) != activity) {
                activityStack.get(i).finish();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
}
