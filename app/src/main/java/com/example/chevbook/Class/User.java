package com.example.chevbook.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ugho on 28/01/14.
 */
public class User {

    //Variables
    private static String email;
    private static String passwordMD5;
    private static String nom;
    private static String prenom;
    private static String url_image;

    private static SharedPreferences prefs;
    private static SharedPreferences.Editor prefsEditor;

    //Constructeurs
    public User() {
    }

    public User(String vemail, String vpassword, String vnom, String vprenom, String vurl_img) {
        email = vemail;
        passwordMD5 = getMd5(vpassword);
        nom = vnom;
        prenom = vprenom;
        url_image = vurl_img;
    }

    //Méthodes
    public void InstantiateByPrefs(Context vcontext)
    {
        initPrefs(vcontext);

        email = prefs.getString("pref_user_email", "");
        passwordMD5 = prefs.getString("pref_user_password","");
        prenom = prefs.getString("pref_user_firstname", "");
        nom = prefs.getString("pref_user_lastname", "");
        url_image = prefs.getString("pref_user_url_image", "");
    }

    public void SaveInPrefs(Context vcontext)
    {
        initPrefs(vcontext);
        prefsEditor.putString("pref_user_name", this.getPrenom() + " " + this.getNom());
        prefsEditor.putString("pref_user_firstname", this.getPrenom());
        prefsEditor.putString("pref_user_lastname", this.getNom());
        prefsEditor.putString("pref_user_password", this.getPasswordMD5());
        prefsEditor.putString("pref_user_url_image", this.getUrl_image());
        prefsEditor.putString("pref_user_email", this.getEmail());
        prefsEditor.commit();
    }

    public void Delete(Context vcontext)
    {
        initPrefs(vcontext);
        prefsEditor.putString("pref_user_name", "");
        prefsEditor.putString("pref_user_firstname", "");
        prefsEditor.putString("pref_user_lastname", "");
        prefsEditor.putString("pref_user_password", "");
        prefsEditor.putString("pref_user_url_image", "");
        prefsEditor.putString("pref_user_email", "");
        prefsEditor.commit();

        email = "";
        passwordMD5 = "";
        prenom = "";
        nom = "";
        url_image = "";
    }

    public void initPrefs(Context vcontext)
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(vcontext);
        prefsEditor = prefs.edit();
    }

    public static String getMd5(String input)
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

    //Getters && Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordMD5() {
        return passwordMD5;
    }

    public void setPasswordMD5(String passwordMD5) {
        this.passwordMD5 = passwordMD5;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }
}
