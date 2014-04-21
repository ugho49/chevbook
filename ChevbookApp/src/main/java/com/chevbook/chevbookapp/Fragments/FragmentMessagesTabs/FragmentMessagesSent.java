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
import com.chevbook.chevbookapp.Adapter.ListViewMessageSentAdapter;
import com.chevbook.chevbookapp.Class.Message;
import com.chevbook.chevbookapp.Class.User;
import com.chevbook.chevbookapp.CustomDialog.CustomDialogMessage;
import com.chevbook.chevbookapp.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by Ugho on 24/02/14.
 */
public class FragmentMessagesSent extends Fragment implements OnRefreshListener {

    @InjectView(R.id.listViewMessageSent)
    ListView mListViewMessageSent;
    @InjectView(R.id.linearLayoutMessagesSentLoading)
    LinearLayout mLinearLayoutMessagesSentLoading;
    @InjectView(R.id.buttonNoResultRafraichirMessagesSent)
    Button mButtonNoResultRafraichirMessagesSent;
    @InjectView(R.id.linearLayoutMessagesSentNoResult)
    LinearLayout mLinearLayoutMessagesSentNoResult;

    private ListViewMessageSentAdapter Adapter;
    private CustomDialogMessage dialogMessage;

    private ActionBarActivity actionBarActivity;
    private PullToRefreshLayout mPullToRefreshLayout;

    private ArrayList<Message> mMessages = new ArrayList<Message>();
    private User mUser;
    private Boolean onResume = false;

    public FragmentMessagesSent() {
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
        View rootView = inflater.inflate(R.layout.fragment_messages_sent, container, false);
        ButterKnife.inject(this, rootView);
        actionBarActivity = (ActionBarActivity) getActivity();

        Adapter = new ListViewMessageSentAdapter(getActivity().getBaseContext(), mMessages);
        mListViewMessageSent.setAdapter(Adapter);

        mUser = new User(getActivity().getApplicationContext());

        // Now find the PullToRefreshLayout to setup
        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout_messages_sent);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        mListViewMessageSent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                dialogMessage = new CustomDialogMessage(getActivity());
                dialogMessage.createDialog();
                dialogMessage.instantiateDialogForLookMessage(mMessages.get(position),false);
                dialogMessage.showDialog();
            }

        });

        mButtonNoResultRafraichirMessagesSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMessages.clear();
                mMessages = new ArrayList<Message>();

                listerMessagesSent();
            }
        });

        if(onResume == false) {
            listerMessagesSent();
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

        listerMessagesSent();
    }

    public void listerMessagesSent()
    {
        actionBarActivity.setSupportProgressBarIndeterminateVisibility(true);
        mLinearLayoutMessagesSentNoResult.setVisibility(View.GONE);
        mListViewMessageSent.setVisibility(View.GONE);
        mLinearLayoutMessagesSentLoading.setVisibility(View.VISIBLE);

        String[] mesparams = {"messages_envoyees"};
        new API_message(FragmentMessagesSent.this).execute(mesparams);
    }

    public void resultListerMessagesSent(boolean success, ArrayList<Message> messages)
    {
        mListViewMessageSent.setVisibility(View.VISIBLE);
        mLinearLayoutMessagesSentLoading.setVisibility(View.GONE);
        mLinearLayoutMessagesSentNoResult.setVisibility(View.GONE);

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

            mListViewMessageSent.setVisibility(View.GONE);
            mLinearLayoutMessagesSentNoResult.setVisibility(View.VISIBLE);
        }

        actionBarActivity.setSupportProgressBarIndeterminateVisibility(false);
        mPullToRefreshLayout.setRefreshComplete();
    }
}
