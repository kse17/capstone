package com.elecom.smartcarrier.dto;

// Database에 저장하기 위한 UserDTO
public class UserDTO {

    public String email;
    public String uname;

    public UserDTO() {
        super();
    }

    public UserDTO(String email, String uname) {
        super();
        this.email = email;
        this.uname = uname;
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

    @Override
    public String toString() {
        return "User [email = " + email + ", user name = " + uname + "]";
    }
}
