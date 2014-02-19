package com.example.chevbook.Activity;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chevbook.R;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class SettingsActivity extends PreferenceActivity{

    private SharedPreferences prefs;
    private PreferenceScreen screen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {  //Build.VERSION_CODES.ICE_CREAM_SANDWICH
            getActionBar().setDisplayHomeAsUpEnabled(true); // show back arrow on title icon
        }
        onCreatePreferenceActivity();
    }

    @SuppressWarnings("deprecation")
    private void onCreatePreferenceActivity() {
        addPreferencesFromResource(R.xml.preferences);
        screen = getPreferenceScreen();

        Preference user_name = (Preference) findPreference("pref_user_name");
        Preference user_firstname = (Preference) findPreference("pref_user_firstname");
        Preference user_lastname = (Preference) findPreference("pref_user_lastname");
        Preference user_email = (Preference) findPreference("pref_user_email");
        Preference user_url_image = (Preference) findPreference("pref_user_url_image");
        Preference user_password = (Preference) findPreference("pref_user_password");
        Preference more_assistance = (Preference) findPreference("pref_more_assistance");
        Preference more_privacy = (Preference) findPreference("pref_more_privacy");
        Preference more_conditions = (Preference) findPreference("pref_more_conditions");

        user_name.setSummary(prefs.getString("pref_user_name", ""));
        user_email.setSummary(prefs.getString("pref_user_email", ""));
        screen.removePreference(user_url_image);
        screen.removePreference(user_password);
        screen.removePreference(user_firstname);
        screen.removePreference(user_lastname);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        this.finish();

        Toast.makeText(getApplicationContext(), getString(R.string.settings_saved), Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

}
