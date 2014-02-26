package com.example.chevbook;

import android.app.Application;

import com.example.chevbook.Activity.LoginActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;

/**
 * Created by Ugho on 28/01/14.
 *
 * Initialize all the configuration in this class !!!
 */

public class Chevbook extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /*Modele vmodele = new Modele();

        if(vmodele.UserExist(this.getApplicationContext()))
        {
            vmodele.getCurrentUser().InstantiateByPrefs(this.getApplicationContext());
        }*/

        //Parse.initialize(this, "YOUR_APP_ID", "YOUR_CLIENT_KEY");
        Parse.initialize(this, getResources().getString(R.string.PARSE_APP_ID), getResources().getString(R.string.PARSE_CLIENT_KEY));

        PushService.setDefaultPushCallback(this, LoginActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);
    }
}
