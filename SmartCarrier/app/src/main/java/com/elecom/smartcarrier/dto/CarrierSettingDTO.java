package com.elecom.smartcarrier.dto;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CarrierSettingDTO {
    // Service state
    private String slock;
    private String sbuzzer;
    private String sled;

    public CarrierSettingDTO(){
        super();
    }

    public CarrierSettingDTO(String slock, String sbuzzer, String sled) {
        super();

        this.slock = slock;
        this.sbuzzer = sbuzzer;
        this.sled = sled;
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


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> carriersSettings = new HashMap<>();

        carriersSettings.put("slock", slock);
        carriersSettings.put("sbuzzer", sbuzzer);
        carriersSettings.put("sled", sled);

        return carriersSettings;
    }

    @Override
    public String toString() {
        return "Carrier [slock = " + slock + ", sbuzzer = " + sbuzzer + ", sled = " + sled + "]";
    }

}

