package com.chevbook.chevbookapp.API;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ugho on 19/04/2014.
 */

public abstract class API extends AsyncTask<String, Void, Boolean> {

    //Variables
    protected WeakReference<Activity> mActivity = null;
    protected Fragment mFragment = null;
    protected Resources resources = null;
    protected String mClassActivity = null;
    protected String mClassFragment = null;

    protected String action = null;
    protected String mUrl = "";

    protected String[] mParams;

    protected User mUser;

    protected URL url_object = null;
    protected HttpURLConnection urlConnection = null;
    protected OutputStreamWriter out = null;
    protected JSONObject jsonParam = new JSONObject();

    protected StringBuilder sb = new StringBuilder();

    //Constructor
    public API (Activity activity) {
        mActivity = new WeakReference<Activity>(activity);
        resources = activity.getResources();
        mClassActivity = activity.getClass().toString();
        mUser = new User(activity.getApplicationContext());
    }

    public API (Fragment fragment) {
        mActivity = new WeakReference<Activity>(fragment.getActivity());
        mFragment = fragment;
        resources = fragment.getActivity().getResources();
        mClassActivity = fragment.getActivity().getClass().toString();
        mClassFragment = ((Object) fragment).getClass().getSimpleName();
        mUser = new User(fragment.getActivity().getApplicationContext());
    }

    //Méthodes
    protected Date ConvertToDate(String dateString){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    private boolean prepareAsyncTask(){
        Boolean mReturn = true;
        mUrl = resources.getString(R.string.URL_SERVEUR);

        if(!prepareJsonParam())
            mReturn = false;
        if(!prepareURLConnection())
            mReturn = false;
        if(!prepareOutputStreamWriter())
            mReturn = false;

        return mReturn;
    }

    protected Boolean AddParamUser(){
        try {
            jsonParam.put("email", mUser.getEmail());
            jsonParam.put("password", mUser.getPasswordSha1());
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean prepareJsonParam(){ //override methode
        return true;
    }

    private boolean prepareURLConnection(){
        try {
            url_object = new URL(mUrl);
            urlConnection = (HttpURLConnection)url_object.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(5000);

            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean prepareOutputStreamWriter(){
        try {
            out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(jsonParam.toString());
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean interpreterResult(String mResult){ //override methode
        return true;
    }

    @Override
    protected Boolean doInBackground (String... params) {

        action = params[0];
        mParams = new String[params.length];

        for (int i=0; i< params.length; i++){
            mParams[i] = params[i];
        }

        try {
            if(!prepareAsyncTask())
                return false;

            // récupération du serveur
            int HttpResult = urlConnection.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                if(interpreterResult(sb.toString())){
                    return true;
                } else {
                    return false;
                }
            }
            else
            {
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
        }
    }
}
