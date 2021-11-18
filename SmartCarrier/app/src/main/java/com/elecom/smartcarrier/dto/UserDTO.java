package com.elecom.smartcarrier.dto;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

// Database에 저장하기 위한 UserDTO
public class UserDTO {

    public String email;
    public String uname;
    public String uid;

    public UserDTO() {
        super();
    }

    public UserDTO(String uid, String email, String uname) {
        super();
        this.uid = uid;
        this.email = email;
        this.uname = uname;
    }

    public String getUid() {
        return this.uid ;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return this.email ;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getUname() {
        return this.uname ;
    }
    public void setUname(String uname) {
        this.uname = uname;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> users = new HashMap<>();

        users.put("uid", uid);
        users.put("email", email);
        users.put("uname", uname);

        return users;
    }
    
    @Override
    public String toString() {
        return "User [uid = " + uid + ", email = " + email + ", user name = " + uname + "]";
    }
}
