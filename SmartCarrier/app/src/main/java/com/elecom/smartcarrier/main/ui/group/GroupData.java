package com.elecom.smartcarrier.main.ui.group;

import java.util.ArrayList;

public class GroupData {
    private String name ;
    private String email ;

    public GroupData(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return this.name ;
    }
    public String getEmail() {
        return this.email ;
    }

    public static ArrayList getGroupData(){
        ArrayList groupData = new ArrayList();
        groupData.add(new GroupData("임봉섭", "lbs123@kpu.ac.kr"));
        groupData.add(new GroupData("장빈", "bin2@kpu.ac.kr"));
        groupData.add(new GroupData("서종선", "sunny@kpu.ac.kr"));
        groupData.add(new GroupData("강수의", "sue@kpu.ac.kr"));
        groupData.add(new GroupData("남주연", "tieh@kpu.ac.kr"));
        return groupData;
    }
}