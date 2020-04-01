package be.SabahLeanderSteven.endtermandroidproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.SabahLeanderSteven.endtermandroidproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * FACTORY METHOD
     * @return new Instance of ListFragment
     */
    public static ListFragment newInstance(){ return new ListFragment();}

    /**
     * ON CREATE VIEW METHOD
     * @param inflater: LayoutInflater for fragment_list
     * @param container: ViewGroup to inflate layout in
     * @param savedInstanceState: Bundle to pass saved instance state
     * @return Inflation of List Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

}
