package com.elecom.smartcarrier.dto;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CarrierControlDTO {
    private String lock;
    private String buzzer;
    private String open;

    public CarrierControlDTO(){
        super();
    }

    public CarrierControlDTO(String lock, String buzzer, String open) {
        super();

        this.lock = lock;
        this.buzzer = buzzer;
        this.open = open;
    }

    public String getLock(){
        return lock;
    }
    public void setLock(String lock){
        this.lock = lock;
    }

    public String getBuzzer(){
        return buzzer;
    }
    public void setBuzzer(String buzzer){
        this.buzzer = buzzer;
    }

    public String getOpen(){
        return open;
    }
    public void setOpen(String open){
        this.open = open;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> carriersControls = new HashMap<>();
        
        carriersControls.put("lock", lock);
        carriersControls.put("buzzer", buzzer);
        carriersControls.put("open", open);
        
        return carriersControls;
    }

    @Override
    public String toString() {
        return "Carrier [lock = " + lock + ", buzzer = " + buzzer
                + ", open = " + open + "]";
    }


}
