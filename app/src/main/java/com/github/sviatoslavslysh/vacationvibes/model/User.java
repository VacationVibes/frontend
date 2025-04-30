package com.github.sviatoslavslysh.vacationvibes.model;

public class User {
    private String id;
    private String email;
    private String name;
    private Integer likes;
    private Integer dislikes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void addLike() {
        this.likes = this.getLikes() + 1;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public void addDislike() {
        this.dislikes = this.getDislikes() + 1;
    }
}
