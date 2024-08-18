package com.example.myapplication.Session;

import android.content.Context;
import android.content.SharedPreferences;

import org.checkerframework.checker.units.qual.C;

import lombok.Data;

@Data
public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "user_session";
    String KEY_ID = "id";
    String DATE = "date";
    public SessionManager(Context context,String name){
        sharedPreferences=context.getSharedPreferences(name,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public SessionManager(Context context){
        sharedPreferences= context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public void createDate(String val){
        editor.putString(DATE,val);
        editor.commit();
    }
    public void createSession(String id){
        editor.putString(KEY_ID,id);
        editor.commit();
    }
    public boolean checkSession() {
        return sharedPreferences.getString(KEY_ID, "-1").equals("-1");
    }
    public void clearSession(){
        editor.clear();
        editor.commit();
    }
    public void updateDate(String val) {
        editor.remove(DATE);
        editor.putString(DATE,val);
        editor.commit();
    }
    public String getDate(){
        return sharedPreferences.getString(DATE,"-1");
    }

    public String getSessionId(){
        return sharedPreferences.getString(KEY_ID,"-1");
    }
}
