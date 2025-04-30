package com.github.sviatoslavslysh.vacationvibes.cardstack;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.activity.CommentSectionActivity;
import com.github.sviatoslavslysh.vacationvibes.model.Place;
import com.github.sviatoslavslysh.vacationvibes.utils.LocationHelper;
import com.github.sviatoslavslysh.vacationvibes.utils.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {
    private List<Place> places;
    private LocationHelper locationHelper;

    public CardStackAdapter(List<Place> places, LocationHelper locationHelper) {
        this.places = Objects.requireNonNullElseGet(places, ArrayList::new);
        this.locationHelper = locationHelper;
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
            double distance = locationHelper.calculateDistanceTo(place.getLatitude(), place.getLongitude());
            if (distance < 0) {
                holder.location.setText("Enable GPS to see the distance to the place");
            } else {
                holder.location.setText(String.format(Locale.US, "%.2f miles away", distance));
            }
        }
        int placeholderDrawableId = R.drawable.baseline_hide_image_24;
        Drawable placeholder = ContextCompat.getDrawable(holder.itemView.getContext(), placeholderDrawableId);

        Glide.with(holder.image)
                .load(place.getImages() != null && !place.getImages().isEmpty() ?
                        place.getImages().get(0).getImageUrl() : placeholder)
                .placeholder(placeholder)
                .error(placeholder)
                .transform(new RoundedCorners(50))
                .into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CommentSectionActivity.class);
            intent.putExtra("EXTRA_PLACE_TITLE", place.getName());
                    String imageUrl = "";
                    if (!place.getImages().isEmpty()) {
                        imageUrl = place.getImages().get(0).getImageUrl();
                    }
                    intent.putExtra("EXTRA_PLACE_IMAGE_URL", imageUrl);
                    UserManager userManager = UserManager.getInstance();
                    intent.putExtra("EXTRA_USER_NAME", userManager.getCurrentUser().getName());
                    intent.putExtra("EXTRA_PLACE_ID", place.getId());
                    v.getContext().startActivity(intent);
        }

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

