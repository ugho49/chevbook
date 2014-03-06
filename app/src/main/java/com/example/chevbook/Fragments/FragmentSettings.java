package com.example.chevbook.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.example.chevbook.R;
import com.nostra13.universalimageloader.core.ImageLoader;


public class FragmentSettings extends PreferenceFragment{

    private SharedPreferences prefs;
    private SharedPreferences prefsUser;
    private PreferenceScreen screen;

    private static ImageLoader imageLoader;

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
        prefsUser = getActivity().getApplicationContext().getSharedPreferences(PREFS_USER, PRIVATE_MODE);

        screen = getPreferenceScreen();

        Preference user_name = (Preference) findPreference("pref_user_name");
        Preference user_email = (Preference) findPreference("pref_user_email");

        Preference clean_cache = (Preference) findPreference("pref_more_clean_cache");

        user_name.setSummary(prefsUser.getString(KEY_FIRSTNAME, "") + " " + prefsUser.getString(KEY_LASTNAME, ""));
        user_email.setSummary(prefsUser.getString(KEY_EMAIL, ""));
        clean_cache.setSummary("Taille du cache " + imageLoader.getMemoryCache().toString());

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
    }

    //@SuppressWarnings("deprecation")
    /*private void onCreatePreferenceActivity() {
        addPreferencesFromResource(R.xml.preferences);
        screen = getPreferenceScreen();

        Preference user_name = (Preference) findPreference("pref_user_name");
        Preference user_email = (Preference) findPreference("pref_user_email");

        /*Preference user_firstname = (Preference) findPreference("pref_user_firstname");
        Preference user_lastname = (Preference) findPreference("pref_user_lastname");
        Preference user_url_image = (Preference) findPreference("pref_user_url_image");
        Preference user_password = (Preference) findPreference("pref_user_password");*/

        /*Preference more_assistance = (Preference) findPreference("pref_more_assistance");
        Preference more_privacy = (Preference) findPreference("pref_more_privacy");
        Preference more_conditions = (Preference) findPreference("pref_more_conditions");

        user_name.setSummary(prefsUser.getString(KEY_FIRSTNAME, "") + " " + prefsUser.getString(KEY_LASTNAME, ""));
        user_email.setSummary(prefsUser.getString(KEY_EMAIL, ""));*/
        /*screen.removePreference(user_url_image);
        screen.removePreference(user_password);
        screen.removePreference(user_firstname);
        screen.removePreference(user_lastname);

    }*/

}
