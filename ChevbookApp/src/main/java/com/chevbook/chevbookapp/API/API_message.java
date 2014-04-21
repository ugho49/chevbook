package com.chevbook.chevbookapp.API;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.chevbook.chevbookapp.Class.Message;
import com.chevbook.chevbookapp.Fragments.FragmentMessagesTabs.FragmentMessagesReceived;
import com.chevbook.chevbookapp.Fragments.FragmentMessagesTabs.FragmentMessagesSent;
import com.chevbook.chevbookapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ugho on 19/04/2014.
 */
public class API_message extends API {

    private ArrayList<Message> mMessages = new ArrayList<Message>();

    public API_message(Activity activity) {
        super(activity);
    }

    public API_message(Fragment fragment) {
        super(fragment);
    }

    @Override
    protected boolean prepareJsonParam(){
        try {
            if(action.equals("messages_envoyees")){
                mUrl += resources.getString(R.string.URL_SERVEUR_MESSAGES_ENVOYEES);
                AddParamUser();
            }

            if(action.equals("messages_recu")){
                mUrl += resources.getString(R.string.URL_SERVEUR_MESSAGES_RECU);
                AddParamUser();
            }

            if(action.equals("lire_messages_recu")){
                mUrl += resources.getString(R.string.URL_SERVEUR_LIRE_MESSAGE);
                AddParamUser();
                jsonParam.put("EmailEmetteur", mParams[1]);
                jsonParam.put("Id_Annonce", mParams[2]);
                jsonParam.put("Date", mParams[3]);
            }

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(action.equals("messages_envoyees")){
            ((FragmentMessagesSent)mFragment).resultListerMessagesSent(success, mMessages);
        }

        if(action.equals("messages_recu")){
            ((FragmentMessagesReceived)mFragment).resultListerMessagesReceived(success, mMessages);
        }

        if(action.equals("lire_messages_recu")){
            ((FragmentMessagesReceived)mFragment).resultLireMessage(success, Integer.parseInt(mParams[4]));
        }
    }

    @Override
    protected boolean interpreterResult(String mResult){

        try {
            if(action.equals("lire_messages_recu")) {
                return true;
            }

            if(action.equals("messages_recu")) {
                JSONArray jsonArray = new JSONArray(mResult);

                if(jsonArray.length()>0){
                    for(int j = 0; j < jsonArray.length(); j++){

                        JSONObject jsonObject = jsonArray.getJSONObject(j);

                        int id_annonce_destinataire = jsonObject.getInt("Id_Annonce_Destinataire");
                        String email_emetteur = jsonObject.getString("Mail_Emetteur");
                        Date date_create_message = ConvertToDate(jsonObject.get("Date").toString());
                        String titre_annonce = jsonObject.getString("Titre_Annonce");
                        String contenu_message = jsonObject.getString("Message");
                        String nomPrenom_emetteur = jsonObject.getString("Prenom_Personne") + " " + jsonObject.getString("Nom_Personne").substring(0,1).toUpperCase();
                        String url_image_emetteur = jsonObject.getString("Avatar_Personne");
                        String nomPrenom_destinataire = mUser.getFirstName() + " " + mUser.getLastName().substring(0,1).toUpperCase();
                        String url_image_destinataire = mUser.getUrlProfilPicture();
                        Boolean est_lu = false;

                        if(jsonObject.getInt("Message_Lu") == 0){
                            est_lu = false;
                        }
                        else {
                            est_lu = true;
                        }

                        mMessages.add(new Message(id_annonce_destinataire,
                                email_emetteur,
                                date_create_message,
                                titre_annonce,
                                contenu_message,
                                nomPrenom_emetteur,
                                url_image_emetteur,
                                nomPrenom_destinataire,
                                url_image_destinataire,
                                est_lu));
                    }
                    return true;
                }
                else {
                    return false;
                }
            }

            if(action.equals("messages_envoyees")) {
                JSONArray jsonArray = new JSONArray(mResult);

                if(jsonArray.length()>0){
                    for(int j = 0; j < jsonArray.length(); j++){

                        JSONObject jsonObject = jsonArray.getJSONObject(j);

                        int id_annonce_destinataire = jsonObject.getInt("Id_Annonce_Destinataire");
                        String email_emetteur = mUser.getEmail();
                        Date date_create_message = ConvertToDate(jsonObject.get("Date").toString());
                        String titre_annonce = jsonObject.getString("Titre_Annonce");
                        String contenu_message = jsonObject.getString("Message");
                        String nomPrenom_emetteur = mUser.getFirstName() + " " + mUser.getLastName().substring(0,1).toUpperCase();
                        String url_image_emetteur = mUser.getUrlProfilPicture();
                        String nomPrenom_destinataire = jsonObject.getString("Prenom_Personne") + " " + jsonObject.getString("Nom_Personne").substring(0,1).toUpperCase();
                        String url_image_destinataire = jsonObject.getString("Avatar_Personne");
                        Boolean est_lu = false;

                        if(jsonObject.getInt("Message_Lu") == 0){
                            est_lu = false;
                        }
                        else {
                            est_lu = true;
                        }

                        mMessages.add(new Message(id_annonce_destinataire,
                                email_emetteur,
                                date_create_message,
                                titre_annonce,
                                contenu_message,
                                nomPrenom_emetteur,
                                url_image_emetteur,
                                nomPrenom_destinataire,
                                url_image_destinataire,
                                est_lu));
                    }
                    return true;
                }
                else {
                    return false;
                }
            }

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
