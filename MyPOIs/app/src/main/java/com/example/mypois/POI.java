package com.example.mypois;

public class POI {
    private String Title;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    private String Description;
    private String Category;
    private String Location;
    private String Timestamp;
    public POI(String Title, String Description, String Category, String Location,String Timestamp){
        this.Title= Title;
        this.Description= Description;
        this.Category=Category;
        this.Location=Location;
        this.Timestamp=Timestamp;
    }

}
