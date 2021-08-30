package com.erenmeric.yuruyus_arkadasim.Model;

public class Post {
    private String imageUrl;
    private String postId;
    private String publisher;

    @Override
    public String toString() {
        return "Post{" +
                "imageUrl='" + imageUrl + '\'' +
                ", postId='" + postId + '\'' +
                ", publisher='" + publisher + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    private String description;

    public Post() {

    }

    public Post(String imageUrl, String postId, String publisher, String description) {
        this.imageUrl = imageUrl;
        this.postId = postId;
        this.publisher = publisher;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
