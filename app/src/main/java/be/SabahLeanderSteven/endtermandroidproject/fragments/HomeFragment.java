package be.SabahLeanderSteven.endtermandroidproject.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.Objects;

import be.SabahLeanderSteven.endtermandroidproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Dialog aboutPopup;

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

        ImageButton wtfButton = rootView.findViewById(R.id.about_ib);
        aboutPopup = new Dialog(Objects.requireNonNull(getContext()));

        wtfButton.setOnClickListener(v -> showPopup());

        return rootView;
    }

    /**
     * SHOW POPUP
     */
    private void showPopup(){
        aboutPopup.setContentView(R.layout.about_popup);
        // TODO : Setup popup content here ...

        aboutPopup.show();
    }

}
