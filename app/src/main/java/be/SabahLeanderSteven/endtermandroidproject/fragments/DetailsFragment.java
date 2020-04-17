package be.SabahLeanderSteven.endtermandroidproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import be.SabahLeanderSteven.endtermandroidproject.R;
import be.SabahLeanderSteven.endtermandroidproject.model.Location;

public class DetailsFragment extends Fragment {

    private TextView characters_tv;
    private TextView year_tv;
    private TextView authors_tv;
    private ImageView imageView;
    private Location currentLocation;

    public DetailsFragment() {
    }

    public static DetailsFragment newInstance(Location detailLocation) {
        DetailsFragment df = new DetailsFragment();
        df.currentLocation = detailLocation;
        return df;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        characters_tv = rootView.findViewById(R.id.characters_tv);
        year_tv = rootView.findViewById(R.id.year_tv);
        authors_tv = rootView.findViewById(R.id.authors_tv);
        imageView = rootView.findViewById(R.id.imageView2);

        authors_tv.setText(currentLocation.getAuthors());
        characters_tv.setText(currentLocation.getCharacters());
        year_tv.setText("" + currentLocation.getYear());


        return rootView;


    }
}
