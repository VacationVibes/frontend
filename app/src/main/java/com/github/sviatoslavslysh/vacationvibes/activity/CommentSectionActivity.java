package com.github.sviatoslavslysh.vacationvibes.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.model.Comment;
import com.github.sviatoslavslysh.vacationvibes.repository.CommentRepository;
import com.github.sviatoslavslysh.vacationvibes.utils.CommentAdapter;
import com.github.sviatoslavslysh.vacationvibes.utils.HistoryCircularImageView;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.List;

public class CommentSectionActivity extends AppCompatActivity {

    private CommentRepository commentRepository;
    private CommentAdapter commentAdapter;
    private RecyclerView recyclerView;
    private EditText commentInput;
    private Button addCommentButton;

    // Top section views
    private HistoryCircularImageView placeImage;
    private TextView placeTitle;
    private TextView userName;
    // Star icons (if needed)
    private ImageView star1, star2, star3, star4, star5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_section);

        // Retrieve data passed via Intent extras
        String extraPlaceTitle = getIntent().getStringExtra("EXTRA_PLACE_TITLE");
        String extraPlaceImageUrl = getIntent().getStringExtra("EXTRA_PLACE_IMAGE_URL");
        String extraUserName = getIntent().getStringExtra("EXTRA_USER_NAME");

        // Initialize top section views (using HistoryCircularImageView for the picture)
        placeImage = findViewById(R.id.placeImage);
        placeTitle = findViewById(R.id.placeTitle);
        userName = findViewById(R.id.userName);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);

        // Set the received values
        if (extraPlaceTitle != null) {
            placeTitle.setText(extraPlaceTitle);
        }
        if (extraUserName != null) {
            userName.setText(extraUserName);
        }
        if (extraPlaceImageUrl != null && !extraPlaceImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(extraPlaceImageUrl)
                    .placeholder(R.drawable.baseline_hide_image_24)
                    .into(placeImage);
        }

        // Optionally, set star icons (replace ic_star with your resource)
        star1.setImageResource(R.drawable.ic_star);
        star2.setImageResource(R.drawable.ic_star);
        star3.setImageResource(R.drawable.ic_star);
        star4.setImageResource(R.drawable.ic_star);
        star5.setImageResource(R.drawable.ic_star);

        // Initialize comment repository, RecyclerView, and adapter for comments
        commentRepository = new CommentRepository();
        recyclerView = findViewById(R.id.recyclerView);
        commentInput = findViewById(R.id.commentInput);
        addCommentButton = findViewById(R.id.addCommentButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Comment> initialComments = commentRepository.getComments();
        commentAdapter = new CommentAdapter(initialComments);
        recyclerView.setAdapter(commentAdapter);

        // Submit button click listener to add a new comment
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentInput.getText().toString();
                if (!commentText.isEmpty()) {
                    Comment comment = new Comment(commentText);
                    commentRepository.addComment(comment);
                    commentAdapter.updateComments(commentRepository.getComments());
                    commentInput.setText("");
                }
            }
        });
    }
}
