package com.elecom.smartcarrier.dto;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CarrierDTO {
    private String mac;
    private String cname;
    private String manager;
//    private String carrierUsers;      // 그룹기능 아직이라 baaam(뺌ㅋ)
    private String fpValue;
    private String fpNum;
    private Integer fpLocation;
    private Integer fpConfidence;

    public CarrierDTO() {
        super();
    }

    public CarrierDTO(String mac, String cname, String manager, String fpValue,
                      String fpNum, Integer fpLocation, Integer fpConfidence) {
        super();

        this.mac = mac;
        this.cname = cname;
        this.manager = manager;
        //this.carrierUsers = carrierUsers;
        this.fpValue = fpValue;
        this.fpNum = fpNum;
        this.fpLocation = fpLocation;
        this.fpConfidence = fpConfidence;
    }

    public String getMac() {
        return this.mac ;
    }
    public void setMac(String email) {
        this.mac = mac;
    }

    public String getCname() {
        return this.cname ;
    }
    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getManager() {
        return this.manager ;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }

//    public String getCarrierUsers(){
//        return carrierUsers;
//    }
//    public void setCarrierUsers(String carrierUsers){
//        this.carrierUsers = carrierUsers;
//    }

    public String getFpValue() {
        return this.fpValue ;
    }
    public void setFpValue(String fpValue) {
        this.fpValue = fpValue;
    }

    public String getFpNum() {
        return this.fpNum ;
    }
    public void setFpNum(String FpNum) {
        this.fpNum = fpNum;
    }

    public Integer getFpLocation() {
        return this.fpLocation;
    }
    public void setFpLocation(Integer fpLocation) {
        this.fpLocation = fpLocation;
    }

    public Integer getFpConfidence() {
        return this.fpConfidence;
    }
    public void setFpConfidence(Integer fpConfidence) { this.fpConfidence = fpConfidence; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> carriers = new HashMap<>();

        carriers.put("mac", mac);
        carriers.put("cname", cname);
        carriers.put("manager", manager);
//        carriers.put("carrierUser", carrierUsers);
        carriers.put("fpValue", fpValue);
        carriers.put("fpNum", fpNum);
        carriers.put("fpLocation",fpLocation);
        carriers.put("fpConfidence", fpConfidence);

        return carriers;
    }

    @Override
    public String toString() {
        return "Carrier [mac = " + mac + ", carrier name = " + cname + ", manager name = " + manager
                + ", fpValue = " + fpValue + ", fpNum = " + fpNum
                + ", fpLocation = " + fpLocation + "fpConfidence" + fpConfidence + "]";
    }

}
