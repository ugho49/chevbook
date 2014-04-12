package com.chevbook.chevbookapp.Class;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ugho on 28/01/14.
 */
public class Annonce implements Serializable {

    private int id_annonce;
    private Date date_create_annonce;

    private String titre_annonce;
    private double prix_annonce;
    private String description_annonce;

    private String email_user_annonce;
    private String pseudo_user_annonce;
    private String avatar_user_annonce;

    private int number_room_annonce;
    private int surface_annonce; //m²

    private String adresse_annonce;

    private String categorie_annonce;
    private String sous_categorie_annonce;

    private String type_location_annonce;
    private String quartier_annonce;

    private boolean est_meuble;

    private ArrayList<String> url_images_annonces = new ArrayList<String>();

    //Constructor
    public Annonce() {
        //empty constructor
    }

    public Annonce(int id_annonce, Date date_create_annonce, String titre_annonce, double prix_annonce, String description_annonce, String email_user_annonce, String pseudo_user_annonce, String avatar_user_annonce, int number_room_annonce, int surface_annonce, String adresse_annonce, String categorie_annonce, String sous_categorie_annonce, String type_location_annonce, String quartier_annonce, boolean est_meuble, ArrayList<String> url_images_annonces) {
        this.id_annonce = id_annonce;
        this.date_create_annonce = date_create_annonce;
        this.titre_annonce = titre_annonce;
        this.prix_annonce = prix_annonce;
        this.description_annonce = description_annonce;
        this.email_user_annonce = email_user_annonce;
        this.pseudo_user_annonce = pseudo_user_annonce;
        this.avatar_user_annonce = avatar_user_annonce;
        this.number_room_annonce = number_room_annonce;
        this.surface_annonce = surface_annonce;
        this.adresse_annonce = adresse_annonce;
        this.categorie_annonce = categorie_annonce;
        this.sous_categorie_annonce = sous_categorie_annonce;
        this.type_location_annonce = type_location_annonce;
        this.quartier_annonce = quartier_annonce;
        this.est_meuble = est_meuble;
        this.url_images_annonces = url_images_annonces;
    }

    //Getters
    public String getDescription_annonce() {
        return description_annonce;
    }

    public int getId_annonce() {
        return id_annonce;
    }

    public Date getDate_create_annonce() {
        return date_create_annonce;
    }

    public String getTitre_annonce() {
        return titre_annonce;
    }

    public double getPrix_annonce() {
        return prix_annonce;
    }

    public String getEmail_user_annonce() {
        return email_user_annonce;
    }

    public int getNumber_room_annonce() {
        return number_room_annonce;
    }

    public int getSurface_annonce() {
        return surface_annonce;
    }

    public String getAdresse_annonce() {
        return adresse_annonce;
    }

    public String getCategorie_annonce() {
        return categorie_annonce;
    }

    public String getSousCategorie_annonce() {
        return sous_categorie_annonce;
    }

    public String getType_location_annonce() {
        return type_location_annonce;
    }

    public String getQuartier_annonce() {
        return quartier_annonce;
    }

    public Boolean get_isMeuble() {
        return est_meuble;
    }

    public ArrayList<String> getUrl_images_annonces() {
        return url_images_annonces;
    }

    public String getPseudo_user_annonce() {
        return pseudo_user_annonce;
    }

    public String getAvatar_user_annonce() {
        return avatar_user_annonce;
    }

    //Setters
    public void setTitre_annonce(String titre_annonce) {
        this.titre_annonce = titre_annonce;
    }

    public void setPrix_annonce(double prix_annonce) {
        this.prix_annonce = prix_annonce;
    }

    public void setDescription_annonce(String description_annonce) {
        this.description_annonce = description_annonce;
    }

    public void setEmail_user_annonce(String email_user_annonce) {
        this.email_user_annonce = email_user_annonce;
    }

    public void setNumber_room_annonce(int number_room_annonce) {
        this.number_room_annonce = number_room_annonce;
    }

    public void setSurface_annonce(int surface_annonce) {
        this.surface_annonce = surface_annonce;
    }

    public void setAdresse_annonce(String adresse_annonce) {
        this.adresse_annonce = adresse_annonce;
    }

    public void setCategorie_annonce(String categorie_annonce) {
        this.categorie_annonce = categorie_annonce;
    }

    public void setSousCategorie_annonce(String sous_categorie_annonce) {
        this.sous_categorie_annonce = sous_categorie_annonce;
    }

    public void setType_location_annonce(String type_location_annonce) {
        this.type_location_annonce = type_location_annonce;
    }

    public void setQuartier_annonce(String quartier_annonce) {
        this.quartier_annonce = quartier_annonce;
    }

    public void setUrl_images_annonces(ArrayList<String> url_images_annonces) {
        this.url_images_annonces = url_images_annonces;
    }

    public void setPseudo_user_annonce(String pseudo_user_annonce) {
        this.pseudo_user_annonce = pseudo_user_annonce;
    }

    public void setAvatar_user_annonce(String avatar_user_annonce) {
        this.avatar_user_annonce = avatar_user_annonce;
    }

    public void set_isMeuble(boolean isMeuble) {
        this.est_meuble = isMeuble;
    }
}
