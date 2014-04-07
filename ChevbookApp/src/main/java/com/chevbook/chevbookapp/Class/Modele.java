package com.chevbook.chevbookapp.Class;

import android.os.Environment;

import com.chevbook.chevbookapp.Adapter.MenuDrawerAdapter;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

import java.io.File;

/**
 * Created by Ugho on 27/01/14.
 */
public class Modele {

    //Variables
    private String DB4OFILENAME;
    private ObjectContainer db;
    private File appDir;
    private static User currentUser = null;
    private static MenuDrawerAdapter drawerAdapter;

    //Getter & Setter
    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public MenuDrawerAdapter getDrawerAdapter() {
        return drawerAdapter;
    }
    public void setDrawerAdapter(MenuDrawerAdapter drawerAdapter) {
        this.drawerAdapter = drawerAdapter;
    }

    //Constructeurs
    public Modele()
    {
    }

    //Méthodes
    public void initDB4O()
    {
        mkdir();
        open();
        close();
    }

    public void open()
    {
        DB4OFILENAME= Environment.getExternalStorageDirectory()+"/Chevbook"+"/chevbook_ppe4.db4o";
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB4OFILENAME);
    }

    public void close()
    {
        db.close();
    }

    public void mkdir()
    {
        appDir = new File(Environment.getExternalStorageDirectory()+"/Chevbook");

        if(!appDir.exists() && !appDir.isDirectory())
        {
            appDir.mkdirs();
        }
    }

    /*public void UserLogOut(Context c)
    {
        this.getCurrentUser().Delete(c);
    }

    public boolean UserExist(Context c)
    {
        //return true if User is connected
        boolean vretour = false;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);

        if(prefs.getString("pref_user_email", "") != "" && prefs.getString("pref_user_password", "") != "")
        {
            vretour = true;
        }

        return vretour;
    }*/

}
