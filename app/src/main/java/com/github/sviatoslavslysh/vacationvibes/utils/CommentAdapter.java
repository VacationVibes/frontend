package com.github.sviatoslavslysh.vacationvibes.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.model.Comment;

import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;

    public CommentAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.commentText.setText(comment.getComment());
        holder.commentAuthor.setText(comment.getUser().getName());
        holder.commentTime.setText(TimeAgo.getTimeAgo(comment.getCreatedAt()));
        if (comment.getRating() == 1.0) {
            holder.star1.setImageResource(R.drawable.ic_star_filled);
        } else if (comment.getRating() == 2.0) {
            holder.star1.setImageResource(R.drawable.ic_star_filled);
            holder.star2.setImageResource(R.drawable.ic_star_filled);
        } else if (comment.getRating() == 3.0) {
            holder.star1.setImageResource(R.drawable.ic_star_filled);
            holder.star2.setImageResource(R.drawable.ic_star_filled);
            holder.star3.setImageResource(R.drawable.ic_star_filled);

        } else if (comment.getRating() == 4.0) {
            holder.star1.setImageResource(R.drawable.ic_star_filled);
            holder.star2.setImageResource(R.drawable.ic_star_filled);
            holder.star3.setImageResource(R.drawable.ic_star_filled);
            holder.star4.setImageResource(R.drawable.ic_star_filled);

        } else if (comment.getRating() == 5.0) {
            holder.star1.setImageResource(R.drawable.ic_star_filled);
            holder.star2.setImageResource(R.drawable.ic_star_filled);
            holder.star3.setImageResource(R.drawable.ic_star_filled);
            holder.star4.setImageResource(R.drawable.ic_star_filled);
            holder.star5.setImageResource(R.drawable.ic_star_filled);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void updateComments(List<Comment> comments) {
        this.comments = comments;
        notifyDataSetChanged();
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView commentText;
        TextView commentAuthor;
        TextView commentTime;
        ImageView star1, star2, star3, star4, star5;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentText);
            commentAuthor = itemView.findViewById(R.id.commentAuthor);
            commentTime = itemView.findViewById(R.id.commentTime);
            star1 = itemView.findViewById(R.id.star1);
            star2 = itemView.findViewById(R.id.star2);
            star3 = itemView.findViewById(R.id.star3);
            star4 = itemView.findViewById(R.id.star4);
            star5 = itemView.findViewById(R.id.star5);
        }
    }
}