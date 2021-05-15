package com.example.socialmediaapp.ModelClasses;

public class Model_GetImageComments {
    private String comment, commentperson, currentdatetime, profilepicurl;

    public Model_GetImageComments() {
    }

    public Model_GetImageComments(String comment, String commentperson, String currentatetime, String profilepicurl) {
        this.comment = comment;
        this.commentperson = commentperson;
        this.currentdatetime = currentatetime;
        this.profilepicurl = profilepicurl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentperson() {
        return commentperson;
    }

    public void setCommentperson(String commentperson) {
        this.commentperson = commentperson;
    }

    public String getCurrentdatetime() {
        return currentdatetime;
    }

    public void setCurrentdatetime(String currentatetime) {
        this.currentdatetime = currentatetime;
    }

    public String getProfilepicurl() {
        return profilepicurl;
    }

    public void setProfilepicurl(String profilepicurl) {
        this.profilepicurl = profilepicurl;
    }
}
