package com.elecom.smartcarrier.ui.group;

import java.util.ArrayList;

public class GroupData {
    private String nameStr ;
    private String emailStr ;

    public GroupData(String name, String email){
        nameStr = name;
        emailStr = email;
    }

    public String getName() {
        return nameStr ;
    }
    public String getEmail() {
        return emailStr ;
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