package com.elecom.smartcarrier.dto;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class CarrierLogDTO {

    public String date;
    public String name;
    public String email;
    public String locklog;

    public CarrierLogDTO() {
        super();
    }

    public CarrierLogDTO(String date, String name, String email, String locklog) {
        super();
        this.date = date;
        this.name = name;
        this.email = email;
        this.locklog = locklog;
    }

    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocklog() {
        return this.locklog;
    }
    public void setLocklog(String locklog) {
        this.locklog = locklog;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> logs = new HashMap<>();

        logs.put("date", date);
        logs.put("name", name);
        logs.put("email", email);
        logs.put("locklog", locklog);

        return logs;
    }

    @Override
    public String toString() {
        return "User [date = " + date + ", name = " + name
                + ", email = " + email + ", lock log = " + locklog + "]";
    }
}
