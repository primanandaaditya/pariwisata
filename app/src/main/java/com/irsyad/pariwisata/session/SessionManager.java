package com.irsyad.pariwisata.session;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.irsyad.pariwisata.MainActivity;
import com.irsyad.pariwisata.ui.admin.AdminActivity;
import com.irsyad.pariwisata.ui.login.LoginActivity;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    String key_nama = "nama";
    String key_id_user = "id user";
    String key_role = "role";
    String key_alamat = "alamat";
    String key_tempat = "tempat";
    String key_tgllahir = "tgllahir";
    String key_gender = "gender";


    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String nama, String id_user, String role, String alamat, String tempat, String tgllahir, String gender){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(key_nama, nama);
        editor.putString(key_id_user, id_user);
        editor.putString(key_role, role);
        editor.putString(key_alamat, alamat);
        editor.putString(key_tempat, tempat);
        editor.putString(key_tgllahir, tgllahir);
        editor.putString(key_gender, gender);
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
            ((Activity) _context).finish();
        }else{
            HashMap<String, String> detail = getUserDetails();
            if (detail.get(key_role).equals("admin")){
                // user is not logged in redirect him to Login Activity
                Intent i = new Intent(_context, AdminActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Staring Login Activity
                _context.startActivity(i);
                ((Activity) _context).finish();
            }else{
                // user is not logged in redirect him to Login Activity
                Intent i = new Intent(_context, MainActivity.class);
                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Staring Login Activity
                _context.startActivity(i);
                ((Activity) _context).finish();
            }
        }
    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(key_nama, pref.getString(key_nama, ""));
        user.put(key_id_user, pref.getString(key_id_user, ""));
        user.put(key_role, pref.getString(key_role, ""));
        user.put(key_alamat, pref.getString(key_alamat, ""));
        user.put(key_tempat, pref.getString(key_tempat, ""));
        user.put(key_tgllahir, pref.getString(key_tgllahir, ""));
        user.put(key_gender, pref.getString(key_gender, ""));
        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // user is not logged in redirect him to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);
        ((Activity) _context).finish();
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
