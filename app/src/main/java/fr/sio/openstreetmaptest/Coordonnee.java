package fr.sio.openstreetmaptest;

public class Coordonnee {
    private double latitude;
    private double longitude;
    private String nom;
    private String description;

    public Coordonnee(double latitude, double longitude, String nom, String description) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.nom = nom;
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }


}
