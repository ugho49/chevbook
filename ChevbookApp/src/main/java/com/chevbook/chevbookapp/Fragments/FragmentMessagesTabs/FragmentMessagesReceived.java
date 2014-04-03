package com.chevbook.chevbookapp.Fragments.FragmentMessagesTabs;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chevbook.chevbookapp.Adapter.ListViewMessageReceivedAdapter;
import com.chevbook.chevbookapp.CustomDialog.CustomDialogMessage;
import com.chevbook.chevbookapp.R;

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

    private CustomDialogMessage dialogMessage;
    private PullToRefreshLayout mPullToRefreshLayout;
    private ListViewMessageReceivedAdapter Adapter;

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

        Adapter = new ListViewMessageReceivedAdapter(getActivity().getBaseContext());
        mListViewMessageReceived.setAdapter(Adapter);

        // Now find the PullToRefreshLayout to setup
        mPullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.ptr_layout_messages_received);
        ActionBarPullToRefresh.from(getActivity())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        dialogMessage = new CustomDialogMessage(getActivity());

        mListViewMessageReceived.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                dialogMessage.createDialog();
                dialogMessage.lookMessage();
                dialogMessage.showButtonReply(true);
                dialogMessage.resetDialog();
                dialogMessage.instantiateDialog("https://fbcdn-sphotos-e-a.akamaihd.net/hphotos-ak-prn2/t1/1546110_10202187125312508_9323923_n.jpg", "Ugho49", "12/02/2014", "Renseignement pour votre appartement", "Bonjour, je suis très intéressé par votre annonce, pouvez-vous me recontacter au plus vite pour en connaître d'avantage ? Mon numéro : 06.88.10.65.38. Coordialement, Ugho STEPHAN");
                dialogMessage.showDialog();
            }

        });

        return rootView;
    }

    @Override
    public void onRefreshStarted(View view) {
        new AsyncTask<Void, Void, Void>() {

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
                Toast.makeText(getActivity(), "Refresh Messages Received", Toast.LENGTH_SHORT).show();
                mPullToRefreshLayout.setRefreshComplete();
            }
        }.execute();
    }
}
