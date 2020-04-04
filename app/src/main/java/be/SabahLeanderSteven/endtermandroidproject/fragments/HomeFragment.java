package be.SabahLeanderSteven.endtermandroidproject.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Objects;

import be.SabahLeanderSteven.endtermandroidproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Dialog aboutPopup;
    ImageButton wtfButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * FACTORY METHOD
     * @return new Instance of HomeFragment
     */
    public static HomeFragment newInstance(){return new HomeFragment();}

    /**
     * ON CREATE VIEW METHOD
     * @param inflater: LayoutInflater for fragment_home
     * @param container: ViewGroup to inflate layout in
     * @param savedInstanceState: Bundle to pass saved instance state
     * @return Inflation of Home Fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        wtfButton = rootView.findViewById(R.id.about_ib);
        aboutPopup = new Dialog(Objects.requireNonNull(getContext()));

        wtfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

        return rootView;
    }

    public void showPopup(){
        aboutPopup.setContentView(R.layout.about_popup);
        aboutPopup.show();
    }

}
