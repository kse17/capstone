package com.elecom.smartcarrier.main;

// Database에 저장하기 위한 UserDTO
public class UserDTO {

    public String email;
    public String name;

    public UserDTO() {
        super();
    }

    public UserDTO(String email, String name) {
        super();
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return this.email ;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name ;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User [email = " + email + ", name = " + name + "]";
    }
}
