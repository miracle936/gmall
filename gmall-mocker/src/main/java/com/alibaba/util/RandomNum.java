package com.alibaba.util;

import java.util.Random;

/**
 * @Author:ygq
 * @Date:Created in 20:23 2020/2/17
 * @Description:
 */
public class RandomNum {

    public static int getRandInt(int fromNum, int toNum) {
        return fromNum + new Random().nextInt(toNum - fromNum + 1);
    }

}
