package com.example.chevbook.Fragments;

import android.app.AlertDialog;
import android.content.Context;
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

import com.example.chevbook.Activity.DetailsAppartementActivity;
import com.example.chevbook.Adapter.ListViewAnnonceAdapter;
import com.example.chevbook.Class.Appartement;
import com.example.chevbook.Class.Modele;
import com.example.chevbook.R;

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

    private PullToRefreshLayout mPullToRefreshLayout;
    private static LayoutInflater mInflater;

    public static Fragment newInstance(Context context) {
        FragmentAnnonces f = new FragmentAnnonces();

        return f;
    }

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

        Adapter = new ListViewAnnonceAdapter(getActivity().getBaseContext());
        mListViewSearch.setAdapter(Adapter);

        mListViewSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent intentDetailAppartement = new Intent(getActivity(), DetailsAppartementActivity.class);
                intentDetailAppartement.putExtra("position", position);
                intentDetailAppartement.putExtra("appartement", new Appartement());
                startActivity(intentDetailAppartement);
            }

        });

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
        AppartementListType = getResources().getStringArray(R.array.appartements_type_array);

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
        /**
         * Simulate Refresh with 4 seconds sleep
         */
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mPullToRefreshLayout.setRefreshing(true);
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(4000);
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
        }.execute();
    }
}