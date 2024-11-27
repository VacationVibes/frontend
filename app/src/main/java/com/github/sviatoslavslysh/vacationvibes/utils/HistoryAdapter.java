package com.github.sviatoslavslysh.vacationvibes.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ActionViewHolder> {

    private ArrayList<Place> places;

    public HistoryAdapter(ArrayList<Place> actions) {
        this.places = actions != null ? actions : new ArrayList<>();
    }

    public static class ActionViewHolder extends RecyclerView.ViewHolder {
        TextView title, distance;
        ImageView imageView;
        ImageView reactionImage;

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.history_place_name);
            distance = itemView.findViewById(R.id.history_place_distance);
            imageView = itemView.findViewById(R.id.history_place_image);
            reactionImage = itemView.findViewById(R.id.history_place_reaction);
        }
    }

    @NonNull
    @Override
    public ActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new ActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionViewHolder holder, int position) {
        Place place = places.get(position);
        holder.title.setText(place.getName());
        // todo add distance, city, reaction, image etc
        Random random = new Random();
        int randomNumber = random.nextInt(39) + 2;
        holder.distance.setText(randomNumber + " miles away");
        if (place.getReactions().get(0).getReaction().equals("like")) {
            holder.reactionImage.setImageResource(R.drawable.baseline_thumb_up_24);
        } else if (place.getReactions().get(0).getReaction().equals("dislike")) {
            holder.reactionImage.setImageResource(R.drawable.baseline_thumb_down_24);
        }
        if (!place.getImages().isEmpty()) {
            String imageUrl = place.getImages().get(0).getImageUrl();
            Glide.with(holder.imageView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.baseline_hide_image_24)
                    .error(R.drawable.baseline_hide_image_24)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public List<Place> getPlaces() {
        return new ArrayList<>(places);
    }

    public void addPlace(Place newPlace) {
        places.add(newPlace);
        notifyItemInserted(places.size() - 1);
    }

    public void clean() {
        places = new ArrayList<>();
    }
}
