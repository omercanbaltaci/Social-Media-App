package com.example.socialmediaapp.ModelClasses;

public class Model_GetTextComments {
    private String comment, commentperson, currentdatetime, profilepicurl;

    public Model_GetTextComments() {

    }

    public Model_GetTextComments(String comment, String commentperson, String currentdatetime, String profilepicurl) {
        this.comment = comment;
        this.commentperson = commentperson;
        this.currentdatetime = currentdatetime;
        this.profilepicurl = profilepicurl;
    }


    public String getProfilepicurl() {
        return profilepicurl;
    }

    public void setProfilepicurl(String profilepicurl) {
        this.profilepicurl = profilepicurl;
    }

    public String getCurrentdatetime() {
        return currentdatetime;
    }

    public void setCurrentdatetime(String currentdatetime) {
        this.currentdatetime = currentdatetime;
    }

    public String getCommentperson() {
        return commentperson;
    }

    public void setCommentperson(String commentperson) {
        this.commentperson = commentperson;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
