package com.manueldev.roghurgarantias.enums;

public enum UserAppStatus {

    LOGIN(true), LOGOUT(false);


    private boolean userAppStatus;

    UserAppStatus(boolean userAppStatus){
        this.userAppStatus = userAppStatus;
    }

    public boolean getUserStatus(){
        return userAppStatus;
    }

}
