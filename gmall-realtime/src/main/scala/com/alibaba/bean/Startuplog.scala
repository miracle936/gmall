package com.alibaba.bean

/**
 * @Author:ygq
 * @Date:Created in 19:41 2020/2/18
 * @Description:
 */
case class StartUpLog(mid: String,
                      uid: String,
                      appid: String,
                      area: String,
                      os: String,
                      ch: String,
                      `type`: String,
                      vs: String,
                      var logDate: String,
                      var logHour: String,
                      var ts: Long)
