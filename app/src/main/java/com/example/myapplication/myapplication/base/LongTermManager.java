package com.example.myapplication.myapplication.base;


import android.content.Context;
import com.example.myapplication.myapplication.models.UserModel;
import com.google.gson.Gson;


public class LongTermManager {
    private static LongTermManager mInstance;
    private static SharedPreferencesManger mSharedPreference;
    private static Gson gson;
    private UserModel userModel;

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
        try {
            userModel = gson.fromJson(mSharedPreference.GetStringPreferences("userModel", ""), UserModel.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
