package com.elecom.smartcarrier;

// Database에 저장하기 위한 UserDTO
class UserDTO {
    private String id;
    private String password;
    private String phone;

    public UserDTO() {
    }

    public UserDTO(String id, String password, String phone) {
        super();
        this.id = id;
        this.password = password;
        this.phone = phone;
    }

    public String getid(){
        return id;
    }
    public void setid(String id){
        this.id = id;
    }
    public String getpassword(){
        return password;
    }
    public void setpassword(String password){
        this.password = password;
    }
    public String getphone(){
        return phone;
    }
    public void setphone(String phone){
        this.phone = phone;
    }
}
