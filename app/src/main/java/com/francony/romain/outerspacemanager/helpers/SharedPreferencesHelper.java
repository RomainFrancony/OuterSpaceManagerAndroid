package com.francony.romain.outerspacemanager.helpers;


import android.content.Context;
import android.content.SharedPreferences;


/**
 * Shared preference helpers
 * will be removed in future and using DB Flow storage instead
 */
public abstract class SharedPreferencesHelper {
    private static String SHARED_PREF_REF = "outerspacemanager";
    private static String TOKEN_REF = "userToken";
    private static String TOKENEXPIRATION_REF = "userTokenExpiration";

    public static String getToken(Context ctx){
        SharedPreferences prefs = ctx.getSharedPreferences(SHARED_PREF_REF, 0);
        return prefs.getString(TOKEN_REF, null);
    }

    public static void setToken(Context ctx, String token){
        SharedPreferences prefs = ctx.getSharedPreferences(SHARED_PREF_REF, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_REF, token);
        editor.commit();
    }

    public static void clearToken(Context ctx){
        SharedPreferences prefs = ctx.getSharedPreferences(SHARED_PREF_REF, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(TOKEN_REF);
        editor.commit();
    }

    public static long getTokenExpiration(Context ctx){
        SharedPreferences prefs = ctx.getSharedPreferences(SHARED_PREF_REF, 0);
        return prefs.getLong(TOKENEXPIRATION_REF, 0);
    }

    public static void setTokenexpiration(Context ctx, Long time){
        SharedPreferences prefs = ctx.getSharedPreferences(SHARED_PREF_REF, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(TOKENEXPIRATION_REF, time);
        editor.commit();
    }

    public static void clearTokenExpiration(Context ctx){
        SharedPreferences prefs = ctx.getSharedPreferences(SHARED_PREF_REF, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(TOKENEXPIRATION_REF);
        editor.commit();
    }

}
