package com.example.chevbook.Fragments;

import android.content.Context;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chevbook.Activity.DetailsAccountActivity;
import com.example.chevbook.Class.Modele;
import com.example.chevbook.Class.User;
import com.example.chevbook.CustomsView.CircularImageView;
import com.example.chevbook.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class FragmentMyAccount extends Fragment implements OnRefreshListener {

    @InjectView(R.id.profilePicture)CircularImageView mProfilePicture;
    @InjectView(R.id.profilePrenom)TextView mProfilePrenom;
    @InjectView(R.id.profileNom)TextView mProfileNom;
    @InjectView(R.id.profileEmail)TextView mProfileEmail;
    @InjectView(R.id.profileAppartement)Button mProfileAppartement;
    @InjectView(R.id.profileMessages)Button mProfileMessages;
    @InjectView(R.id.profileFavoris)Button mProfileFavoris;

    private Modele vmodele;
    private User vuser;
    private static boolean refreshing;

    private MenuItem menuRefresh;
    private MenuItem menuEdit;

    private ActionBarActivity actionBarActivity;
    private ActionBar actionBar;
    private static ImageLoader imageLoader;

    private PullToRefreshLayout mPullToRefreshLayout;

    public static Fragment newInstance(Context context) {
        FragmentMyAccount f = new FragmentMyAccount();

        return f;
    }

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
        LoadData();

        //ActionBar
        actionBar.setDisplayShowTitleEnabled(true);
        String[] mNavigationTitles = getResources().getStringArray(R.array.navigation_array);
        actionBar.setTitle(mNavigationTitles[0]);

        //click Button
        mProfileAppartement.setOnClickListener(clickListener);
        mProfileMessages.setOnClickListener(clickListener);
        mProfileFavoris.setOnClickListener(clickListener);

        return root;
    }

    @Override
    public void onRefreshStarted(View view) {
        /**
         * Simulate Refresh with 4 seconds sleep
         */
        LoadData();
        /*new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);

                // Notify PullToRefreshLayout that the refresh has finished
                mPullToRefreshLayout.setRefreshComplete();
            }
        }.execute();*/
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v)
        {

            switch(v.getId())
            {
                case R.id.profileAppartement:
                    Toast.makeText(getActivity(), "Mes Appartements", Toast.LENGTH_SHORT).show();

                    /*getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new FragmentMyAnnonces())
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit();*/

                    break;

                case R.id.profileMessages:
                    Toast.makeText(getActivity(), "Mes Messages", Toast.LENGTH_SHORT).show();

                    /*getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new FragmentMessages())
                            .setTransition(FragmentTransaction.TRANSIT_ENTER_MASK)
                            .addToBackStack(null)
                            .commit();*/
                    break;

                case R.id.profileFavoris:
                    Toast.makeText(getActivity(), "Mes Favoris", Toast.LENGTH_SHORT).show();

                    /*getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new FragmentFavoris())
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                            .addToBackStack(null)
                            .commit();*/
                    break;
            }
        }
    };

    public void LoadData() {
        mProfileNom.setText(vuser.getLastName());
        mProfilePrenom.setText(vuser.getFirstName());
        mProfileEmail.setText(vuser.getEmail());
        imageLoader.displayImage(vuser.getUrlProfilPicture(), mProfilePicture);

        LoadUserTask mLoadUserTask = new LoadUserTask();
        mLoadUserTask.execute((Void) null);
    }

    public void EditProfil() {
        //Toast.makeText(getActivity(), getString(R.string.edit_profil), Toast.LENGTH_SHORT).show();

        Intent intentDetailAccount = new Intent(getActivity(), DetailsAccountActivity.class);
        //intentDetailAccount.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //intentDetailAccount.putExtra("url_image","bsjeibjsprjbopsrj");
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
                    LoadData();
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ShowProgressBar(true);

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(1000);

            } catch (InterruptedException e) {

                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            ShowProgressBar(false);
            mPullToRefreshLayout.setRefreshComplete();

            if (success) {
                //todo

            }
        }
    }
}