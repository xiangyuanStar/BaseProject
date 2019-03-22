package com.baseproject.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

/**
 * Created by yuanxiang on 2019/1/23.
 */
public class ImageLoaderUtils {
    public static void loadImageView(Context context, Bitmap bitmap, ImageView imageView) {
        Glide.with(context.getApplicationContext()).asBitmap().load(bitmap).into(imageView);
    }
}