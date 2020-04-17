package be.SabahLeanderSteven.endtermandroidproject.fragments;

import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.zip.Inflater;

import be.SabahLeanderSteven.endtermandroidproject.R;
import be.SabahLeanderSteven.endtermandroidproject.model.Location;
import be.SabahLeanderSteven.endtermandroidproject.model.LocationViewModel;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final String SUBJECT = "argSubject";
    private String subject; // Use this as id for sidebar selection

    private MapView mapView;
    private GoogleMap myMap;
    private AppCompatActivity mContext;
    private final LatLng Brussel = new LatLng(50.858712, 4.347446);

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = (AppCompatActivity) context;

    }

    public static MapFragment newInstance(String subject) {
        MapFragment mapFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(SUBJECT, subject);
        mapFragment.setArguments(args);

        return mapFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);

        return rootView;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        CameraUpdate locationBrussel = CameraUpdateFactory.newLatLngZoom(Brussel, 12);
        myMap.animateCamera(locationBrussel);
        myMap.setOnInfoWindowClickListener(this);
        drawMarkers();


    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void drawMarkers() {
        // get argument from instance created to pass to getAllLocationsOfType
        if (getArguments() != null) {
            subject = getArguments().getString(SUBJECT);
        }
        //methode om pinnekes op u kaart te krijgen.
        //for loop over database aanmaken.
//        Marker m = myMap.addMarker(new MarkerOptions()....).position(new LatLng marker.getgeolat, marker.getgeoLong)
        LocationViewModel locationViewModel = new ViewModelProvider(mContext).get(LocationViewModel.class);
        locationViewModel.getAllLocationsOfType(subject).observe(mContext, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                //data is binnengehaald
                //data omzetten naar pinnetjes

                //step1 = alle data overlopen via loop
                for (Location currentLocation : locations) {
                    //step2 marker aanmaken
                    Marker m = myMap.addMarker(new MarkerOptions()
                            .position(new LatLng(currentLocation.getGeoLat(), currentLocation.getGeolong()))
                            .title(currentLocation.getCharacters())
                            .snippet(currentLocation.getAuthors())
                    );
                    m.setTag(currentLocation);
                }
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //welke pin was het
        Location detailLocation = (Location) marker.getTag();
        //naar waar navigeren
        DetailsFragment df = DetailsFragment.newInstance(detailLocation);
        //navigatie
        mContext.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, df)
                .addToBackStack("BACK")
                .commit();
    }
}