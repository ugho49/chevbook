package com.example.chevbook.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chevbook.Activity.DetailsAnnonceActivity;
import com.example.chevbook.Adapter.ListViewAnnonceAdapter;
import com.example.chevbook.Class.Annonce;
import com.example.chevbook.Class.Modele;
import com.example.chevbook.R;

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
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class FragmentAnnonces extends Fragment implements OnRefreshListener {

    @InjectView(R.id.editTextSearch)
    EditText mEditTextSearch;
    @InjectView(R.id.imageViewSearchMoreDetail)
    ImageView mImageViewSearchMoreDetail;
    @InjectView(R.id.imageViewSearch)
    ImageView mImageViewSearch;
    @InjectView(R.id.listViewSearch)
    ListView mListViewSearch;
    @InjectView(R.id.linearLayoutSearchAppartementLoading)
    LinearLayout mLinearLayoutSearchAppartementLoading;
    @InjectView(R.id.linearLayoutSearchAppartementNoResult)
    LinearLayout mLinearLayoutSearchAppartementNoResult;

    private int AnnonceMax = 0;
    private int AnnonceChargees = 0;

    //Custom Dialog
    private static EditText EditTextKeyWord;
    private static EditText EditTextPrixMin;
    private static EditText EditTextPrixMax;
    private static EditText mEditTextPieceMin;
    private static EditText mEditTextPieceMax;
    private static Spinner mSpinnerQuartier;
    private static Spinner mSpinnerType;
    private static CheckBox mCheckBoxMaison;
    private static CheckBox mCheckBoxAppartements;
    private static CheckBox mCheckBoxTerrain;
    private static CheckBox mCheckBoxParking;
    private static Button mButtonSearch;

    private static String [] AppartementListQuartier;
    private static String [] AppartementListType;

    private Modele vmodele;
    private ListViewAnnonceAdapter Adapter;

    private ArrayList<Annonce> mAnnonces = new ArrayList<Annonce>();

    private PullToRefreshLayout mPullToRefreshLayout;
    private static LayoutInflater mInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vmodele = new Modele();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //init
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_annonce, null);
        ButterKnife.inject(this, root);
        mInflater = inflater;
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();

        // Now find the PullToRefreshLayout to setup
        mPullToRefreshLayout = (PullToRefreshLayout) root.findViewById(R.id.ptr_layout_list_appartements);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        //ActionBar
        actionBar.setDisplayShowTitleEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        actionBar.setTitle(mNavigationTitles[2]);

        //click Button
        mImageViewSearch.setOnClickListener(clickListener);
        mImageViewSearchMoreDetail.setOnClickListener(clickListener);


        mListViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent intentDetailAppartement = new Intent(getActivity(), DetailsAnnonceActivity.class);
                intentDetailAppartement.putExtra("annonce", mAnnonces.get(position));
                startActivity(intentDetailAppartement);
            }

        });

        listerAnnonces(AnnonceChargees, AnnonceChargees + 20);

        return root;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.imageViewSearchMoreDetail:
                    //Toast.makeText(getActivity(), "Plus de détails - Custom dialog...", Toast.LENGTH_SHORT).show();
                    custom_dialog_more_detail();
                    break;

                case R.id.imageViewSearch:
                    Toast.makeText(getActivity(), "Recherche lancée...", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    private void custom_dialog_more_detail() {
        View custom_view_change_password = mInflater.inflate(R.layout.custom_dialog_search_annonce_more_detail, null);

        AppartementListQuartier = getResources().getStringArray(R.array.appartements_quartier_array);
        AppartementListType = getResources().getStringArray(R.array.appartements_type_location_array);

        mSpinnerQuartier = (Spinner)custom_view_change_password.findViewById(R.id.spinnerCustomDialogSearchAppartDetailQuartier);
        mSpinnerType = (Spinner)custom_view_change_password.findViewById(R.id.spinnerCustomDialogSearchAppartDetailType);

        ArrayAdapter<String> spinnerQuartierArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, AppartementListQuartier);
        ArrayAdapter<String> spinnerTypeArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, AppartementListType);

        mSpinnerQuartier.setAdapter(spinnerQuartierArrayAdapter);
        mSpinnerType.setAdapter(spinnerTypeArrayAdapter);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(custom_view_change_password)
                .setCancelable(false)
                .setTitle(getString(R.string.search_details))
                .setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity(), "Annulation", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                })
                .create();

        dialog.show();
    }

    @Override
    public void onRefreshStarted(View view) {
        listerAnnonces(AnnonceChargees, AnnonceChargees + 20);
    }

    public void listerAnnonces(final int debut, final int fin)
    {
        new AsyncTask<Void, Void, Boolean>() {

            String AfficherJSON = null;
            String ErreurLoginTask = "Erreur ";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mPullToRefreshLayout.setRefreshing(true);
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                /*try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;*/

                HttpURLConnection urlConnection = null;
                StringBuilder sb = new StringBuilder();

                try {
                    URL url = new URL(getResources().getString(R.string.URL_SERVEUR) + getResources().getString(R.string.URL_SERVEUR_LIST_ANNONCES));
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
                    jsonParam.put("debut", debut);
                    jsonParam.put("fin", fin);
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

                        //AfficherJSON = sb.toString();

                        JSONArray jsonArray = new JSONArray(sb.toString());

                        AnnonceMax = jsonArray.getJSONArray(0).getJSONObject(0).getInt("Nb");

                        /*String annonces = jsonArray.getJSONArray(1).toString();
                        JsonElement json = new JsonParser().parse(annonces);
                        JsonArray varray = json.getAsJsonArray();
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-mm-dd").create();

                        for(JsonElement obj : varray )
                        {
                            Annonce a = gson.fromJson(obj.getAsJsonObject(), Annonce.class);
                            gson.fromJson(obj.getAsJsonObject(), Annonce.class);

                            mAnnonces.add(a);
                        }

                        return true;*/

                        JSONArray listAnnonces = jsonArray.getJSONArray(1); //contient des JSON objets

                        if(listAnnonces.length()>0){
                            for(int j = 0; j < listAnnonces.length(); j++){

                                AfficherJSON = listAnnonces.getJSONObject(j).toString();
                                JSONObject jsonObject = listAnnonces.getJSONObject(j);

                                int id_annonce = jsonObject.getInt("Id_Annonce");

                                /*SimpleDateFormat sdf = new SimpleDateFormat("aaaa/MM/dd HH:mm:ss");

                                try {
                                    Date date_create_annonce = sdf.parse(jsonObject.get("Date_Ajout_Annonce").toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }*/
                                Date date_create_annonce = new Date();

                                String titre_annonce = jsonObject.getString("Titre_Annonce");
                                double prix_annonce = jsonObject.getDouble("Prix_Annonce");
                                String description_annonce = jsonObject.getString("Description_Annonce");
                                String email_user_annonce = jsonObject.getString("E_mail_Personne_Annonce");

                                //int number_room_annonce = jsonObject.getInt("");
                                int number_room_annonce = 0;

                                int surface_annonce = jsonObject.getInt("Surface_Annonce");
                                String adresse_annonce = jsonObject.getString("Adresse_Annonce");
                                String categorie_annonce = jsonObject.getString("Libelle_Ctagorie");
                                String sous_categorie_annonce = jsonObject.getString("Libelle_Sous_Categorie");
                                String type_location_annonce = jsonObject.getString("Libelle_Type_Location");
                                String quartier_annonce = jsonObject.getString("Libelle_Quartier");
                                boolean est_meuble = jsonObject.getBoolean("Est_Meuble");

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
                    //todo : success
                    /*mAnnonces.add(new Annonce());
                    mAnnonces.add(new Annonce());
                    mAnnonces.add(new Annonce());
                    mAnnonces.add(new Annonce());
                    mAnnonces.add(new Annonce());
                    mAnnonces.add(new Annonce());
                    mAnnonces.add(new Annonce());
                    mAnnonces.add(new Annonce());*/

                    Toast.makeText(getActivity(), "Succes", Toast.LENGTH_SHORT).show();

                    Adapter = new ListViewAnnonceAdapter(getActivity().getBaseContext(), mAnnonces);
                    mListViewSearch.setAdapter(Adapter);
                }
                else {
                    //todo : error
                    Toast.makeText(getActivity(), ErreurLoginTask, Toast.LENGTH_SHORT).show();

                    /*AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setNegativeButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    adb.setMessage(AfficherJSON);
                    adb.show();*/
                }
                // Notify PullToRefreshLayout that the refresh has finished
                mPullToRefreshLayout.setRefreshComplete();
            }
        }.execute();
    }
}