package com.k002422.tochkatest.utils;

import android.net.Uri;

public class AccountUtils {
    private String userName, authType;
    private Uri userPhotoUrl;

    public String getUserName() {
        return userName;
    }

    public Uri getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public String getAuthType() {
        return authType;
    }

    public AccountUtils(String authType, String userName, Uri userPhotoUrl) {
        this.authType = authType;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
    }

    @Override
    public String toString() {
        return "\t\t" + userName + "\n\t\t" + userPhotoUrl;
    }
}
