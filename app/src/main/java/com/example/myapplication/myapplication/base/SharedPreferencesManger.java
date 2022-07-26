package com.example.myapplication.myapplication.base;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.myapplication.myapplication.HiltApplication;



/**
 * Created by tariqkhundakji on 5/11/16.
 * Mange all shared preferences in app
 */
class SharedPreferencesManger {
    private static SharedPreferences oSharedPreferences;
    private static SharedPreferences.Editor oEditor;

    //------------ Shared Preferences Constructor ----------
    SharedPreferencesManger() {
        initPreference();
    }

    private static void initPreference() {
        if (oSharedPreferences == null) {
            oSharedPreferences = HiltApplication.Companion.getContext().getSharedPreferences("grocery.usama.com.grocery.dookanti", Context.MODE_PRIVATE);
//            PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        if (oEditor == null) {
            oEditor = oSharedPreferences.edit();
            oEditor.apply();
        }
    }

    void clearPreference() {
        oEditor.clear();
        oEditor.commit();
    }

    ////.to add encryption to the shared data ... you need to create a class to decrypt and encrypt the data before set and after get.
    //------------ Get Shared Preferences Value ------------
    String GetStringPreferences(String Key, String sDefaultValue) {
        return oSharedPreferences.getString(Key, sDefaultValue);
    }

    public long GetLongPreferences(String Key, long sDefaultValue) {
        return oSharedPreferences.getLong(Key, sDefaultValue);
    }

    boolean GetBooleanPreferences(String Key, boolean sDefaultValue) {
        return oSharedPreferences.getBoolean(Key, sDefaultValue);
    }

    int GetIntPreferences(String Key, int sDefaultValue) {
        return oSharedPreferences.getInt(Key, sDefaultValue);
    }

    float GetFloatPreferences(String Key, float sDefaultValue) {
        return oSharedPreferences.getFloat(Key, sDefaultValue);
    }

    //------------ Set Shared Preferences Value ------------
    void SetStringPreferences(String Key, String sValue) {
        oEditor.putString(Key, sValue).apply();
    }

    public void SetLongPreferences(String Key, long sValue) {
        oEditor.putLong(Key, sValue).apply();
    }

    void SetBooleanPreferences(String Key, boolean sValue) {
        oEditor.putBoolean(Key, sValue).apply();
    }

    void SetIntPreferences(String Key, int sValue) {
        oEditor.putInt(Key, sValue).apply();
    }

    void SetFloatPreferences(String Key, float sValue) {
        oEditor.putFloat(Key, sValue).apply();
    }
}
