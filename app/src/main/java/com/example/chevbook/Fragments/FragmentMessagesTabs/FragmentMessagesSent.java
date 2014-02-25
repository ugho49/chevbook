package com.example.chevbook.Fragments.FragmentMessagesTabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chevbook.Adapter.ListViewMessageSentAdapter;
import com.example.chevbook.CustomDialog.CustomDialogMessage;
import com.example.chevbook.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Ugho on 24/02/14.
 */
public class FragmentMessagesSent extends Fragment {

    @InjectView(R.id.listViewMessageSent)
    ListView mListViewMessageSent;

    private ListViewMessageSentAdapter Adapter;
    private CustomDialogMessage dialogMessage;

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

        Adapter = new ListViewMessageSentAdapter(getActivity().getBaseContext());
        mListViewMessageSent.setAdapter(Adapter);

        dialogMessage = new CustomDialogMessage(getActivity());

        mListViewMessageSent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                dialogMessage.createDialog();
                dialogMessage.lookMessage(true);
                dialogMessage.resetDialog();
                dialogMessage.instantiateDialog("https://fbcdn-sphotos-e-a.akamaihd.net/hphotos-ak-prn2/t1/1546110_10202187125312508_9323923_n.jpg", "Ugho49", "12/02/2014", "Renseignement pour votre appartement", "Bonjour, je suis très intéressé par votre annonce, pouvez-vous me recontacter au plus vite pour en connaître d'avantage ? Mon numéro : 06.88.10.65.38. Coordialement, Ugho STEPHAN");
                dialogMessage.showDialog();
            }

        });


        return rootView;
    }
}
