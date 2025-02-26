package com.github.sviatoslavslysh.vacationvibes.repository;

import com.github.sviatoslavslysh.vacationvibes.model.Comment;
import java.util.ArrayList;
import java.util.List;

public class CommentRepository {
    private List<Comment> comments;

    public CommentRepository() {
        comments = new ArrayList<>();
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }
}