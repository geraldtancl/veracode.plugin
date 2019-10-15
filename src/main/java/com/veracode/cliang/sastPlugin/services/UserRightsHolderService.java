package com.veracode.cliang.sastPlugin.services;

import com.veracode.cliang.sastPlugin.objects.raw.appList.UserType;

public class UserRightsHolderService {

    UserType userRights;

    public UserType getUserRights() {
        return userRights;
    }

    public void setUserRights(UserType userRights) {
        this.userRights = userRights;
    }
}
