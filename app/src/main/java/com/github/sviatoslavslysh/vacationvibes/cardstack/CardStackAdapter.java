package com.github.sviatoslavslysh.vacationvibes.cardstack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {
    private List<Place> places;

    public CardStackAdapter(List<Place> places) {
        this.places = Objects.requireNonNullElseGet(places, ArrayList::new);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.card_spot, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.name.setText(place.getName());
        if (place.getNote() != null) {  // note is not empty
            holder.location.setText(place.getNote());
        } else {  // note is empty
            holder.location.setText("51  miles away");  // todo calculate and show distance from user
        }
        Glide.with(holder.image)
                .load(place.getImages().get(0).getImageUrl())
                .into(holder.image);
        holder.itemView.setOnClickListener(v ->
                Toast.makeText(v.getContext(), place.getId(), Toast.LENGTH_SHORT).show()
        );
    }

    public void addPlace(Place place) {
        places.add(place);
        notifyItemInserted(places.size() - 1);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public List<Place> getPlaces() {
        return new ArrayList<>(places);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView name;
        public final TextView location;
        public final ImageView image;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.place);
            location = view.findViewById(R.id.location);
            image = view.findViewById(R.id.feed_image);
        }
    }
}

