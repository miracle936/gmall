package com.alibaba.util

import java.io.InputStreamReader
import java.util.Properties

/**
 * @Author:ygq
 * @Date:Created in 19:39 2020/2/18
 * @Description:
 */
object PropertiesUtil {

  def load(propertieName: String): Properties = {
    val prop = new Properties();
    prop.load(new InputStreamReader(Thread.currentThread().getContextClassLoader.getResourceAsStream(propertieName), "UTF-8"))
    prop
  }

}
