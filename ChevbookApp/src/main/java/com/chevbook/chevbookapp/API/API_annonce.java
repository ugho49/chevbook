package com.chevbook.chevbookapp.API;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.chevbook.chevbookapp.Class.Annonce;
import com.chevbook.chevbookapp.Fragments.FragmentAnnonces;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Ugho on 19/04/2014.
 */
public class API_annonce extends API {

    private ArrayList<Annonce> mAnnonces = new ArrayList<Annonce>();
    private int AnnonceMax = 0;
    private int annonceChargeesInThisTask = 0;

    public API_annonce(Activity activity) {
        super(activity);
    }

    public API_annonce(Fragment fragment) {
        super(fragment);
    }

    @Override
    protected void onPostExecute (Boolean result) {

        if(action.equals("lister_annonce")){
            ((FragmentAnnonces)mFragment).setAnnonceMax(AnnonceMax);
            ((FragmentAnnonces)mFragment).resultListerAnnonce(result, mAnnonces, Integer.parseInt(mParams[1]), annonceChargeesInThisTask);
        }

    }

    @Override
    protected boolean interpreterResult(String mResult){

        try {
            JSONArray jsonArray = new JSONArray(sb.toString());

            AnnonceMax = jsonArray.getJSONArray(0).getJSONObject(0).getInt("Nb");

            JSONArray listAnnonces = jsonArray.getJSONArray(1); //contient des JSON objets

            annonceChargeesInThisTask = listAnnonces.length();

            if(annonceChargeesInThisTask>0){
                for(int j = 0; j < annonceChargeesInThisTask; j++){

                    JSONObject jsonObject = listAnnonces.getJSONObject(j);

                    Date date_create_annonce = null;
                    date_create_annonce = ConvertToDate(jsonObject.get("Date_Ajout_Annonce").toString());

                    int id_annonce = jsonObject.getInt("Id_Annonce");
                    String titre_annonce = jsonObject.getString("Titre_Annonce");

                    double prix_annonce = jsonObject.getDouble("Prix_Annonce");
                    String description_annonce = jsonObject.getString("Description_Annonce");
                    String email_user_annonce = jsonObject.getString("E_mail_Personne_Annonce");
                    String pseudo_user_annonce = jsonObject.getString("Prenom_Personne") + " " + jsonObject.getString("Nom_Personne").substring(0,1).toUpperCase();
                    String avatar_user_annonce = jsonObject.getString("Avatar_Personne");
                    int number_room_annonce = jsonObject.getInt("Nb_Pieces_Annonce");
                    int surface_annonce = jsonObject.getInt("Surface_Annonce");
                    String adresse_annonce = jsonObject.getString("Adresse_Annonce");
                    String categorie_annonce = jsonObject.getString("Libelle_Categorie");
                    String sous_categorie_annonce = jsonObject.getString("Libelle_Sous_Categorie");
                    String type_location_annonce = jsonObject.getString("Libelle_Type_Location");
                    String quartier_annonce = jsonObject.getString("Libelle_Quartier");


                    int b = Integer.parseInt(jsonObject.get("Est_Meuble").toString());

                    boolean est_meuble;
                    if(b == 0){est_meuble = false;}
                    else {est_meuble = true;}

                    ArrayList<String> url_images_annonces = new ArrayList<String>();

                    JSONArray arr = jsonObject.getJSONArray("listeImage");
                    if(arr.length()>0){
                        for(int z = 0; z < arr.length(); z++){
                            url_images_annonces.add(arr.getString(z));
                        }
                    }

                    mAnnonces.add(new Annonce(id_annonce,
                            date_create_annonce,
                            titre_annonce,
                            prix_annonce,
                            description_annonce,
                            email_user_annonce,
                            pseudo_user_annonce,
                            avatar_user_annonce,
                            number_room_annonce,
                            surface_annonce,
                            adresse_annonce,
                            categorie_annonce,
                            sous_categorie_annonce,
                            type_location_annonce,
                            quartier_annonce,
                            est_meuble,
                            url_images_annonces));
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
