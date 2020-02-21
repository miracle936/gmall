package com.alibaba.gmallpublisher.service;

import java.util.Map;

/**
 * @Author:ygq
 * @Date:Created in 15:35 2020/2/20
 * @Description:
 */
public interface PublisherService {

    //获得当日日活
    public int getDauTotal(String date);

    //活的分时日活数
    public Map getDauHours(String date);

    //查询当日交易额总数
    public Double getOrderAmount(String date);

    //查询当日交易额分时明细
    public Map getOrderAmountHour(String date);

    //获得当天的新用户
    public Integer getNewUser(String date);

}
