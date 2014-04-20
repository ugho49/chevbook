package com.chevbook.chevbookapp.API;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.chevbook.chevbookapp.Activity.DeposerModifierAnnonceActivity;
import com.chevbook.chevbookapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ugho on 20/04/2014.
 */
public class API_spinner extends API {

    private String [] SpinnerListQuartier;
    private String [] SpinnerListCategorie;
    private String [] SpinnerListSousCategorie;
    private String [] SpinnerListType;

    public API_spinner(Activity activity) {
        super(activity);
    }

    public API_spinner(Fragment fragment) {
        super(fragment);
    }

    @Override
    protected boolean prepareJsonParam(){
        if(action.equals("lister_spinner_annonce")){
            mUrl += resources.getString(R.string.URL_SERVEUR_LIST_SPINNER);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(action.equals("lister_spinner_annonce")){
            ((DeposerModifierAnnonceActivity)mActivity).setSpinnerListCategorie(SpinnerListCategorie);
            ((DeposerModifierAnnonceActivity)mActivity).setSpinnerListQuartier(SpinnerListQuartier);
            ((DeposerModifierAnnonceActivity)mActivity).setSpinnerListSousCategorie(SpinnerListSousCategorie);
            ((DeposerModifierAnnonceActivity)mActivity).setSpinnerListType(SpinnerListType);
            ((DeposerModifierAnnonceActivity)mActivity).remplirSpinner(success);
        }
    }

    @Override
    protected boolean interpreterResult(String mResult){

        try {
            if(action.equals("lister_spinner_annonce")) {
                JSONObject jsonObject = new JSONObject(sb.toString());
                JSONArray jsonArrayCategorie = jsonObject.getJSONArray("categorie");
                JSONArray jsonArrayQuartier = jsonObject.getJSONArray("quartier");
                JSONArray jsonArraySousCategorie = jsonObject.getJSONArray("sous_categorie");
                JSONArray jsonArrayType = jsonObject.getJSONArray("type_location");

                if (jsonArrayCategorie.length() > 0) {
                    SpinnerListCategorie = new String[jsonArrayCategorie.length()];
                    for (int i = 0; i < jsonArrayCategorie.length(); i++) {
                        JSONObject Object = jsonArrayCategorie.getJSONObject(i);
                        SpinnerListCategorie[i] = Object.getString("Libelle_Ctagorie");
                    }
                }

                if (jsonArrayQuartier.length() > 0) {
                    SpinnerListQuartier = new String[jsonArrayQuartier.length()];
                    for (int i = 0; i < jsonArrayQuartier.length(); i++) {
                        JSONObject Object = jsonArrayQuartier.getJSONObject(i);
                        SpinnerListQuartier[i] = Object.getString("Libelle_Quartier");
                    }
                }

                if (jsonArraySousCategorie.length() > 0) {
                    SpinnerListSousCategorie = new String[jsonArraySousCategorie.length()];
                    for (int i = 0; i < jsonArraySousCategorie.length(); i++) {
                        JSONObject Object = jsonArraySousCategorie.getJSONObject(i);
                        SpinnerListSousCategorie[i] = Object.getString("Libelle_Sous_Categorie");
                    }
                }

                if (jsonArrayType.length() > 0) {
                    SpinnerListType = new String[jsonArrayType.length()];
                    for (int i = 0; i < jsonArrayType.length(); i++) {
                        JSONObject Object = jsonArrayType.getJSONObject(i);
                        SpinnerListType[i] = Object.getString("Libelle_Type_Location");
                    }
                }

                return true;
            }
            else {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
