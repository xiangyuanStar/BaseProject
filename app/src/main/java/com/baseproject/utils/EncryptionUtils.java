package com.baseproject.utils;

import android.text.TextUtils;
import android.util.Base64;
import com.baseproject.core.remote.HttpConfigs;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.GsonUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author yuanxiang.
 * @date 2019/3/8.
 */
public class EncryptionUtils {
    public static String encryptionRequestKeyParam(Map<String, String> param) {
        String str = GsonUtils.toJson(param);
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        byte[] encrypted = new byte[0];
        String s = "";
        try {
            // 获取字节数组
            byte[] raw = HttpConfigs.AES_KEY.getBytes(HttpConfigs.UTF_8);
            // 构造一个密钥
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            // 算法/模式/补码方式
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encrypted = cipher.doFinal(str.getBytes(HttpConfigs.UTF_8));
            s = Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return encrypted == null ? "" : s;
    }

    public static String encryptionRequestKeyAutograph(Map<String, String> autograph) {
        String str = GsonUtils.toJson(autograph);
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String sb = str + HttpConfigs.MD5_KEY;
        return EncryptUtils.encryptMD5ToString(sb).toLowerCase();
    }
}