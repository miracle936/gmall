package com.alibaba.handler

import com.alibaba.bean.StartUpLog
import com.alibaba.util.RedisUtil
import org.apache.spark.streaming.dstream.DStream

/**
 * @Author:ygq
 * @Date:Created in 13:50 2020/2/20
 * @Description:
 */
object DauHandler {

  //将数据保存至Redis,以供下一次去重使用
  def saveMidToRedis(filterByBatch: DStream[StartUpLog]) = {

    filterByBatch.foreachRDD(rdd => {

      rdd.foreachPartition(iter => {

        val jedis = RedisUtil.getJedisClient

        iter.foreach(bean => {

          jedis.sadd("dau:" + bean.logDate, bean.mid)

        })

        jedis.close()

      })

    })

  }

  //同批次去重
  def filterDataByBatch(filterByRedis: DStream[StartUpLog]): DStream[StartUpLog] = {

    filterByRedis.transform(rdd => {

      rdd.map(bean => {

        ((bean.logDate, bean.mid), bean)

      }).groupByKey().flatMap(tuple => {

        tuple._2.toList.sortWith((l, r) => {

          l.ts < r.ts

        }).take(1)

      })

    })

  }

  //跨批次去重
  def filterDataByRedis(startLogDStream: DStream[StartUpLog]): DStream[StartUpLog] = {

    startLogDStream.transform(rdd => {

      rdd.mapPartitions(iter => {

        val jedis = RedisUtil.getJedisClient

        val logs = iter.filter(bean => {

          !jedis.sismember("dau:" + bean.logDate, bean.mid)

        })

        jedis.close()

        logs

      })

    })

  }

}
