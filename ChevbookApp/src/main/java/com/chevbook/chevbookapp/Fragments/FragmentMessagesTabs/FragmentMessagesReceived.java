package com.chevbook.chevbookapp.Fragments.FragmentMessagesTabs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.chevbook.chevbookapp.Adapter.ListViewMessageReceivedAdapter;
import com.chevbook.chevbookapp.Class.Message;
import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.CustomDialog.CustomDialogMessage;
import com.chevbook.chevbookapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
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
 * Created by Ugho on 24/02/14.
 */
public class FragmentMessagesReceived extends Fragment implements OnRefreshListener {

    @InjectView(R.id.listViewMessageReceived)
    ListView mListViewMessageReceived;
    @InjectView(R.id.linearLayoutMessagesReceivedLoading)
    LinearLayout mLinearLayoutMessagesReceivedLoading;
    @InjectView(R.id.buttonNoResultRafraichirMessagesReceived)
    Button mButtonNoResultRafraichirMessagesReceived;
    @InjectView(R.id.linearLayoutMessagesReceivedNoResult)
    LinearLayout mLinearLayoutMessagesReceivedNoResult;

    private CustomDialogMessage dialogMessage;
    private ListViewMessageReceivedAdapter Adapter;

    private ActionBarActivity actionBarActivity;
    private PullToRefreshLayout mPullToRefreshLayout;

    private ArrayList<Message> mMessages = new ArrayList<Message>();
    private User mUser;
    private Boolean onResume = false;

    public FragmentMessagesReceived() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_messages_received, container, false);
        ButterKnife.inject(this, rootView);
        actionBarActivity = (ActionBarActivity) getActivity();

        Adapter = new ListViewMessageReceivedAdapter(getActivity().getBaseContext(), mMessages);
        mListViewMessageReceived.setAdapter(Adapter);

        mUser = new User(getActivity().getApplicationContext());

        // Now find the PullToRefreshLayout to setup
        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout_messages_received);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        dialogMessage = new CustomDialogMessage(getActivity());
        dialogMessage.createDialog();

        mListViewMessageReceived.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                /*dialogMessage.instantiateDialogForLookMessage(mMessages.get(position),true);
                dialogMessage.showDialog();*/
            }

        });

        mButtonNoResultRafraichirMessagesReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMessages.clear();
                mMessages = new ArrayList<Message>();

                listerMessagesReceived();
            }
        });

        if(onResume == false) {
            listerMessagesReceived();
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
        mMessages.clear();
        mMessages = new ArrayList<Message>();

        listerMessagesReceived();
    }

    public void listerMessagesReceived()
    {
        new AsyncTask<Void, Void, Boolean>() {

            String AfficherJSON = null;
            String ErreurLoginTask = "Erreur ";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);
                mLinearLayoutMessagesReceivedNoResult.setVisibility(View.GONE);
                mListViewMessageReceived.setVisibility(View.GONE);
                mLinearLayoutMessagesReceivedLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                HttpURLConnection urlConnection = null;
                StringBuilder sb = new StringBuilder();

                try {
                    URL url = new URL(getResources().getString(R.string.URL_SERVEUR) + getResources().getString(R.string.URL_SERVEUR_MESSAGES_RECU));
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

                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                catch (MalformedURLException e){
                    ErreurLoginTask = ErreurLoginTask + "URL";
                    return false; //Erreur URL
                } catch (SocketTimeoutException e) {
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

                mListViewMessageReceived.setVisibility(View.VISIBLE);
                mLinearLayoutMessagesReceivedLoading.setVisibility(View.GONE);
                mLinearLayoutMessagesReceivedNoResult.setVisibility(View.GONE);

                if (result)
                {
                    Adapter.setList(mMessages);
                    Adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity(), ErreurLoginTask, Toast.LENGTH_SHORT).show();

                    mListViewMessageReceived.setVisibility(View.GONE);
                    mLinearLayoutMessagesReceivedNoResult.setVisibility(View.VISIBLE);
                }

                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setNegativeButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                adb.setMessage(AfficherJSON);
                adb.show();

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
