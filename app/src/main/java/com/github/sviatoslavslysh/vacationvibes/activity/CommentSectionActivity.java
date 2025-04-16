package com.github.sviatoslavslysh.vacationvibes.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.sviatoslavslysh.vacationvibes.R;
import com.github.sviatoslavslysh.vacationvibes.api.PlaceApiService;
import com.github.sviatoslavslysh.vacationvibes.model.AddCommentRequest;
import com.github.sviatoslavslysh.vacationvibes.model.AddReactionRequest;
import com.github.sviatoslavslysh.vacationvibes.model.AddReactionResponse;
import com.github.sviatoslavslysh.vacationvibes.model.Comment;
import com.github.sviatoslavslysh.vacationvibes.model.ErrorResponse;
import com.github.sviatoslavslysh.vacationvibes.repository.CommentRepository;
import com.github.sviatoslavslysh.vacationvibes.repository.PlaceRepository;
import com.github.sviatoslavslysh.vacationvibes.utils.CommentAdapter;
import com.github.sviatoslavslysh.vacationvibes.utils.HistoryCircularImageView;
import com.github.sviatoslavslysh.vacationvibes.utils.PlaceCallback;
import com.github.sviatoslavslysh.vacationvibes.utils.ToastManager;
import com.github.sviatoslavslysh.vacationvibes.utils.UserManager;
import com.google.gson.Gson;

import android.widget.TextView;
import android.widget.ImageView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentSectionActivity extends AppCompatActivity {

    private PlaceRepository placeRepository;
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
    private UserManager userManager = UserManager.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_section);

        // Retrieve data passed via Intent extras
        String extraPlaceTitle = getIntent().getStringExtra("EXTRA_PLACE_TITLE");
        String extraPlaceImageUrl = getIntent().getStringExtra("EXTRA_PLACE_IMAGE_URL");
        String extraUserName = getIntent().getStringExtra("EXTRA_USER_NAME");
        String extraPlaceId = getIntent().getStringExtra("EXTRA_PLACE_ID");

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
        recyclerView = findViewById(R.id.recyclerView);
        commentInput = findViewById(R.id.commentInput);
        addCommentButton = findViewById(R.id.addCommentButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        placeRepository = new PlaceRepository(this);
        fetchComments(extraPlaceId);

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentInput.getText().toString();
                commentInput.setText("");
                if (!commentText.isEmpty()) {
                    AddCommentRequest addCommentRequest = new AddCommentRequest(extraPlaceId, commentText, 5.0);

                    placeRepository.addComment(addCommentRequest, new PlaceCallback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                            String createdAt = sdf.format(new Date());
                            Comment comment = new Comment(
                                    "CID", "PID",
                                    userManager.getCurrentUser(),
                                    commentText, 5.0, createdAt
                            );
                            List<Comment> currentComments = new ArrayList<>(commentAdapter.getComments());
                            currentComments.add(0, comment);
                            commentAdapter.updateComments(currentComments);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            ToastManager.showToast(CommentSectionActivity.this, errorMessage);
                        }

                        @Override
                        public void onInvalidAuth() {
                            // Handle authentication error
                        }
                    });

                }
            }
        });
    }

    private void fetchComments(String placeId) {
        placeRepository.getComments(placeId, new PlaceCallback<List<Comment>>() {
            @Override
            public void onSuccess(List<Comment> comments) {
                // Successfully fetched the comments, update the RecyclerView
                commentAdapter = new CommentAdapter(comments);
                recyclerView.setAdapter(commentAdapter);
            }

            @Override
            public void onError(String errorMessage) {
                ToastManager.showToast(CommentSectionActivity.this, errorMessage);
            }

            @Override
            public void onInvalidAuth() {
                // Handle authentication error
            }
        });
    }
}
