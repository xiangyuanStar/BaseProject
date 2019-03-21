package com.baseproject.utils;

import java.util.Random;

/**
 * Created by xiaoyuan on 2019/1/18.
 */
public class OtherUtils {

    /**
     * 获取随机数
     *
     * @param startNumber
     * @param endNumber   不包含
     * @return
     */
    public static int getRandomNumber(int startNumber, int endNumber) {
        Random random = new Random();
        return random.nextInt(endNumber) % (endNumber - startNumber + 1) + startNumber;
    }
}