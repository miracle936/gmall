package com.alibaba.gmallpublisher.mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author:ygq
 * @Date:Created in 20:05 2020/2/21
 * @Description:
 */
public interface OrderMapper {

    //查询当日交易额总数
    public Double selectOrderAmountTotal(String date);

    //查询当日交易额分时明细
    public List<Map> selectOrderAmountHourMap(String date);

}
