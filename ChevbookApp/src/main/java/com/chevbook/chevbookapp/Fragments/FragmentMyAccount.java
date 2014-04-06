package com.chevbook.chevbookapp.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chevbook.chevbookapp.Activity.DetailsAccountActivity;
import com.chevbook.chevbookapp.Activity.LoginActivity;
import com.chevbook.chevbookapp.Class.Modele;
import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.CustomsView.CircularImageView;
import com.chevbook.chevbookapp.R;
import com.nostra13.universalimageloader.core.ImageLoader;

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

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class FragmentMyAccount extends Fragment implements OnRefreshListener {

    @InjectView(R.id.profilePicture)CircularImageView mProfilePicture;
    @InjectView(R.id.profilePrenomNom)TextView mProfilePrenomNom;
    @InjectView(R.id.profileEmail)TextView mProfileEmail;
    @InjectView(R.id.profileMesAnnonces)TextView mProfileMesAnnonces;
    @InjectView(R.id.profileContact)TextView mProfileContact;
    @InjectView(R.id.profileDesactivateAccount)TextView mProfileDesactivateAccount;
    @InjectView(R.id.profileDeconnect)Button mButtonProfileDeconnect;
    @InjectView(R.id.profileAppVersion)TextView mProfileAppVersion;

    private Modele vmodele;
    private User vuser;
    private static boolean refreshing;

    private MenuItem menuRefresh;
    private MenuItem menuEdit;

    private ActionBarActivity actionBarActivity;
    private ActionBar actionBar;
    private static ImageLoader imageLoader;

    private PullToRefreshLayout mPullToRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        vmodele = new Modele();
        vuser = new User(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //init
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_my_account, null);
        ButterKnife.inject(this, root);
        actionBarActivity = (ActionBarActivity) getActivity();
        actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        imageLoader = ImageLoader.getInstance();

        // Now find the PullToRefreshLayout to setup
        mPullToRefreshLayout = (PullToRefreshLayout) root.findViewById(R.id.ptr_layout_account);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        //Load Data
        InitData();

        //ActionBar
        actionBar.setDisplayShowTitleEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        actionBar.setTitle(mNavigationTitles[0]);

        //click Button
        mButtonProfileDeconnect.setOnClickListener(clickListener);

        try {
            Context c = getActivity().getApplicationContext();
            String versionName = c.getPackageManager()
                    .getPackageInfo(c.getPackageName(), 0).versionName;

            mProfileAppVersion.setText("V " + versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return root;
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {

            switch(v.getId())
            {
                case R.id.profileDeconnect:
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setPositiveButton(getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //Toast.makeText(getActivity(), getString(R.string.success_logout), Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(myIntent);
                            //vmodele.UserLogOut(getApplicationContext());
                            vuser.logoutUser();
                            getActivity().finish();
                        }
                    });
                    adb.setNegativeButton(getString(R.string.btn_cancel),new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.cancel();
                        }
                    });
                    adb.setMessage(getString(R.string.message_logout));
                    adb.show();

                    break;
            }
        }
    };

    @Override
    public void onRefreshStarted(View view) {
        LoadUserTask mLoadUserTask = new LoadUserTask();
        mLoadUserTask.execute((Void) null);
    }

    public void InitData() {
        mProfilePrenomNom.setText(vuser.getFirstName() + " " + vuser.getLastName().toUpperCase());
        mProfileEmail.setText(vuser.getEmail());
        mProfileMesAnnonces.setText("0");

        if(vuser.getUrlProfilPicture().length() > 0)
        {
            imageLoader.displayImage(vuser.getUrlProfilPicture(), mProfilePicture);
        }
        else
        {
            mProfilePicture.setImageResource(R.drawable.ic_profile);
        }

        LoadUserTask mLoadUserTask = new LoadUserTask();
        mLoadUserTask.execute((Void) null);
    }

    public void EditProfil() {
        Intent intentDetailAccount = new Intent(getActivity(), DetailsAccountActivity.class);
        startActivity(intentDetailAccount);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.account, menu);

        menuRefresh = menu.findItem(R.id.account_menuRefresh);
        menuEdit = menu.findItem(R.id.account_menuEdit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account_menuRefresh:
                if (refreshing) {
                    Toast.makeText(getActivity(), getString(R.string.action_refresh_in_process), Toast.LENGTH_SHORT).show();
                } else {
                    LoadUserTask mLoadUserTask = new LoadUserTask();
                    mLoadUserTask.execute((Void) null);
                }

                return true;

            case R.id.account_menuEdit:
                EditProfil();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void ShowProgressBar(boolean b) {
        if (b) {
            actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);
            refreshing = true;
        } else {
            actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);
            refreshing = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ShowProgressBar(false);
        menuRefresh.setVisible(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);
    }

    public class LoadUserTask extends AsyncTask<Void, Void, Boolean> {

        private String ErreurLoginTask = "Erreur ";
        private Boolean userExist = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ShowProgressBar(true);

        }

        @Override
        protected Boolean doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            StringBuilder sb = new StringBuilder();

            try {
                URL url = new URL(getResources().getString(R.string.URL_SERVEUR) + getResources().getString(R.string.URL_SERVEUR_IDENTIFICATION));
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
                jsonParam.put("email", vuser.getEmail());
                jsonParam.put("password", vuser.getPasswordSha1());
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

                    JSONArray jsonArray = new JSONArray(sb.toString());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    boolean user_exist = jsonObject.getBoolean("connectSuccess");

                    if(user_exist) {

                        String mFirstname = jsonObject.getString("Prenom_Personne"); //prenom
                        String mLastname = jsonObject.getString("Nom_Personne"); //nom
                        String mUrl_image = jsonObject.getString("Avatar_Personne");
                        String mPassword = jsonObject.getString("Mdp_Personne");
                        String mEmail = jsonObject.getString("Email");
                        //int mNbAnnonces = jsonObject.getInt("Nb_annonces_Personne");;

                        vuser.setFirstname(mFirstname);
                        vuser.setLastname(mLastname);
                        vuser.setUrlProfilPicture(mUrl_image);
                        vuser.setEmail(mEmail);

                        return true;
                    }
                    else {
                        //Utilisateur existe pas
                        ErreurLoginTask = ErreurLoginTask + "de mot de passe ou d'email";
                        userExist = false;
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
        protected void onPostExecute(final Boolean success) {

            ShowProgressBar(false);
            mPullToRefreshLayout.setRefreshComplete();

            if (success) {
                mProfilePrenomNom.setText(vuser.getFirstName() + " " + vuser.getLastName().toUpperCase());
                mProfileEmail.setText(vuser.getEmail());
                mProfileMesAnnonces.setText("0");

                if(vuser.getUrlProfilPicture().length() > 0)
                {
                    imageLoader.displayImage(vuser.getUrlProfilPicture(), mProfilePicture);
                }
                else
                {
                    mProfilePicture.setImageResource(R.drawable.ic_profile);
                }

            } else {
                if(!userExist)
                {
                    AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                    adb.setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent myIntent = new Intent(getActivity(), LoginActivity.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(myIntent);
                            vuser.logoutUser();
                            getActivity().finish();
                        }
                    });
                    adb.setMessage("Votre compte à changer, veuillez-vous re-connecter !");
                    adb.setCancelable(false);
                    adb.show();
                }
                else {
                    Toast.makeText(getActivity(), ErreurLoginTask, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}