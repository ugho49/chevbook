package com.chevbook.chevbookapp.API;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.chevbook.chevbookapp.Activity.DetailsAnnonceActivity;
import com.chevbook.chevbookapp.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ugho on 19/04/2014.
 */

public class API_favoris extends API {

    public API_favoris(Activity activity) {
        super(activity);
    }

    public API_favoris(Fragment fragment) {
        super(fragment);
    }

    @Override
    protected boolean prepareJsonParam(){
        try {
            if(action.equals("est_favoris")){
                mUrl += resources.getString(R.string.URL_SERVEUR_IS_FAVORIS);
                AddParamUser();
                jsonParam.put("id_annonce", Integer.parseInt(mParams[1]));
            }

            if(action.equals("mettre_enlever_favoris")){
                mUrl += resources.getString(R.string.URL_SERVEUR_SET_FAVORIS);
                AddParamUser();
                jsonParam.put("id_annonce", Integer.parseInt(mParams[1]));
                jsonParam.put("get_set", Integer.parseInt(mParams[2])); // 0 = delete favoris, 1 = add favoris
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
        if(action.equals("est_favoris")){
            ((DetailsAnnonceActivity)mActivity).resultIsFavorisTask(success);
        }

        if(action.equals("mettre_enlever_favoris")){
            ((DetailsAnnonceActivity)mActivity).resultAddOrDeleteFavorisTask(success);
        }
    }

    @Override
    protected boolean interpreterResult(String mResult){

        try {
            if(action.equals("est_favoris")) {
                JSONObject jsonObject = new JSONObject(mResult);
                Boolean fav = jsonObject.getBoolean("est_Favoris");

                if(fav){
                    return true;
                } else {
                    return false;
                }
            }

            if(action.equals("mettre_enlever_favoris")) {
                JSONObject jsonObject = new JSONObject(mResult);
                Boolean fav = jsonObject.getBoolean("modifReussi");

                if(fav){
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
}
