package com.app.distance.CommonDataArea;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import android.util.Log;
import android.widget.Toast;

import com.app.distance.Activities.AddPlacesActivity;

public class CommonFunctions {
    Context context;

    CommonFunctions(Context context){
        this.context=context;
    }
public static ProgressDialog showprogressdialog(Context context,String message){
    ProgressDialog dialog = new ProgressDialog(context);
    dialog.setMessage(message);
    dialog.show();
    return dialog;
}

    public static boolean isOnline(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
           // Toast.makeText(context, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
