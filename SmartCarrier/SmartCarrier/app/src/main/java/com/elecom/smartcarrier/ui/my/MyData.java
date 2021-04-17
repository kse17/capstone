package com.elecom.smartcarrier.ui.my;

import java.util.ArrayList;

public class MyData {
    private String titleStr ;
    private String detailStr ;

    public MyData(String title, String detail){
        titleStr = title;
        detailStr = detail;
    }

    public String getTitle() {
        return titleStr ;
    }
    public String getDetail() {
        return detailStr ;
    }

    public static ArrayList getMyData(){
        ArrayList myData = new ArrayList();
        myData.add(new MyData("Carrier1", "1"));
        myData.add(new MyData("Carrier2", "2"));
        myData.add(new MyData("Carrier3", "3"));
        return myData;
    }
}