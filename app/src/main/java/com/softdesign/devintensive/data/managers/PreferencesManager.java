package com.softdesign.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesign.devintensive.utils.ConstantManager;
import com.softdesign.devintensive.utils.DevintensiveApp;

import java.util.ArrayList;
import java.util.List;

public class PreferencesManager {

    private SharedPreferences mSharedPreferences;

    private static final String[] USER_FIELDS = {
            ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_EMAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_REPO_KEY,
            ConstantManager.USER_ABOUT_KEY
    };

    private static final String[] USER_VALUES = {
            ConstantManager.USER_RATING_VALUE,
            ConstantManager.USER_CODELINES_VALUE,
            ConstantManager.USER_PROJECTS_VALUE
    };

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
        fields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "N/A"));
        fields.add(mSharedPreferences.getString(ConstantManager.USER_EMAIL_KEY, "N/A"));
        fields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, "N/A"));
        fields.add(mSharedPreferences.getString(ConstantManager.USER_REPO_KEY, "N/A"));
        fields.add(mSharedPreferences.getString(ConstantManager.USER_ABOUT_KEY, "N/A"));
        return fields;
    }

    public void saveUserPhoto(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto(){
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY,
                "android.resources://com.softdesign.devintensive/drawable/userphoto0"));
    }

    public void saveAvatar(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_AVATAR_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadAvatar(){
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_AVATAR_KEY,
                "android.resources://com.softdesign.devintensive/drawable/av"));
    }

    public void saveAuthToken(String authToken){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    public String getAuthToken(){
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY, "");
    }

    public void saveUserId(String userId){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId(){
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "");
    }

    public void saveUserProfileValues(String[] userValues){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_VALUES.length; i++){
            editor.putString(USER_VALUES[i], userValues[i]);
        }
        editor.apply();
    }

    public List<String> loadUserInfoValues(){
        List<String> userValues = new ArrayList<>();
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_RATING_VALUE, "N/A"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_CODELINES_VALUE, "N/A"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_PROJECTS_VALUE, "N/A"));
        return userValues;
    }

    public void saveName(String lastName, String firstName){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.LAST_NAME_KEY, lastName);
        editor.putString(ConstantManager.FIRST_NAME_KEY, firstName);
        editor.apply();
    }

    public String loadName(){
        String name = mSharedPreferences.getString(ConstantManager.LAST_NAME_KEY, "N/A")
                + " " + mSharedPreferences.getString(ConstantManager.FIRST_NAME_KEY, "N/A");
        return name;
    }
}
