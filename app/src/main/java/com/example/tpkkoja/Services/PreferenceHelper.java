package com.example.tpkkoja.Services;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    private final String INTRO = "intro";
    private final String USERNAME = "username";
    private final String DEPARTMENT = "department";
    private final String POSITION = "position";
    private final String PHONE = "phone";
    private final String NAMA = "nama";
    private SharedPreferences app_prefs;
    private Context context;

    public PreferenceHelper(Context context) {
        app_prefs = context.getSharedPreferences("shared",
                Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putIsLogin(boolean loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putBoolean(INTRO, loginorout);
        edit.commit();
    }
    public boolean getIsLogin() {
        return app_prefs.getBoolean(INTRO, false);
    }

    public void putUsername(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(USERNAME, loginorout);
        edit.commit();
    }
    public String getUsername() {
        return app_prefs.getString(USERNAME, "");
    }
    public void putNama(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(NAMA,loginorout);
        edit.commit();
    }
    public String getNama() {
        return app_prefs.getString(NAMA,"");
    }
    public void putPhone(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(PHONE,loginorout);
        edit.commit();
    }
    public String getPhone() {
        return app_prefs.getString(PHONE,"");
    }
    public void putPosition(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(POSITION,loginorout);
        edit.commit();
    }
    public String getPosition() {
        return app_prefs.getString(POSITION,"");
    }
    public void putDepartment(String loginorout) {
        SharedPreferences.Editor edit = app_prefs.edit();
        edit.putString(DEPARTMENT,loginorout);
        edit.commit();
    }
    public String getDepartment() {
        return app_prefs.getString(DEPARTMENT,"");
    }

}
