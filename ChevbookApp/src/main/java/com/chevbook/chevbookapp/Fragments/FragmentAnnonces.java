package com.chevbook.chevbookapp.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

import com.chevbook.chevbookapp.Activity.DeposerModifierAnnonceActivity;
import com.chevbook.chevbookapp.Activity.DetailsAnnonceActivity;
import com.chevbook.chevbookapp.Adapter.ListViewAnnonceAdapter;
import com.chevbook.chevbookapp.Class.Annonce;
import com.chevbook.chevbookapp.Class.Modele;
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

public class FragmentAnnonces extends Fragment implements OnRefreshListener, AbsListView.OnScrollListener{

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
    @InjectView(R.id.buttonNoResultRafraichirAnnonce)
    Button mButtonNoResultRafraichirAnnonce;


    private int AnnonceMax = 0;
    private int AnnonceChargees = 0;

    private int AnnonceDebut = 0;
    private int AnnonceFin = 10;

    private Boolean onResume = false;

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
    private ActionBarActivity actionBarActivity;
    private boolean flag_loading = false;

    private static final int CONST_CREATE = 0;
    private static final int CONST_MODIFIER = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        vmodele = new Modele();
    }

    @Override
    public void onPause() {
        super.onPause();

        onResume = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //init
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_annonce, null);
        ButterKnife.inject(this, root);
        mInflater = inflater;
        actionBarActivity = (ActionBarActivity) getActivity();
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
        actionBar.setTitle(mNavigationTitles[1]);

        //click Button
        mImageViewSearch.setOnClickListener(clickListener);
        mImageViewSearchMoreDetail.setOnClickListener(clickListener);
        mButtonNoResultRafraichirAnnonce.setOnClickListener(clickListener);


        mListViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent intentDetailAppartement = new Intent(getActivity(), DetailsAnnonceActivity.class);
                intentDetailAppartement.putExtra("annonce", mAnnonces.get(position));
                startActivity(intentDetailAppartement);
            }

        });

        mListViewSearch.setOnScrollListener(FragmentAnnonces.this);

        Adapter = new ListViewAnnonceAdapter(getActivity().getBaseContext(), mAnnonces);
        mListViewSearch.setAdapter(Adapter);

        if(onResume == false) {
            listerAnnonces(AnnonceDebut, AnnonceFin);
        }
        else {
            onResume = false;
        }

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

                case R.id.buttonNoResultRafraichirAnnonce:
                    listerAnnonces(AnnonceDebut, AnnonceFin);
                    break;

            }
        }
    };

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.list_appartements, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_appartements_menu_new_announce:
                //Toast.makeText(getActivity(), getString(R.string.action_new_announce), Toast.LENGTH_SHORT).show();
                Intent intentDetailAppartement = new Intent(getActivity(), DeposerModifierAnnonceActivity.class);
                intentDetailAppartement.putExtra("CONST", CONST_CREATE);
                startActivity(intentDetailAppartement);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        int currentPage = (int) Math.floor(AnnonceChargees / 10) + 1;
        int maxPage = (int) Math.floor(AnnonceMax / 10) + 1;

        if(currentPage != maxPage){
            if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0) {
                if (flag_loading == false){
                    flag_loading = true;

                    //Toast.makeText(getActivity(), "Bas de la listView", Toast.LENGTH_SHORT).show();
                    if(AnnonceMax > AnnonceChargees)
                    {
                        listerAnnonces(AnnonceDebut, AnnonceFin);
                    }
                }
            }
        }
    }

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

        mAnnonces.clear();
        mAnnonces = new ArrayList<Annonce>();
        flag_loading = false;

        if(AnnonceChargees == 0)
        {
            listerAnnonces(AnnonceDebut, AnnonceFin);
        }
        else {

            int a = AnnonceChargees;
            AnnonceChargees = 0;
            listerAnnonces(0, a);
        }
    }

    public void listerAnnonces(final int debut, final int fin)
    {
        new AsyncTask<Void, Void, Boolean>() {

            String AfficherJSON = null;
            String ErreurLoginTask = "Erreur ";
            int annonceChargeesInThisTask;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);

                mLinearLayoutSearchAppartementNoResult.setVisibility(View.GONE);

                if(AnnonceChargees == 0)
                {
                    mListViewSearch.setVisibility(View.GONE);
                    mLinearLayoutSearchAppartementLoading.setVisibility(View.VISIBLE);
                }
                //mPullToRefreshLayout.setRefreshing(true);
            }

            @Override
            protected Boolean doInBackground(Void... params) {

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

                        JSONArray listAnnonces = jsonArray.getJSONArray(1); //contient des JSON objets

                        annonceChargeesInThisTask = listAnnonces.length();

                        if(annonceChargeesInThisTask>0){
                            for(int j = 0; j < annonceChargeesInThisTask; j++){

                                AfficherJSON = listAnnonces.getJSONObject(j).toString();
                                JSONObject jsonObject = listAnnonces.getJSONObject(j);

                                Date date_create_annonce = null;

                                date_create_annonce = ConvertToDate(jsonObject.get("Date_Ajout_Annonce").toString());

                                //Date date_create_annonce = new Date();

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


                mListViewSearch.setVisibility(View.VISIBLE);
                mLinearLayoutSearchAppartementLoading.setVisibility(View.GONE);
                mLinearLayoutSearchAppartementNoResult.setVisibility(View.GONE);


                if (result)
                {
                    //Toast.makeText(getActivity(), "Succes", Toast.LENGTH_SHORT).show();

                    Adapter.setList(mAnnonces);
                    Adapter.notifyDataSetChanged();

                    if(debut != 0) {
                        mListViewSearch.smoothScrollToPosition(AnnonceChargees);
                    }


                    AnnonceChargees = AnnonceChargees + annonceChargeesInThisTask;
                    int nbAnnoncesNonChargeesRestantes = AnnonceMax - AnnonceChargees;

                    if(nbAnnoncesNonChargeesRestantes >= 10)
                    {
                        /*AnnonceDebut = AnnonceDebut + 10;
                        AnnonceFin = AnnonceFin + 10;*/
                        AnnonceDebut = AnnonceFin + 1;
                        AnnonceFin = 10;
                    }
                    else {
                        /*AnnonceDebut = AnnonceDebut + nbAnnoncesNonChargeesRestantes;
                        AnnonceFin = AnnonceFin + nbAnnoncesNonChargeesRestantes;*/
                        AnnonceDebut = AnnonceFin + 1;
                        AnnonceFin = nbAnnoncesNonChargeesRestantes;
                    }

                }
                else {
                    //Toast.makeText(getActivity(), ErreurLoginTask, Toast.LENGTH_SHORT).show();

                    /*AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setNegativeButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    adb.setMessage(AfficherJSON);
                    adb.show();*/

                    mListViewSearch.setVisibility(View.GONE);
                    mLinearLayoutSearchAppartementNoResult.setVisibility(View.VISIBLE);
                }

                // Notify PullToRefreshLayout that the refresh has finished
                actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);
                mPullToRefreshLayout.setRefreshComplete();
                flag_loading = false;
            }
        }.execute();
    }

    private Date ConvertToDate(String dateString){

        SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new Date();
        }
    }
}