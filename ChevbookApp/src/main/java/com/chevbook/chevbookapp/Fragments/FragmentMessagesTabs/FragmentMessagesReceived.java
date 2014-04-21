package com.chevbook.chevbookapp.Fragments.FragmentMessagesTabs;

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

import com.chevbook.chevbookapp.API.API_message;
import com.chevbook.chevbookapp.Adapter.ListViewMessageReceivedAdapter;
import com.chevbook.chevbookapp.Class.Message;
import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.CustomDialog.CustomDialogMessage;
import com.chevbook.chevbookapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

        mListViewMessageReceived.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if(!mMessages.get(position).getEst_lu()){
                    lireMessage(mMessages.get(position), position);
                }
                dialogMessage = new CustomDialogMessage(getActivity());
                dialogMessage.createDialog();
                dialogMessage.instantiateDialogForLookMessage(mMessages.get(position),true);
                dialogMessage.showDialog();
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
        actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);
        mLinearLayoutMessagesReceivedNoResult.setVisibility(View.GONE);
        mListViewMessageReceived.setVisibility(View.GONE);
        mLinearLayoutMessagesReceivedLoading.setVisibility(View.VISIBLE);

        String[] mesparams = {"messages_recu"};
        new API_message(FragmentMessagesReceived.this).execute(mesparams);
    }

    public void resultListerMessagesReceived(boolean success, ArrayList<Message> messages)
    {
        mListViewMessageReceived.setVisibility(View.VISIBLE);
        mLinearLayoutMessagesReceivedLoading.setVisibility(View.GONE);
        mLinearLayoutMessagesReceivedNoResult.setVisibility(View.GONE);

        if (success)
        {
            for (Message m : messages){
                mMessages.add(m);
            }

            Adapter.setList(mMessages);
            Adapter.notifyDataSetChanged();
        }
        else {
            //Toast.makeText(getActivity(), ErreurLoginTask, Toast.LENGTH_SHORT).show();

            mListViewMessageReceived.setVisibility(View.GONE);
            mLinearLayoutMessagesReceivedNoResult.setVisibility(View.VISIBLE);
        }

        actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);
        mPullToRefreshLayout.setRefreshComplete();
    }

    public void lireMessage(final Message m, final int pos)
    {
        SimpleDateFormat simpleDate =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDate.format(m.getDate_create_message());

        String[] mesparams = {
                "lire_messages_recu",
                m.getEmail_emetteur(),
                Integer.toString(m.getId_annonce_destinataire()),
                date,
                Integer.toString(pos)};

        new API_message(FragmentMessagesReceived.this).execute(mesparams);
    }

    public void resultLireMessage(boolean success, int pos)
    {
        if (success)
        {
            Adapter.getList().get(pos).setEst_lu(true);
            Adapter.notifyDataSetChanged();
        }
        else {
            //Toast.makeText(getActivity(), ErreurLoginTask, Toast.LENGTH_SHORT).show();
        }
    }
}
