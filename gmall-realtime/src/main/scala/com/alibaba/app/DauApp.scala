package com.alibaba.app

import java.text.SimpleDateFormat
import java.util.Date

import com.alibaba.bean.StartUpLog
import com.alibaba.fastjson.JSON
import com.alibaba.common.GmallConstants
import com.alibaba.handler.DauHandler
import com.alibaba.util.MykafkaUtil
import org.apache.hadoop.conf.Configuration
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.{SparkConf, streaming}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.phoenix.spark._

/**
 * @Author:ygq
 * @Date:Created in 19:42 2020/2/18
 * @Description:
 */
object DauApp {

  private val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH")

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("alibaba").setMaster("local[*]")

    val ssc = new StreamingContext(conf, streaming.Seconds(5))

    val startupStream = MykafkaUtil.getKafkaStream(ssc, Set(GmallConstants.KAFKA_TOPIC_STARTUP))

    //4.将每一行数据转换为样例类对象
    val startLogDStream = startupStream.map { case (_, value: String) =>

      val log = JSON.parseObject(value, classOf[StartUpLog])

      val times = dateFormat.format(new Date(log.ts)).split(" ")

      log.logDate = times(0)

      log.logHour = times(1)

      log

    }

    //5.跨批次去重
    val filterByRedis: DStream[StartUpLog] = DauHandler.filterDataByRedis(startLogDStream)

    //6.同批次去重
    val filterByBatch: DStream[StartUpLog] = DauHandler.filterDataByBatch(filterByRedis)
    filterByBatch.cache()

    //7.将数据保存至Redis,以供下一次去重使用
    DauHandler.saveMidToRedis(filterByBatch)

    filterByBatch.count().print()

    //8.有效数据(不做计算)写入HBase
    filterByBatch.foreachRDD(rdd =>
      rdd.saveToPhoenix("GMALL190615_DAU", Seq("MID", "UID", "APPID", "AREA", "OS", "CH", "TYPE", "VS", "LOGDATE", "LOGHOUR", "TS"), new Configuration(), Some("hadoop111,hadoop112,hadoop113:2181"))
    )

    //启动任务
    ssc.start()
    ssc.awaitTermination()

  }

}
