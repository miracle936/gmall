package com.alibaba.gmallpublisher.service;

import java.util.Map;

/**
 * @Author:ygq
 * @Date:Created in 15:35 2020/2/20
 * @Description:
 */
public interface PublisherService {

    public int getDauTotal(String date);

    public Map getDauHours(String date);

}
