package com.alibaba.util;

/**
 * @Author:ygq
 * @Date:Created in 20:19 2020/2/17
 * @Description:
 */
public class RanOpt<T> {

    T value;
    int weight;

    public RanOpt(T value, int weight) {
        this.value = value;
        this.weight = weight;
    }

    public T getValue() {
        return value;
    }

    public int getWeight() {
        return weight;
    }

}
