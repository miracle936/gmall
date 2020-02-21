package com.alibaba.gmalllogger.controller;

import com.alibaba.common.GmallConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:ygq
 * @Date:Created in 20:35 2020/2/17
 * @Description:
 */
@Slf4j
@RestController
public class LoggerController {

    @Autowired
    KafkaTemplate<String, String> kafka;

    @PostMapping("log")
    public String logString(@RequestParam("logString") String str) {

        //将str封装成json对象，并添加时间戳
        JSONObject json = JSON.parseObject(str);
        json.put("ts", System.currentTimeMillis());

        String jsonString = json.toString();

        //记录日志文件
        log.info(jsonString);

        //kafka生产者
        if (jsonString.contains("startup")) {
            kafka.send(GmallConstants.KAFKA_TOPIC_STARTUP, jsonString);
        } else {
            kafka.send(GmallConstants.KAFKA_TOPIC_EVENT, jsonString);
        }

        return "success";

    }

}
