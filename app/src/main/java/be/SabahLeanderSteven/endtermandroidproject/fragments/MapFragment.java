package be.SabahLeanderSteven.endtermandroidproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import be.SabahLeanderSteven.endtermandroidproject.R;

//A simple {@link Fragment} subclass.

public class MapFragment extends Fragment {

 private static final String SUBJECT = "argSubject";
private String subject; // Use this as id for sidebar selection

private MapView mapView;
private GoogleMap myMap;
 public MapFragment() {
        //Required empty public constructor}

///**
// * FACTORY METHOD
// * @return new Instance of MapFragment
// */
// public static MapFragment newInstance(String subject){
//  MapFragment mapFragment = new MapFragment();
// Bundle args = new Bundle();
//args.putString(SUBJECT, subject);
//mapFragment.setArguments(args);

//   return mapFragment;
    }

            ///**
            //* ON CREATE VIEW METHOD
            //* @param inflater: LayoutInflater for fragment_map
            //* @param container: ViewGroup to inflate layout in
            //* @param savedInstanceState: Bundle to pass saved instance state
            //* @return Inflation of Map Fragment
//*/
//@Override
            //public View onCreateView(LayoutInflater inflater, ViewGroup container,
            //  Bundle savedInstanceState) {
            // View rootView = inflater.inflate(R.layout.fragment_map, container, false);

            // if (getArguments() != null){
            // subject = getArguments().getString(SUBJECT);
            //}
            // String sideBarItemID = subject;

            //mapView = rootView.findViewById(R.id.mapView);
            //mapView.onCreate(savedInstanceState);
            //mapView.getMapAsync(onMapReady);

            // return rootView;
//}

            // /**
            // * ON MAP READY
            //* Callback on completion of async task
// */
// private OnMapReadyCallback onMapReady = new OnMapReadyCallback() {
//  @Override
            // public void onMapReady(GoogleMap googleMap) {
            // myMap = googleMap;
            // }
//};

            ///**
            //* GOOGLE MAP LIFECYCLE
//*/
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
            public void onDestroyView() {
            super.onDestroyView();
            mapView.onDestroy();
    }

@Override
public void onLowMemory() {
    super.onLowMemory();
    mapView.onLowMemory();
}


}
