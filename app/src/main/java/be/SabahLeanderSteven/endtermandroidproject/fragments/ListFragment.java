package be.SabahLeanderSteven.endtermandroidproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import be.SabahLeanderSteven.endtermandroidproject.R;
import be.SabahLeanderSteven.endtermandroidproject.model.Location;
import be.SabahLeanderSteven.endtermandroidproject.model.LocationViewModel;
import be.SabahLeanderSteven.endtermandroidproject.util.LocationAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private static final String SUBJECT = "argSubject";
    private String subject; // Use this as id for sidebar selection
    private AppCompatActivity mContext;

    private LocationAdapter adapter;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * FACTORY METHOD
     * @return new Instance of ListFragment
     */
    public static ListFragment newInstance(String subject) {
        ListFragment listFragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString(SUBJECT, subject);
        listFragment.setArguments(args);

        return listFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = (AppCompatActivity) context;
    }

    /**
     * ON CREATE VIEW METHOD
     * @param inflater:           LayoutInflater for fragment_list
     * @param container:          ViewGroup to inflate layout in
     * @param savedInstanceState: Bundle to pass saved instance state
     * @return Inflation of List Fragment
     */



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        if (getArguments() != null){
            subject = getArguments().getString(SUBJECT);
        }
        String sideBarItemID = subject;

        RecyclerView locationRV = rootView.findViewById(R.id.rv_locations);
        locationRV.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        adapter = new LocationAdapter(getActivity());
        locationRV.setAdapter(adapter);

        //quick fix by david
        LocationViewModel model = new ViewModelProvider(mContext).get(LocationViewModel.class);
        model.getAllLocationsOfType(sideBarItemID).observe(getViewLifecycleOwner(), new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                adapter.addLocations(locations);
                adapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

}
