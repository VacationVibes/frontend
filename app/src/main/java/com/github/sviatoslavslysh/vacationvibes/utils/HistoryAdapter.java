package com.github.sviatoslavslysh.vacationvibes.utils;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.activity.CommentSectionActivity;
import com.github.sviatoslavslysh.vacationvibes.model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ActionViewHolder> {

    private ArrayList<Place> places;
    private LocationHelper locationHelper;

    public HistoryAdapter(ArrayList<Place> actions, LocationHelper locationHelper) {
        this.places = actions != null ? actions : new ArrayList<>();
        this.locationHelper = locationHelper;
    }

    public static class ActionViewHolder extends RecyclerView.ViewHolder {
        TextView title, distance;
        ImageView imageView;
        ImageView reactionImage;
        ImageButton commentButton;

        public ActionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.history_place_name);
            distance = itemView.findViewById(R.id.history_place_distance);
            imageView = itemView.findViewById(R.id.history_place_image);
            reactionImage = itemView.findViewById(R.id.history_place_reaction);
            commentButton = itemView.findViewById(R.id.comment_button);
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
        double distance = locationHelper.calculateDistanceTo(place.getLatitude(), place.getLongitude());
        holder.distance.setText(String.format(Locale.US, "%.2f miles away", distance));

        // Comment button click listener
        holder.commentButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CommentSectionActivity.class);
            intent.putExtra("EXTRA_PLACE_TITLE", place.getName());
            String imageUrl = "";
            if (!place.getImages().isEmpty()) {
                imageUrl = place.getImages().get(0).getImageUrl();
            }
            intent.putExtra("EXTRA_PLACE_IMAGE_URL", imageUrl);
            // Replace with the actual user name (or pass a whole User if it's Parcelable/Serializable)
            intent.putExtra("EXTRA_USER_NAME", "John Doe");
            v.getContext().startActivity(intent);
        });

        // Set reaction image based on the reaction
        if (!place.getReactions().isEmpty()) {
            String reaction = place.getReactions().get(0).getReaction();
            if (reaction.equals("like")) {
                holder.reactionImage.setImageResource(R.drawable.baseline_thumb_up_24);
            } else if (reaction.equals("dislike")) {
                holder.reactionImage.setImageResource(R.drawable.baseline_thumb_down_24);
            }
        }

        // Load image using Glide
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
