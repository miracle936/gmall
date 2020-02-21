package com.alibaba.main;

import com.alibaba.common.GmallConstants;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.util.MykafkaSender;
import com.google.protobuf.InvalidProtocolBufferException;

import java.net.InetSocketAddress;

/**
 * @Author:ygq
 * @Date:Created in 15:02 2020/2/21
 * @Description:
 */
public class CanalClient {

    public static void main(String[] args) throws InvalidProtocolBufferException {

        CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress("hadoop111", 11111), "example", "", "");

        while (true) {

            canalConnector.connect();

            canalConnector.subscribe("gmall.*");

            Message message = canalConnector.get(100);

            if (message.getEntries().size() == 0) {

                try {
                    System.out.println("没有新数据");
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            } else {

                for (CanalEntry.Entry entry : message.getEntries()) {

                    if (CanalEntry.EntryType.ROWDATA.equals(entry.getEntryType())) {

                        CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());

                        String tableName = entry.getHeader().getTableName();

                        handler(tableName, rowChange.getEventType(), rowChange);

                    }

                }

            }

        }

    }

    //将数据传给kafka
    private static void handler(String tableName, CanalEntry.EventType eventType, CanalEntry.RowChange rowChange) {

        if (CanalEntry.EventType.INSERT.equals(eventType) && "order_info".equals(tableName)) {

            for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tableName", tableName);

                for (CanalEntry.Column column : rowData.getAfterColumnsList()) {

                    jsonObject.put(column.getName(), column.getValue());

                }

                System.out.println(jsonObject.toString());

                MykafkaSender.send(GmallConstants.KAFKA_TOPIC_ORDER_INFO, jsonObject.toJSONString());

            }

        }

    }

}
