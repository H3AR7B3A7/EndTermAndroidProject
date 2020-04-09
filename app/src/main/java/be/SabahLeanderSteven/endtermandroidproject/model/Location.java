package be.SabahLeanderSteven.endtermandroidproject.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "locations")
public class Location {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int year;
    private String characters, authors, photo, type;
    private double geoLat, geolong;

    public Location( int year, String characters, String authors, String photo, String type, double geoLat, double geolong) {
        this.year = year;
        this.characters = characters;
        this.authors = authors;
        this.photo = photo;
        this.type = type;
        this.geoLat = geoLat;
        this.geolong = geolong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getCharacters() {
        return characters;
    }

    public void setCharacters(String characters) {
        this.characters = characters;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(double geoLat) {
        this.geoLat = geoLat;
    }

    public double getGeolong() {
        return geolong;
    }

    public void setGeolong(double geolong) {
        this.geolong = geolong;
    }
}
