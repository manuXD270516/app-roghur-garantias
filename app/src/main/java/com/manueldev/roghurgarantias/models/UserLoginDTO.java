package com.manueldev.roghurgarantias.models;

public class UserLoginDTO {

    private String username;
    private String password;
    private String userType;

    public UserLoginDTO(){}

    public String getUsername() {
        return username;
    }

    public UserLoginDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserLoginDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getUserType() {
        return userType;
    }

    public UserLoginDTO setUserType(String userType) {
        this.userType = userType;
        return this;
    }
}
