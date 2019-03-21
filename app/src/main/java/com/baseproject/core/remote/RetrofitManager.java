package com.baseproject.core.remote;

import com.baseproject.constant.Constants;
import com.baseproject.utils.EncryptionUtils;
import com.baseproject.utils.HttpLog;
import com.baseproject.utils.LogUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.baseproject.constant.Constants.ENCRYPTION_POST_REQUEST;
import static com.baseproject.core.remote.HttpConfigs.REQUEST_KEY_AUTOGRAPH;
import static com.baseproject.core.remote.HttpConfigs.REQUEST_KEY_PARAM;

/**
 * 加密参考：http://www.canhuah.com/Retrofit%E7%BB%99%E5%8F%82%E6%95%B0%E7%BB%9F%E4%B8%80%E5%8A%A0%E5%AF%86.html
 *
 * @author xiaoyuan.
 * @date 2019/3/13.
 */
public class RetrofitManager {
    private volatile static RetrofitManager instance;
    private OkHttpClient mOkHttpClient;

    private RetrofitManager() {
    }

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    /**
     * 获取ApiService
     *
     * @return 所有apiService
     */
    public ApiService getApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.APP_BASE_URL)
                .client(okhttpclient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

    /**
     * 初始化okhttpclient.
     *
     * @return okhttpClient
     */
    private OkHttpClient okhttpclient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public Response intercept(@NotNull Chain chain) throws IOException {
                            if (ENCRYPTION_POST_REQUEST) {
                                Request request = chain.request();
                                String method = request.method();

                                // 用于存储原请求的参数
                                Map<String, String> params = new HashMap<>();

                                // get方法和post方法处理参数情况不一样，需要区分开来
                                if ("GET".equals(method)) {
                                    HttpUrl url = request.url();
                                    for (int i = 0; i < url.querySize(); i++) {
                                        // 取出url中？后的参数
                                        String key = url.queryParameterName(i);
                                        String value = url.queryParameterValue(i);
                                        params.put(key, value);
                                        // TODO: 2019/3/14 可以修改内容,例如给key为phone或者password的值进行RSA加密
                                    }

                                    HttpUrl.Builder newBuilder = url.newBuilder();
                                    for (Map.Entry<String, String> entry : params.entrySet()) {
                                        newBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                                    }
                                    // 添加公共参数可以单独这样写 也可以直接在上面的map中先添加再遍历map添加
                                    newBuilder.addEncodedQueryParameter("key1", "value1")
                                            .addEncodedQueryParameter("key2", "value2")
                                            .build();

                                    // 构建新的request
                                    request = request.newBuilder().url(newBuilder.build()).build();
                                } else if ("POST".equals(method)) {
                                    // 这里是表单请求（postJson形式后面讲）
                                    if (request.body() instanceof FormBody) {
                                        FormBody.Builder builder = new FormBody.Builder();
                                        FormBody oldFormBody = (FormBody) request.body();
                                        for (int i = 0; i < oldFormBody.size(); i++) {
                                            // 取出并保存原请求参数
                                            params.put(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
                                        }
                                        // 添加公共参数
                                        String userToken = SPStaticUtils.getString(Constants.KEY_TOKEN);
                                        if (!userToken.isEmpty()) {
                                            params.put(HttpConfigs.TOKEN, userToken);
                                        }

                                        LogUtils.i("未加密的请求参数：" + params.toString());
                                        // 对参数进行加密，添加加密后的参数
                                        builder.add(REQUEST_KEY_PARAM, EncryptionUtils.encryptionRequestKeyParam(params));
                                        builder.add(REQUEST_KEY_AUTOGRAPH, EncryptionUtils.encryptionRequestKeyAutograph(params));

                                        // 构建新的request
                                        request = request.newBuilder().post(builder.build()).build();
                                    }
                                }
                                return chain.proceed(request);
                            } else {
                                return chain.proceed(chain.request());
                            }
                        }
                    })
                    .addNetworkInterceptor(new HttpLoggingInterceptor(new HttpLogger()).setLevel(HttpLoggingInterceptor.Level.BODY))
                    .connectTimeout(HttpConfigs.HTTP_REQUEST_TIME_OUT, TimeUnit.SECONDS)
                    .build();
        }
        return mOkHttpClient;
    }

    private class HttpLogger implements HttpLoggingInterceptor.Logger {
        private StringBuilder mMessage = new StringBuilder();

        @Override
        public void log(@NotNull String message) {
            // 请求或者响应开始
            if (message.startsWith("--> POST")) {
                mMessage.setLength(0);
            }
            // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
            if ((message.startsWith("{") && message.endsWith("}"))
                    || (message.startsWith("[") && message.endsWith("]"))) {
                message = HttpLog.formatJson(message);
            }
            mMessage.append(message.concat("\n"));
            // 请求或者响应结束，打印整条日志
            if (message.startsWith("<-- END HTTP")) {
                LogUtils.i(mMessage.toString());
            }
        }
    }
}