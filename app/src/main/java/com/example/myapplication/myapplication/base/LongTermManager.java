package com.example.myapplication.myapplication.base;


import android.content.Context;

import com.example.myapplication.myapplication.models.NotificationModel;
import com.example.myapplication.myapplication.models.UserModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;


public class LongTermManager {
    private static LongTermManager mInstance;
    private static SharedPreferencesManger mSharedPreference;
    private static Gson gson;
    private UserModel userModel;
    private String notificationsToken = "";
    private ArrayList<NotificationModel> notificationModelArrayList;
    private String deviceLanguage;

    public static synchronized LongTermManager getInstance() {
        if (mInstance == null) {
            mInstance = new LongTermManager();
            gson = new Gson();
        }
        return mInstance;
    }


    public String getCurrentApi() {
        return "Config.SERVER_URL_LIVE";
    }

    public void setContext(Context mContext) {
        mSharedPreference = new SharedPreferencesManger();
        notificationsToken = mSharedPreference.GetStringPreferences("notificationsToken", "");
        deviceLanguage = mSharedPreference.GetStringPreferences("deviceLanguage", "En");

        try {
            userModel = gson.fromJson(mSharedPreference.GetStringPreferences("userModel", ""), UserModel.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            Type collectionType = new TypeToken<Collection<NotificationModel>>() {
            }.getType();
            notificationModelArrayList = gson.fromJson(mSharedPreference.GetStringPreferences("notificationModelArrayList", ""), collectionType);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getDeviceLanguage() {
        return deviceLanguage;
    }

    public void setDeviceLanguage(String deviceLanguage) {
        this.deviceLanguage = deviceLanguage;
        try {
            mSharedPreference.SetStringPreferences("deviceLanguage", deviceLanguage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<NotificationModel> getNotificationModelArrayList() {
        return notificationModelArrayList == null ? new ArrayList<>() : notificationModelArrayList;
    }

    public void setNotificationModelArrayList(ArrayList<NotificationModel> notificationModelArrayList) {
        this.notificationModelArrayList = notificationModelArrayList;
        try {
            mSharedPreference.SetStringPreferences("notificationModelArrayList", gson.toJson(notificationModelArrayList));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getNotificationsToken() {
        return notificationsToken;
    }

    public void setNotificationsToken(String notificationsToken) {
        this.notificationsToken = notificationsToken;
        mSharedPreference.SetStringPreferences("notificationsToken", notificationsToken);
    }


    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
        try {
            mSharedPreference.SetStringPreferences("userModel", gson.toJson(userModel));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clearAll() {
        mSharedPreference.clearPreference();
        userModel = null;
    }
}
