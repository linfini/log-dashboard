package org.jayanat.dashboard.config;

import cn.hutool.json.JSONUtil;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.jayanat.dashboard.entity.JournalLog;

public class JournalJsonSerde extends Serdes.WrapperSerde<JournalLog> {

    public JournalJsonSerde() {
        super(new JournalSerializer(), new JournalDeserializer());
    }

    public static class JournalSerializer implements Serializer<JournalLog> {
        @Override
        public byte[] serialize(String s, JournalLog journalLog) {
            return JSONUtil.toJsonStr(journalLog).getBytes();
        }
    }

    public static class JournalDeserializer implements Deserializer<JournalLog> {
        @Override
        public JournalLog deserialize(String s, byte[] bytes) {
            return JSONUtil.toBean(new String(bytes), JournalLog.class);
        }
    }
}
