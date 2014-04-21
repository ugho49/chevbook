package com.chevbook.chevbookapp.API;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.chevbook.chevbookapp.Activity.DetailsAccountActivity;
import com.chevbook.chevbookapp.Activity.LoginActivity;
import com.chevbook.chevbookapp.Fragments.FragmentMyAccount;
import com.chevbook.chevbookapp.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ugho on 19/04/2014.
 */

public class API_user extends API {

    private String firstname = "", lastname = "", url_image = "";
    private boolean userExist = true;

    public API_user(Activity activity) {
        super(activity);
    }

    public API_user(Fragment fragment) {
        super(fragment);
    }

    @Override
    protected boolean prepareJsonParam(){
        try {
            if(action.equals("password_oublie")){
                mUrl += resources.getString(R.string.URL_SERVEUR_FORGOT_PASSWORD_USER);
                jsonParam.put("email", mParams[1]);
            }

            if(action.equals("identification_user")){
                mUrl += resources.getString(R.string.URL_SERVEUR_IDENTIFICATION);
                jsonParam.put("email", mParams[1]);
                jsonParam.put("password", mParams[2]);
            }

            if(action.equals("modification_user")){
                mUrl += resources.getString(R.string.URL_SERVEUR_MODIFICATION_USER);
                AddParamUser();
                jsonParam.put("passwordMod", mParams[1]);
                jsonParam.put("prenomMod", mParams[2]);
                jsonParam.put("nomMod", mParams[3]);
                jsonParam.put("photoMod", mParams[4]);
            }

            if(action.equals("charge_donnees_user")){
                mUrl += resources.getString(R.string.URL_SERVEUR_IDENTIFICATION);
                AddParamUser();
            }

            return true;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(action.equals("password_oublie")){
            ((LoginActivity)mActivity).resultForgotPassword(success);
        }

        if(action.equals("identification_user")){
            ((LoginActivity)mActivity).resultUserLoginTask(success, firstname, lastname, url_image);
        }

        if(action.equals("modification_user")){
            ((DetailsAccountActivity)mActivity).resultUpdateUserTask(success, url_image);
        }

        if(action.equals("charge_donnees_user")){
            ((FragmentMyAccount)mFragment).resultLoadUserTask(success, userExist);
        }
    }

    @Override
    protected boolean interpreterResult(String mResult){
        try {
            if(action.equals("password_oublie")) {
                JSONArray jsonArray = new JSONArray(mResult);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                boolean recupOK = jsonObject.getBoolean("recupSuccess");

                if (recupOK) {
                    return true;
                } else {
                    return false;
                }
            }

            if(action.equals("modification_user")) {
                JSONObject jsonObject = new JSONObject(mResult);
                int modifOK = jsonObject.getInt("modifEffectue");
                if(modifOK == 1){
                    url_image = jsonObject.getString("0");

                    if(url_image.equals("null"))
                    {
                        url_image = "";
                    }

                    return true;
                } else {
                    return false;
                }
            }

            if(action.equals("charge_donnees_user")) {
                JSONArray jsonArray = new JSONArray(mResult);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                boolean user_exist = jsonObject.getBoolean("connectSuccess");

                if(user_exist) {

                    mUser.setFirstname(jsonObject.getString("Prenom_Personne"));
                    mUser.setLastname(jsonObject.getString("Nom_Personne"));
                    mUser.setUrlProfilPicture(jsonObject.getString("Avatar_Personne"));
                    mUser.setEmail(jsonObject.getString("Email"));
                    //int mNbAnnonces = jsonObject.getInt("Nb_annonces_Personne");;
                    userExist = true;
                    return true;
                }
                else {
                    //Utilisateur existe pas
                    userExist = false;
                    return false;
                }
            }

            if(action.equals("identification_user")) {
                JSONArray jsonArray = new JSONArray(mResult);
                JSONObject jsonObject = jsonArray.getJSONObject(0);

                boolean user_exist = jsonObject.getBoolean("connectSuccess");

                if (user_exist) {

                    firstname = jsonObject.getString("Prenom_Personne"); //prenom
                    lastname = jsonObject.getString("Nom_Personne"); //nom
                    url_image = jsonObject.getString("Avatar_Personne");

                    try {
                        String User_Phone_Id = "";
                        GoogleCloudMessaging gcm = ((LoginActivity)mActivity).getGcm();
                        if (gcm == null) {
                            gcm = GoogleCloudMessaging.getInstance(mActivity);
                        }
                        User_Phone_Id = gcm.register(resources.getString(R.string.GOOGLE_PROJECT_ID));

                        envoyerPhoneId(User_Phone_Id);

                    } catch (IOException ex) {
                        Log.i("GCM", ex.getMessage());
                    }

                    return true;
                } else {
                    return false;
                }
            }

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void envoyerPhoneId(String id) {
        if (!id.equals("")) {

            //Boolean testOK = false;
            HttpURLConnection urlConnection = null;
            StringBuilder sb = new StringBuilder();

            try {
                URL url = new URL(resources.getString(R.string.URL_SERVEUR) + resources.getString(R.string.URL_SERVEUR_SEND_PHONE_ID));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setConnectTimeout(5000);
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());

                // Création objet jsonn clé valeur
                JSONObject jsonParam = new JSONObject();
                // Exemple Clé valeur utiles à notre application
                jsonParam.put("email", mParams[1]);
                jsonParam.put("password", mParams[2]);
                jsonParam.put("phoneId", id);
                out.write(jsonParam.toString());
                out.flush();
                out.close();

                int HttpResult = urlConnection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();

                    String result = sb.toString();
                } else {
                    //return false;
                }
            } catch (MalformedURLException e) {
                //todo
            } catch (java.net.SocketTimeoutException e) {
                //todo
            } catch (IOException e) {
                //todo
            } catch (JSONException e) {
                //todo
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }
}
