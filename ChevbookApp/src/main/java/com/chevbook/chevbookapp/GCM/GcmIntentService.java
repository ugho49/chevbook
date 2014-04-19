package com.chevbook.chevbookapp.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.chevbook.chevbookapp.Activity.MainActivity;
import com.chevbook.chevbookapp.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ugho on 15/04/2014.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    private SharedPreferences preferences;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                //sendNotification("Deleted messages on server: " + extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                sendNotification(extras.toString());
                //Log.i("gcm", "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String sb) {

        String title = ""; //Une annonce à été déposé
        String msg = "";

        try {
            JSONArray bundle = new JSONArray(sb.substring(6));
            JSONObject content = bundle.getJSONObject(0);
            title = content.getString("title");
            msg = content.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.small_logo)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.InboxStyle().addLine(msg))
                        .setLights(Color.MAGENTA, 3000, 3000); //int argb, int onMs, int offMs
                        //.setContentText(msg);

        if(preferences.getBoolean("pref_notification_vibrate", false)) { //vibreur que si accepté dans les prefs
            mBuilder.setVibrate(new long[] {1000, 1000, 1000});
        }

        if(preferences.getBoolean("pref_notification_ringtone_on_or_off", false)) { //son que si accepté dans les prefs
            String strRingtonePreference = preferences.getString("pref_notification_ringtone", "DEFAULT_SOUND");
            mBuilder.setSound(Uri.parse(strRingtonePreference));
        }


        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


    }
}
