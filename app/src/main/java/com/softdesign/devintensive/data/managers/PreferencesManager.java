package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApp;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {

    private SharedPreferences mSharedPreferences;

    private static final String[] USER_FIELDS = {ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_EMAIL_KEY, ConstantManager.USER_VK_KEY,
            ConstantManager.USER_REPO_KEY, ConstantManager.USER_ABOUT_KEY};

    public PreferencesManager() {
        this.mSharedPreferences = DevintensiveApp.getSharedPreferences();
    }

    public void saveProfileData(List<String> fields) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        for (int i = 0; i < USER_FIELDS.length; i++){
            editor.putString(USER_FIELDS[i], fields.get(i));
        }
        editor.apply();
    }

    public List<String> loadProfileData(){
        List<String> fields = new ArrayList<>();
        fields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, null));
        fields.add(mSharedPreferences.getString(ConstantManager.USER_EMAIL_KEY, null));
        fields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, null));
        fields.add(mSharedPreferences.getString(ConstantManager.USER_REPO_KEY, null));
        fields.add(mSharedPreferences.getString(ConstantManager.USER_ABOUT_KEY, null));
        return fields;
    }

    public void saveUserPhoto(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto(){
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "android.resources://com.softdesign.devintensive/drawable/userphoto0"));
    }
}
