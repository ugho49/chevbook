package com.example.chevbook.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.example.chevbook.Class.Modele;
import com.example.chevbook.Class.User;
import com.example.chevbook.Fragments.NavigationDrawerFragment;
import com.example.chevbook.R;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    final String[] fragments = {
            "com.example.chevbook.Fragments.FragmentMyAccount",
            "com.example.chevbook.Fragments.FragmentAnnonces",
            "com.example.chevbook.Fragments.FragmentMessages",
            "com.example.chevbook.Fragments.FragmentMyAnnonces",
            "com.example.chevbook.Fragments.FragmentFavoris",
            "com.example.chevbook.Fragments.FragmentSettingsContainer",
            "com.example.chevbook.Fragments.FragmentAbout",
            ""
    };

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private Modele vmodele;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);

        vmodele = new Modele();
        mUser = new User(getApplicationContext());

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        if(position == 7)
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which)
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.success_logout), Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myIntent);
                    //vmodele.UserLogOut(getApplicationContext());
                    mUser.logoutUser();
                    finish();
                }
            });
            adb.setNegativeButton(getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.cancel();
                }
            });
            adb.setMessage(getString(R.string.message_logout));
            adb.show();
        }
        else{
            fragmentManager.beginTransaction()
                    .replace(R.id.container, Fragment.instantiate(MainActivity.this, fragments[position]))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();


        }

    }

    public void onSectionAttached(int number) {
        //todo
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.

            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        EasyTracker.getInstance(this).activityStart(this);  // Start Google Analytics
    }

    @Override
    public void onStop() {
        super.onStop();

        EasyTracker.getInstance(this).activityStop(this);  // Stop Google Analytics
    }
}
