package com.manueldev.roghurgarantias.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.manueldev.roghurgarantias.R;
import com.manueldev.roghurgarantias.enums.UserAppStatus;
import com.manueldev.roghurgarantias.helpers.Utils;

public class AccessUserSharedPreferences {

    private SharedPreferences sharedPreferences;
    private Context context;

    private String keyUserAccessStatus;
    private String keyUserIdLogged;


    public AccessUserSharedPreferences(Context context) {
        this.context = context;

        this.sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.shared_preferences_access), Context.MODE_PRIVATE);
        this.keyUserAccessStatus = context.getString(R.string.sp_login_status);
        this.keyUserIdLogged = context.getString(R.string.sp_login_userId);
    }

    public AccessUserSharedPreferences logInUser(long userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(keyUserAccessStatus, UserAppStatus.LOGIN.getUserStatus())
                .putLong(keyUserIdLogged, userId);
        editor.commit();
        return this;

    }

    public AccessUserSharedPreferences logOutUser(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(keyUserAccessStatus, UserAppStatus.LOGOUT.getUserStatus())
                .putLong(keyUserIdLogged, Utils.VALUE_NOT_ASSIGNED_ID);
        editor.commit();
        return this;
    }


   /* public AccessUserSharedPreferences setUserAppStatus(UserAppStatus userAppStatus) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(keyUserAccessStatus, userAppStatus.getUserStatus());
        editor.commit();
        return this;
    }

    public AccessUserSharedPreferences setUserIdLogger(long userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(keyUserIdLogged, userId);
        editor.commit();
        return this;
    }*/

    public UserAppStatus getUserAppStatus() {
        return sharedPreferences.getBoolean(keyUserAccessStatus, Boolean.FALSE) ? UserAppStatus.LOGIN : UserAppStatus.LOGOUT;
    }

    public long getUserIdLogged() {
        return sharedPreferences.getLong(keyUserIdLogged, Utils.VALUE_NOT_ASSIGNED_ID);
    }

}
