package com.alibaba.gmallpublisher.mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author:ygq
 * @Date:Created in 15:36 2020/2/20
 * @Description:
 */
public interface DauMapper {

    public Integer selectDauTotal(String date);

    public List<Map> selectDauTotalHourMap(String date);

}
