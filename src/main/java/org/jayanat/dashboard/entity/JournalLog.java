package org.jayanat.dashboard.entity;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
public class JournalLog {
    private String businessCode;
    private int msgType;
    private long tranStamp1;
    private long tranStamp2;
    private long tranStamp3;
    private long tranStamp4;


    public long eventTimestamp() {
        return tranStamp1;
    }

    public boolean isSuccess() {
        return Objects.equals(businessCode, "0000");
    }

    public long totalTime() {
        return tranStamp4 - tranStamp1;
    }

    public long externalTime() {
        return tranStamp3 - tranStamp2;
    }

    public long internalTime() {
        return totalTime() - externalTime();
    }
}
