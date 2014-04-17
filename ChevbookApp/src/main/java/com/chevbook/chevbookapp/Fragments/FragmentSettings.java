package com.chevbook.chevbookapp.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.RingtonePreference;

import com.chevbook.chevbookapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;


public class FragmentSettings extends PreferenceFragment{

    private SharedPreferences prefs;
    private SharedPreferences.Editor prefs_editor;

    private SharedPreferences prefsUser;
    private PreferenceScreen screen;

    private Preference user_name;
    private Preference user_email;
    private Preference clean_cache;
    private Preference version_app;

    private CheckBoxPreference checkBoxPreferenceNotification;
    private CheckBoxPreference checkBoxPreferenceVibrate;
    private CheckBoxPreference checkBoxPreferenceSound;
    private RingtonePreference ringtonePreferenceSound;

    private static ImageLoader imageLoader;

    private int CptEasterEggs = 0;

    // All Shared Preferences
    private static final String KEY_EMAIL = "emailUserChevbook";
    private static final String KEY_PASSWORD_MD5 = "passwordMD5UserChevbook";
    private static final String KEY_FIRSTNAME = "firstnameUserChevbook";
    private static final String KEY_LASTNAME = "lastnameUserChevbook";
    private static final String KEY_URL_PROFIL_PICTURE = "urlProfilPictureUserChevbook";

    private static final int PRIVATE_MODE = 0;

    // Pref user
    private static final String PREFS_USER = "PrefsUserChevbook";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        imageLoader = ImageLoader.getInstance();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        prefs_editor = prefs.edit();
        prefsUser = getActivity().getApplicationContext().getSharedPreferences(PREFS_USER, PRIVATE_MODE);

        screen = getPreferenceScreen();

        user_name = (Preference) findPreference("pref_user_name");
        user_email = (Preference) findPreference("pref_user_email");
        clean_cache = (Preference) findPreference("pref_more_clean_cache");
        version_app = (Preference) findPreference("pref_more_version_app");

        checkBoxPreferenceNotification = (CheckBoxPreference) findPreference("pref_notification_on_or_off");
        checkBoxPreferenceVibrate = (CheckBoxPreference) findPreference("pref_notification_vibrate");
        checkBoxPreferenceSound = (CheckBoxPreference) findPreference("pref_notification_ringtone_on_or_off");
        ringtonePreferenceSound = (RingtonePreference) findPreference("pref_notification_ringtone");

        user_name.setSummary(prefsUser.getString(KEY_FIRSTNAME, "") + " " + prefsUser.getString(KEY_LASTNAME, ""));
        user_email.setSummary(prefsUser.getString(KEY_EMAIL, ""));
        //clean_cache.setSummary("Taille du cache ");

        try {
            Context c = getActivity().getApplicationContext();
            String versionName = c.getPackageManager()
                    .getPackageInfo(c.getPackageName(), 0).versionName;

            version_app.setSummary(versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        clean_cache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        imageLoader.clearDiscCache();
                        imageLoader.clearMemoryCache();
                    }
                });
                adb.setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                adb.setMessage("Voulez-vous vider le cache de l'application Chevbook ?");
                adb.show();

                return false;
            }
        });

        version_app.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            @Override
            public boolean onPreferenceClick(Preference preference) {
                /*CptEasterEggs++;
                if(CptEasterEggs == 5){
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setMessage("Vous avez découvert un easter eggs !");
                    adb.show();

                    //getActivity().finish();

                    PowerManager manager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock wl = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Your Tag");
                    wl.acquire();
                    wl.release();
                }*/
                return false;
            }
        });

        checkBoxPreferenceNotification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(prefs.getBoolean("pref_notification_on_or_off", false)){
                    prefs_editor.putBoolean("pref_notification_on_or_off", false);
                }else {
                    prefs_editor.putBoolean("pref_notification_on_or_off", true);
                }

                prefs_editor.commit();

                initCheckbox();

                return true;
            }
        });

        checkBoxPreferenceVibrate.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(prefs.getBoolean("pref_notification_vibrate", false)){
                    prefs_editor.putBoolean("pref_notification_vibrate", false);
                }else {
                    prefs_editor.putBoolean("pref_notification_vibrate", true);
                }

                prefs_editor.commit();

                initCheckbox();

                return true;
            }
        });

        checkBoxPreferenceSound.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(prefs.getBoolean("pref_notification_ringtone_on_or_off", false)){
                    prefs_editor.putBoolean("pref_notification_ringtone_on_or_off", false);
                }else {
                    prefs_editor.putBoolean("pref_notification_ringtone_on_or_off", true);
                }

                prefs_editor.commit();

                initCheckbox();

                return true;
            }
        });

        initCheckbox();
    }

    private void initCheckbox(){

        //set checked or not
        if(prefs.getBoolean("pref_notification_on_or_off", false)){ //si notif est à true
            checkBoxPreferenceNotification.setChecked(true);
        } else {
            checkBoxPreferenceNotification.setChecked(false);
        }

        if(prefs.getBoolean("pref_notification_vibrate", false)){ //si vibrate est à true
            checkBoxPreferenceVibrate.setChecked(true);
        } else {
            checkBoxPreferenceVibrate.setChecked(false);
        }

        if(prefs.getBoolean("pref_notification_ringtone_on_or_off", false)){ //si ringtone est à true
            checkBoxPreferenceSound.setChecked(true);
        } else {
            checkBoxPreferenceSound.setChecked(false);
        }

        //set enabled or not
        if(checkBoxPreferenceNotification.isChecked()){

            checkBoxPreferenceVibrate.setEnabled(true);
            checkBoxPreferenceSound.setEnabled(true);

            if(checkBoxPreferenceSound.isChecked()){
                ringtonePreferenceSound.setEnabled(true);
            }else {
                ringtonePreferenceSound.setEnabled(false);
            }
        }
        else {
            checkBoxPreferenceVibrate.setEnabled(false);
            checkBoxPreferenceSound.setEnabled(false);
            ringtonePreferenceSound.setEnabled(false);
        }
    }
}
