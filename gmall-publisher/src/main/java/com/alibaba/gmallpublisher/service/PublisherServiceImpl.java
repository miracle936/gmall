package com.alibaba.gmallpublisher.service;

import com.alibaba.gmallpublisher.mapper.DauMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:ygq
 * @Date:Created in 15:35 2020/2/20
 * @Description:
 */
@Service
public class PublisherServiceImpl implements PublisherService {

    @Autowired
    private DauMapper dauMapper;

    @Override
    public int getDauTotal(String date) {

        return dauMapper.selectDauTotal(date);

    }

    @Override
    public Map getDauHours(String date) {

        HashMap<String, Long> map = new HashMap<>();

        List<Map> maps = dauMapper.selectDauTotalHourMap(date);

        for (Map map1 : maps) {

            map.put(map1.get("LH").toString(), (Long) map1.get("CT"));

        }

        return map;
    }
}
