package com.psteam.lib.modeluser;

import java.util.ArrayList;

public class GetCommentAndLikeReview {
    private String notification;

    private ArrayList<CommentModel> comments;

    private ArrayList<UserLike> getlike;

    private String status;

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public ArrayList<CommentModel> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentModel> comments) {
        this.comments = comments;
    }

    public ArrayList<UserLike> getGetlike() {
        return getlike;
    }

    public void setGetlike(ArrayList<UserLike> getlike) {
        this.getlike = getlike;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}