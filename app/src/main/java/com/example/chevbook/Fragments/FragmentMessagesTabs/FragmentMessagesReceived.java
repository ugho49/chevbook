package com.example.chevbook.Fragments.FragmentMessagesTabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chevbook.Adapter.ListViewMessageReceivedAdapter;
import com.example.chevbook.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Ugho on 24/02/14.
 */
public class FragmentMessagesReceived extends Fragment {

    @InjectView(R.id.listViewMessageReceived)
    ListView mListViewMessageReceived;

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

        mListViewMessageReceived.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //todo
            }

        });

        return rootView;
    }
}
