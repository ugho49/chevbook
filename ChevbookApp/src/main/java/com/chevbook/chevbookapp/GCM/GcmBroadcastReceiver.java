package com.chevbook.chevbookapp.GCM;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.chevbook.chevbookapp.Class.User;

/**
 * Created by Ugho on 15/04/2014.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    private User mUser;
    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        mUser = new User(context);
        preferences= PreferenceManager.getDefaultSharedPreferences(context);

        if(mUser.isLoggedIn()){ //notif que si l'utilisateur est connecté

            if(preferences.getBoolean("pref_notification_on_or_off", false)){ //notif que si accepté dans les prefs
                // Explicitly specify that GcmIntentService will handle the intent.
                ComponentName comp = new ComponentName(context.getPackageName(),
                        GcmIntentService.class.getName());

                // Start the service, keeping the device awake while it is launching.
                startWakefulService(context, (intent.setComponent(comp)));
                setResultCode(Activity.RESULT_OK);
            }
        }
    }
}
