package com.elecom.smartcarrier.dto;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserSettingDTO {

    public String sopen;
    public String sdist;
    public String sgroup;

    public UserSettingDTO() {
        super();
    }

    public UserSettingDTO(String sopen, String sdist, String sgroup) {
        super();
        this.sopen = sopen;
        this.sdist = sdist;
        this.sgroup = sgroup;
    }

    public String getSopen() {
        return this.sopen ;
    }
    public void setSopen(String sopen) {
        this.sopen = sopen;
    }

    public String getSdist() {
        return this.sdist ;
    }
    public void setSdist(String sdist) {
        this.sdist = sdist;
    }

    public String getSgroup() {
        return this.sgroup ;
    }
    public void setSgroup(String sgroup) {
        this.sgroup = sgroup;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> usersSettings = new HashMap<>();

        usersSettings.put("sopen", sopen);
        usersSettings.put("sdist", sdist);
        usersSettings.put("sgroup", sgroup);

        return usersSettings;
    }
    
    @Override
    public String toString() {
        return "UserSettingDTO [sopen = " + sopen
                + ", sdist = " + sdist + ", sgroup = " + sgroup + "]";
    }
}
