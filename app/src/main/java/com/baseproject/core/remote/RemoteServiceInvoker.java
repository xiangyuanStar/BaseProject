package com.baseproject.core.remote;

import android.app.Activity;
import com.baseproject.constant.Constants;
import com.baseproject.core.config.SystemApplication;
import com.baseproject.ui.callback.ProgressDialogCallback;
import com.baseproject.utils.EncryptionUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.baseproject.constant.Constants.ENCRYPTION_POST_REQUEST;
import static com.baseproject.core.remote.API.APP_BASE_URL;
import static com.baseproject.core.remote.HttpConfigs.*;

/**
 * 封装OkGo
 *
 * @author yuanxiang.
 * @date 2019/3/4.
 */
public class RemoteServiceInvoker {

    private volatile static RemoteServiceInvoker remoteServiceInvoker;

    private RemoteServiceInvoker() {
    }

    public static RemoteServiceInvoker invoker() {
        if (remoteServiceInvoker == null) {
            synchronized (RemoteServiceInvoker.class) {
                if (remoteServiceInvoker == null) {
                    remoteServiceInvoker = new RemoteServiceInvoker();
                }
            }
        }
        return remoteServiceInvoker;
    }

    private boolean mIsToken = true;

    /**
     * post 请求
     * 无参请求
     *
     * @param activity
     * @param url
     * @param listener
     */
    public void requestPost(Activity activity, String url, OnRequestDataListener listener) {
        requestPost(activity, url, null, ENCRYPTION_POST_REQUEST, listener);
    }

    /**
     * post 请求
     * 有参请求
     *
     * @param activity
     * @param url
     * @param map
     * @param listener
     */
    public void requestPost(Activity activity, String url, Map<String, String> map, OnRequestDataListener listener) {
        requestPost(activity, url, map, ENCRYPTION_POST_REQUEST, true, "加载中", listener);
    }

    /**
     * post 请求
     *
     * @param activity
     * @param url
     * @param map
     * @param isShowProgressDialog
     * @param dialogText
     * @param listener
     */
    public void requestPost(Activity activity, String url, Map<String, String> map, boolean isShowProgressDialog, String dialogText, OnRequestDataListener listener) {
        requestPost(activity, url, map, ENCRYPTION_POST_REQUEST, isShowProgressDialog, dialogText, listener);
    }

    /**
     * post 请求
     *
     * @param activity
     * @param url
     * @param map
     * @param encryption 加密
     * @param listener
     */
    public void requestPost(Activity activity, String url, Map<String, String> map, boolean encryption, OnRequestDataListener listener) {
        requestPost(activity, url, map, encryption, true, "加载中", listener);
    }

    /**
     * post 请求
     *
     * @param activity
     * @param url
     * @param map
     * @param encryption           加密
     * @param isShowProgressDialog 加载提示对话框
     * @param dialogText           提示内容
     * @param listener
     */
    public void requestPost(final Activity activity, String url, Map<String, String> map, boolean encryption, boolean isShowProgressDialog, String dialogText, final OnRequestDataListener listener) {
        OkGo.<String>post(APP_BASE_URL + url)
                .params(encryption ? getRequestParams(map, mIsToken) : new HttpParams(REQUEST_KEY_PARAM, GsonUtils.toJson(map)))
                .tag(SystemApplication.getInstance())
                .execute(new ProgressDialogCallback(isShowProgressDialog, activity, dialogText.isEmpty() ? "加载中" : dialogText) {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject json = new JSONObject(response.body());
                            String code = json.optString(CODE);

                            // TODO: 2019/3/20  根据返回的code，处理其它情况，比如未登录，跳转到登录界面

                            if ("".equals(code)) {
                                listener.requestData(json);
                            } else {
                                ToastUtils.showLong(json.optString(MESSAGE));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 加密请求参数
     *
     * @param map
     * @param isToken
     * @return
     */
    private HttpParams getRequestParams(Map<String, String> map, boolean isToken) {
        if (map == null) {
            map = new HashMap<>();
        }
        HttpParams httpParams = new HttpParams();

        String userToken = SPStaticUtils.getString(Constants.KEY_TOKEN);
        if (isToken && !userToken.isEmpty()) {
            map.put(HttpConfigs.TOKEN, userToken);
        }
        httpParams.put(REQUEST_KEY_PARAM, EncryptionUtils.encryptionRequestKeyParam(map));
        httpParams.put(REQUEST_KEY_AUTOGRAPH, EncryptionUtils.encryptionRequestKeyAutograph(map));
        return httpParams;
    }

    /**
     * 请求结果回调接口
     */
    public interface OnRequestDataListener {
        void requestData(JSONObject jsonObject);
    }
}