package com.uit.myairquality.Model;

public class ChartResquest {
    private Long fromTimestamp;
    private Long toTimestamp;

    private String fromTime;
    private String toTime;

    private String type = "string";
    public ChartResquest (String fromtime, String toTime) {
        this.fromTime=fromtime;
        this.toTime=toTime;

    }
}