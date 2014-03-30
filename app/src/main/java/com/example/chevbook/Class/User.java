package com.example.chevbook.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;

/**
 * Created by Ugho on 26/02/14.
 */
public class User {

    // Shared Preferences
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Context _context;

    private static final int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "PrefsUserChevbook";

    // All Shared Preferences
    private static final String KEY_EMAIL = "emailUserChevbook";
    private static final String KEY_PASSWORD_SHA1 = "passwordSha1UserChevbook";
    private static final String KEY_FIRSTNAME = "firstnameUserChevbook";
    private static final String KEY_LASTNAME = "lastnameUserChevbook";
    private static final String KEY_URL_PROFIL_PICTURE = "urlProfilPictureUserChevbook";

    // All Other Shared Preferences
    private static final String IS_LOGIN = "IsLoggedIn";


    public User(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void loginUser(String email, String password){

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD_SHA1, getSha1(password));

        editor.commit();
    }

    public void loginUser(String email, String password, String firstname, String lastname, String url_picture){

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD_SHA1, getSha1(password));
        editor.putString(KEY_FIRSTNAME, firstname);
        editor.putString(KEY_LASTNAME, lastname);
        editor.putString(KEY_URL_PROFIL_PICTURE, url_picture);

        editor.commit();
    }

    public void logoutUser(){
        editor.clear();

        editor.putBoolean(IS_LOGIN, false);

        editor.putString(KEY_EMAIL, "");
        editor.putString(KEY_PASSWORD_SHA1, "");
        editor.putString(KEY_FIRSTNAME, "");
        editor.putString(KEY_LASTNAME, "");
        editor.putString(KEY_URL_PROFIL_PICTURE, "");

        editor.commit();

        /*editor.putBoolean(IS_LOGIN, false);
        editor.commit();*/
    }

    public Boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name

        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PASSWORD_SHA1, pref.getString(KEY_PASSWORD_SHA1, null));
        user.put(KEY_FIRSTNAME, pref.getString(KEY_FIRSTNAME, null));
        user.put(KEY_LASTNAME, pref.getString(KEY_LASTNAME, null));
        user.put(KEY_URL_PROFIL_PICTURE, pref.getString(KEY_URL_PROFIL_PICTURE, null));

        // return user
        return user;
    }

    // Getters
    public String getEmail()
    {
        return pref.getString(KEY_EMAIL, null);
    }

    public String getPasswordSha1()
    {
        return pref.getString(KEY_PASSWORD_SHA1, null);
    }

    public String getFirstName()
    {
        return pref.getString(KEY_FIRSTNAME, null);
    }

    public String getLastName()
    {
        return pref.getString(KEY_LASTNAME, null);
    }

    public String getUrlProfilPicture()
    {
        return pref.getString(KEY_URL_PROFIL_PICTURE, null);
    }

    // Setters
    public void setEmail(String email)
    {
        editor.putString(KEY_EMAIL, email);
    }

    public void setPassword(String password)
    {
        editor.putString(KEY_PASSWORD_SHA1, getSha1(password));
    }

    public void setFirstname(String firstname)
    {
        editor.putString(KEY_FIRSTNAME, firstname);
    }

    public void setLastname(String lastname)
    {
        editor.putString(KEY_LASTNAME, lastname);
    }

    public void setUrlProfilPicture(String url)
    {
        editor.putString(KEY_URL_PROFIL_PICTURE, url);
    }

    //Methode MD5
    public static String getMD5(String input)
    {
        //Permet de crypter une chaîne donnée en MD5
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
        }
    }

    private static String getSha1(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
