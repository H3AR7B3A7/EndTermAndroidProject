package be.SabahLeanderSteven.endtermandroidproject.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import be.SabahLeanderSteven.endtermandroidproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String SUBJECT = "argSubject";
    private String subject;

    private Dialog aboutPopup;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * FACTORY METHOD
     * @return new Instance of HomeFragment
     */
    public static HomeFragment newInstance(String subject){
        HomeFragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(SUBJECT, subject);
        homeFragment.setArguments(args);

        return homeFragment;
    }

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
        TextView tv = rootView.findViewById(R.id.subject_tv);

        if (getArguments() != null){
            subject = getArguments().getString(SUBJECT);
        }
        tv.setText(subject);

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
        // TODO : Setup popup content, like buttons, here ...

        aboutPopup.show();
    }

}
