package com.chevbook.chevbookapp.GCM;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by Ugho on 15/04/2014.
 */
public class GcmUtils {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public GcmUtils() {
        //empty constructor
    }

    public boolean checkPlayServices(Activity activity) {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity.getApplicationContext());

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("GCM", "This device is not supported.");
            }
            return false;
        }
        return true;
    }
}
