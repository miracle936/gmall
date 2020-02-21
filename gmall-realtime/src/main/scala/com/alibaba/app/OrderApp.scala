package com.alibaba.app

import com.alibaba.bean.OrderInfo
import com.alibaba.common.GmallConstants
import com.alibaba.fastjson.JSON
import com.alibaba.util.MykafkaUtil
import org.apache.hadoop.conf.Configuration
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.phoenix.spark._


/**
 * @Author:ygq
 * @Date:Created in 19:51 2020/2/21
 * @Description:
 */
object OrderApp {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("alibaba").setMaster("local[*]")

    val ssc = new StreamingContext(conf, Seconds(5))

    val kafka = MykafkaUtil.getKafkaStream(ssc, Set(GmallConstants.KAFKA_TOPIC_ORDER_INFO))

    kafka.map(tuple => {

      //每条json封装成bean
      val bean = JSON.parseObject(tuple._2, classOf[OrderInfo])

      val createTimeArr: Array[String] = bean.create_time.split(" ")

      bean.create_date = createTimeArr(0)

      bean.create_hour = createTimeArr(1).split(":")(0)

      // 收件人 电话 脱敏
      bean.consignee_tel = bean.consignee_tel.splitAt(4)._1 + "*******"

      bean

    }).foreachRDD(rdd => {

      //存入Phoenix
      rdd.saveToPhoenix("GMALL2019_ORDER_INFO", Seq("ID", "PROVINCE_ID", "CONSIGNEE", "ORDER_COMMENT", "CONSIGNEE_TEL", "ORDER_STATUS", "PAYMENT_WAY", "USER_ID", "IMG_URL", "TOTAL_AMOUNT", "EXPIRE_TIME", "DELIVERY_ADDRESS", "CREATE_TIME", "OPERATE_TIME", "TRACKING_NO", "PARENT_ORDER_ID", "OUT_TRADE_NO", "TRADE_BODY", "CREATE_DATE", "CREATE_HOUR"), new Configuration(), Some("hadoop111,hadoop112,hadoop113:2181"))

    })

    ssc.start()
    ssc.awaitTermination()

  }

}
