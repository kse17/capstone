package com.elecom.smartcarrier.dto;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CarrierDTO {
    private String mac;
    private String cname;
    private String manager;
    private String lock;

    // Service state
    private String slock;
    private String sbuzzer;
    private String sled;

    private String operation;
    private String result;


    public CarrierDTO() {
    }

    public CarrierDTO(String mac, String cname, String manager, String lock, String slock, String sbuzzer, String sled, String operation, String result) {
        super();

        this.mac = mac;
        this.cname = cname;
        this.manager = manager;
        this.lock = lock;
        this.slock = slock;
        this.sbuzzer = sbuzzer;
        this.sled = sled;
        this.operation = operation;
        this.result = result;
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

    public String getLock(){
        return lock;
    }
    public void setLock(String lock){
        this.lock = lock;
    }

    public String getSlock(){
        return slock;
    }
    public void setSlock(String slock){
        this.slock = slock;
    }

    public String getSbuzzer(){
        return sbuzzer;
    }
    public void setSbuzzer(String sbuzzer){
        this.sbuzzer = sbuzzer;
    }

    public String getSled(){
        return sled;
    }
    public void setSled(String sled){
        this.sled = sled;
    }

    public String getOperation(){
        return operation;
    }
    public void setOperation(String operation){
        this.operation = operation;
    }

    public String getResult(){
        return result;
    }
    public void setResult(String result){
        this.result = result;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> carriers = new HashMap<>();
        carriers.put("mac", mac);
        carriers.put("cname", cname);
        carriers.put("manager", manager);
        carriers.put("lock", lock);
        carriers.put("slock", slock);
        carriers.put("sbuzzer", sbuzzer);
        carriers.put("sled", sled);
        carriers.put("operation", operation);
        carriers.put("result", result);

        return carriers;
    }

    @Override
    public String toString() {
        return "Carrier [mac = " + mac + ", carrier name = " + cname + ", manager name = " + manager
                + ", lock = " + lock + ", slock = " + slock + ", sbuzzer = " + sbuzzer + ", sled = " + sled
                + ", operation = " + operation + ", result = " + result + "]";
    }

}
