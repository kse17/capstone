package com.elecom.smartcarrier.dto;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CarrierStateDTO {
    private String lock;
    private String open;

    public CarrierStateDTO(){
        super();
    }

    public CarrierStateDTO(String lock, String open){
        super();
        this.lock = lock;
        this.open = open;
    }


    public String getLock(){
        return lock;
    }
    public void setLock(String lock){
        this.lock = lock;
    }

    public String getOpen(){
        return open;
    }
    public void setOpen(String open){
        this.open = open;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> carriersControl = new HashMap<>();

        carriersControl.put("lock", lock);
        carriersControl.put("open", open);


        return carriersControl;
    }

    @Override
    public String toString() {
        return "Carrier [lock = " + lock + ", open = " + open + "]";
    }


}
