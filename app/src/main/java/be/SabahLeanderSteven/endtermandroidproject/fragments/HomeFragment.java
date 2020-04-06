package be.SabahLeanderSteven.endtermandroidproject.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import be.SabahLeanderSteven.endtermandroidproject.R;
import be.SabahLeanderSteven.endtermandroidproject.model.Location;
import be.SabahLeanderSteven.endtermandroidproject.model.LocationRoomDB;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String SUBJECT = "argSubject";
    private String subject;
    private ExecutorService threadExecutor = Executors.newFixedThreadPool(4);

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

        fetchComicBookLocations();

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

    // TODO : Add db Async task here somewhere to start DL in background
    private void fetchComicBookLocations() {
        threadExecutor.execute(() -> {
            Log.e("DATA", "Start fetching");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://bruxellesdata.opendatasoft.com/api/records/1.0/search/?dataset=comic-book-route&rows=58")
                    .get().build();

            try {
                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(Objects.requireNonNull(response.body()).string());
                JSONArray jsonArray = jsonObject.getJSONArray("records");

                //TODO : Look into the following ...

                ArrayList<Location> locations = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject fields = jsonArray.getJSONObject(i).getJSONObject("fields");

                    final Location currentLocation = new Location(
                            Integer.parseInt(fields.getString("annee")),
                            (fields.has("personnage_s"))?fields.getString("personnage_s"):"Unspecified", //controle of iets ingevuld
                            fields.getString("auteur_s"),
                            (fields.has("photo"))?fields.getJSONObject("photo").getString("id"):"Unspecified",
                            fields.getString("coordonnees_geographiques")
                    );

                    locations.add(currentLocation);
                    LocationRoomDB.getDatabase(Objects.requireNonNull(getActivity()).getApplication()).locationDAO().insert(currentLocation);
                }


            } catch (IOException | JSONException exception) {
                exception.printStackTrace();
            }
        });
    }

}
