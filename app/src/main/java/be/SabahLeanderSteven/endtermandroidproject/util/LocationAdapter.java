package be.SabahLeanderSteven.endtermandroidproject.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import be.SabahLeanderSteven.endtermandroidproject.R;
import be.SabahLeanderSteven.endtermandroidproject.model.Location;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> implements Filterable {

    class LocationViewHolder extends RecyclerView.ViewHolder {
        // Fields
        final ImageView photoIV;
        final TextView charactersTV, authorTV;

        // Constructor
        LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            //rij nummer zoveel =>  getAdapterPosition();
            // Constructor params
            this.photoIV = itemView.findViewById(R.id.card_iv);
            this.charactersTV = itemView.findViewById(R.id.card_characters_tv);
            this.authorTV = itemView.findViewById(R.id.card_authors_tv);
        }
    }

    private ArrayList<Location> items;
    private ArrayList<Location> OGitems;

    public LocationAdapter(FragmentActivity activity) {
        items = new ArrayList<>();
        OGitems = new ArrayList<>();
    }

    // ADD LOCATIONS TO RV
    public void addLocations(List<Location> locations) {
        items.clear();
        items.addAll(locations);
        OGitems = items;
    }

    // LIFECYCLE
    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View card = layoutInflater.inflate(R.layout.location_card, parent, false);

        return new LocationViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        if (items.size() > 0 && position < items.size()) {
            Location currentLocation = items.get(position);

            if (!currentLocation.getCharacters().equals("Unspecified")) {
                holder.charactersTV.setText(currentLocation.getCharacters());
            }

            holder.authorTV.setText(currentLocation.getAuthors());

            switch (currentLocation.getType()) {
                case "COMICS":
                    if (!currentLocation.getPhoto().equals("Unspecified")) {
                        String path = "https://opendata.brussel.be/explore/dataset/striproute0/files/"
                                + currentLocation.getPhoto()
                                + "/download";
                        Picasso.get().load(path).into(holder.photoIV);
                    }
                    break;
                case "GRAFFITI":
                    if (!currentLocation.getPhoto().equals("Unspecified")) {
                        String path = "https://opendata.brussel.be/explore/dataset/street-art/files/" + currentLocation.getPhoto() + "/download";
                        Picasso.get().load(path).into(holder.photoIV);
                    }
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //TODO: Override rv filter
    @Override
    public Filter getFilter() {
        return null;
    }
}
