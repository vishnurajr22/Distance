package com.app.distance.CommonDataArea;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import android.util.Log;

import com.app.distance.Activities.AddPlacesActivity;

public class CommonFunctions {


public static AddPlacesActivity addPlacesActivity;
public static Context context=addPlacesActivity.getApplicationContext();
    public static Boolean internetcheck(){
        if (addPlacesActivity == null) return false;
        ConnectivityManager cm =
                (ConnectivityManager) addPlacesActivity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else
            return false;
    }


    public String uuid(){

       String uuid= Settings.Secure.getString(addPlacesActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
        return uuid;
    }


}
