package com.elecom.smartcarrier.ui.log;

import java.util.ArrayList;

public class LogData {
    private String dateStr ;
    private String timeStr ;
    private String nameStr ;

    public LogData(String date, String time, String name) {
        dateStr = date;
        timeStr = time;
        nameStr = name;
    }

    public String getDate() {
        return dateStr ;
    }
    public String getTime() {
        return timeStr ;
    }
    public String getName() {
        return nameStr ;
    }


    public static ArrayList getGroupData(){
        ArrayList logData = new ArrayList();
        logData.add(new LogData("2021-02-12-", "12:08:38", "임봉섭"));
        logData.add(new LogData("2021-02-13", "16:05:50", "임봉섭"));
        logData.add(new LogData("2021-02-13", "20:55:12", "강수의"));

        return logData;
    }
}