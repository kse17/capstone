package com.elecom.smartcarrier.main.ui.my;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class MyDTO {
    private String mac;
    private String cname;
    private String manager;
    private Boolean lock;
    private Boolean buzzer;
    private Boolean led;

    public MyDTO() {
    }

    public MyDTO(String cname, String manager, Boolean lock, Boolean buzzer, Boolean led) {
        super();

        this.cname = cname;
        this.manager = manager;
        this.lock = lock;
        this.buzzer = buzzer;
        this.led = led;
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

    public Boolean getLock(){
        return lock;
    }
    public void setLock(Boolean lock){
        this.lock = lock;
    }

    public Boolean getBuzzer(){
        return buzzer;
    }
    public void setBuzzer(Boolean buzzer){
        this.buzzer = buzzer;
    }

    public Boolean getLed(){
        return led;
    }
    public void setLed(Boolean led){
        this.led = led;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> myMap = new HashMap<>();
        myMap.put("cname", cname);
        myMap.put("manager", manager);
        myMap.put("lock", lock);
        myMap.put("buzzer", buzzer);
        myMap.put("led", led);

        return myMap;
    }
}
