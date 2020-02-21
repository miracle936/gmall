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
    public String getRealTimeTotal(@RequestParam("date") String date) {

        int total = publisherService.getDauTotal(date);

        ArrayList<Map> result = new ArrayList<>();

        HashMap<String, Object> dauMap = new HashMap<>();
        dauMap.put("id", "dau");
        dauMap.put("name", "新增日活");
        dauMap.put("value", total);

        Integer newUser = publisherService.getNewUser(date);

        HashMap<String, Object> newMidMap = new HashMap<>();
        newMidMap.put("id", "new_mid");
        newMidMap.put("name", "新增设备");
        newMidMap.put("value", newUser);

        Double count = publisherService.getOrderAmount(date);

        HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("id", "order_amount");
        orderMap.put("name", "新增交易额");
        orderMap.put("value", count);

        result.add(dauMap);
        result.add(newMidMap);
        result.add(orderMap);

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

        String yesterday = getYesterdayDate(date);

        Map map1 = null;

        Map map2 = null;

        if ("dau".equals(id)) {

            map1 = publisherService.getDauHours(date);

            //获得前一天的数据
            map2 = publisherService.getDauHours(yesterday);

        } else if ("order_amount".equals(id)) {

            map1 = publisherService.getOrderAmountHour(date);

            //获得前一天的数据
            map2 = publisherService.getOrderAmountHour(yesterday);

        }

        result.put("yesterday", map2);

        result.put("today", map1);

        return JSON.toJSONString(result);

    }

    //获取前一天的时间
    private String getYesterdayDate(@RequestParam("date") String date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar instance = Calendar.getInstance();

        try {
            instance.setTime(dateFormat.parse(date));
        } catch (ParseException e) {

        }

        instance.add(Calendar.DAY_OF_MONTH, -1);

        return dateFormat.format(new Date(instance.getTimeInMillis()));
    }
}
