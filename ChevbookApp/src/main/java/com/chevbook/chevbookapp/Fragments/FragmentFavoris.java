package com.chevbook.chevbookapp.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chevbook.chevbookapp.Activity.DetailsAnnonceActivity;
import com.chevbook.chevbookapp.Adapter.ListViewFavorisAdapter;
import com.chevbook.chevbookapp.Class.Annonce;
import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.R;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class FragmentFavoris extends Fragment implements OnRefreshListener {

    @InjectView(R.id.listViewFavoris)
    ListView mListViewFavoris;

    private PullToRefreshLayout mPullToRefreshLayout;
    private ActionBarActivity actionBarActivity;

    private ListViewFavorisAdapter Adapter;
    private Boolean onResume = false;
    private User mUser;

    private ArrayList<Annonce> mAnnonces = new ArrayList<Annonce>();

    public FragmentFavoris() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favoris, container, false);
        ButterKnife.inject(this, rootView);

        actionBarActivity = (ActionBarActivity) getActivity();
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        actionBar.setTitle(mNavigationTitles[4]);

        mUser = new User(getActivity().getApplicationContext());

        // Now find the PullToRefreshLayout to setup
        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout_list_my_favoris);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        mListViewFavoris.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intentDetailAppartement = new Intent(getActivity(), DetailsAnnonceActivity.class);
                intentDetailAppartement.putExtra("annonce", mAnnonces.get(position));
                intentDetailAppartement.putExtra("is_favoris", true);
                startActivity(intentDetailAppartement);
            }

        });

        Adapter = new ListViewFavorisAdapter(getActivity(), getActivity().getBaseContext(), mAnnonces);
        mListViewFavoris.setAdapter(Adapter);

        if(onResume == false) {
            listerMyFavoris();
        }
        else {
            onResume = false;
        }

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();

        onResume = true;
    }

    @Override
    public void onRefreshStarted(View view) {

        mAnnonces.clear();
        mAnnonces = new ArrayList<Annonce>();

        listerMyFavoris();
    }

    public void listerMyFavoris()
    {
        new AsyncTask<Void, Void, Boolean>() {

            String AfficherJSON = null;
            String ErreurLoginTask = "Erreur ";
            int annonceChargeesInThisTask;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                HttpURLConnection urlConnection = null;
                StringBuilder sb = new StringBuilder();

                try {
                    URL url = new URL(getResources().getString(R.string.URL_SERVEUR) + getResources().getString(R.string.URL_SERVEUR_LIST_MY_FAVORIS));
                    urlConnection = (HttpURLConnection)url.openConnection();
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoOutput(true);
                    urlConnection.setConnectTimeout(5000);
                    OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());

                    // Création objet jsonn clé valeur
                    JSONObject jsonParam = new JSONObject();
                    // Exemple Clé valeur utiles à notre application
                    jsonParam.put("email", mUser.getEmail());
                    jsonParam.put("password", mUser.getPasswordSha1());
                    out.write(jsonParam.toString());
                    out.flush();
                    out.close();

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

                        AfficherJSON = sb.toString();

                        JSONArray jsonArray = new JSONArray(sb.toString());
                        annonceChargeesInThisTask = jsonArray.length();

                        if(annonceChargeesInThisTask>0){
                            for(int j = 0; j < annonceChargeesInThisTask; j++){

                                //AfficherJSON = jsonArray.getJSONObject(j).toString();
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                Date date_create_annonce = null;

                                date_create_annonce = ConvertToDate(jsonObject.get("Date_Ajout_Annonce").toString());

                                //Date date_create_annonce = new Date();

                                int id_annonce = jsonObject.getInt("Id_Annonce");
                                String titre_annonce = jsonObject.getString("Titre_Annonce");

                                double prix_annonce = jsonObject.getDouble("Prix_Annonce");
                                String description_annonce = jsonObject.getString("Description_Annonce");
                                String email_user_annonce = jsonObject.getString("E_mail_Personne_Annonce");
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
                    }
                    else
                    {
                        return false;
                    }
                }
                catch (MalformedURLException e){
                    ErreurLoginTask = ErreurLoginTask + "URL";
                    return false; //Erreur URL
                } catch (java.net.SocketTimeoutException e) {
                    ErreurLoginTask = ErreurLoginTask + "Temps trop long";
                    return false; //Temps trop long
                } catch (IOException e) {
                    ErreurLoginTask = ErreurLoginTask + "Connexion internet lente ou inexistante";
                    return false; //Pas de connexion internet
                } catch (JSONException e) {
                    ErreurLoginTask = ErreurLoginTask + "Problème de JSON";
                    return false; //Erreur JSON
                } finally {
                    if (urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }

            @Override
            protected void onPostExecute(final Boolean result) {

                if (result)
                {
                    //Toast.makeText(getActivity(), "reussite", Toast.LENGTH_SHORT).show();
                    Adapter.setList(mAnnonces);
                    Adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity(), "erreur", Toast.LENGTH_SHORT).show();
                }

                /*AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setNegativeButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                adb.setMessage(AfficherJSON);
                adb.show();*/

                // Notify PullToRefreshLayout that the refresh has finished
                actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);
                mPullToRefreshLayout.setRefreshComplete();
            }
        }.execute();
    }

    private Date ConvertToDate(String dateString){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new Date();
        }
    }
}
