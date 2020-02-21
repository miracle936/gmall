package com.alibaba.gmallpublisher.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.gmallpublisher.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author:ygq
 * @Date:Created in 15:34 2020/2/20
 * @Description:
 */
@RestController
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    /**
     * @Author:ygq
     * @Date:Created in 15:35 2020/2/20
     * @Description:获得当日日活
     */
    @GetMapping("realtime-total")
    public String realtimeHourDate(@RequestParam("date") String date) {

        int total = publisherService.getDauTotal(date);

        ArrayList<Map> result = new ArrayList<>();

        HashMap<String, Object> dauMap = new HashMap<>();
        dauMap.put("id", "dau");
        dauMap.put("name", "新增日活");
        dauMap.put("value", total);

        HashMap<String, Object> newMidMap = new HashMap<>();
        newMidMap.put("id", "new_mid");
        newMidMap.put("name", "新增设备");
        newMidMap.put("value", "233");

        result.add(dauMap);
        result.add(newMidMap);

        return JSON.toJSONString(result);

    }

    /**
     * @Author:ygq
     * @Date:Created in 15:35 2020/2/20
     * @Description:获得近两日分时日活
     */
    @GetMapping("realtime-hours")
    public String realtimeHourDate(@RequestParam("id") String id, @RequestParam("date") String date) {

        HashMap<String, Map> result = new HashMap<>();

        Map map1 = publisherService.getDauHours(date);

        //获取前一天的时间
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar instance = Calendar.getInstance();

        try {
            instance.setTime(dateFormat.parse(date));
        } catch (ParseException e) {

        }

        instance.add(Calendar.DAY_OF_MONTH, -1);

        String yesterday = dateFormat.format(new Date(instance.getTimeInMillis()));

        //获得前一天的数据
        Map map2 = publisherService.getDauHours(yesterday);

        result.put("yesterday", map2);

        result.put("today", map1);

        return JSON.toJSONString(result);

    }
}
