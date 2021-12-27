package com.psteam.lib.modeluser;

public class GetReviewById {
    private String notification;

    private ReviewModel review;

    private String reviewTotal;

    private String status;

    public String getNotification ()
    {
        return notification;
    }

    public void setNotification (String notification)
    {
        this.notification = notification;
    }

    public ReviewModel getReview ()
    {
        return review;
    }

    public void setReview (ReviewModel review)
    {
        this.review = review;
    }

    public String getReviewTotal ()
    {
        return reviewTotal;
    }

    public void setReviewTotal (String reviewTotal)
    {
        this.reviewTotal = reviewTotal;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

}
