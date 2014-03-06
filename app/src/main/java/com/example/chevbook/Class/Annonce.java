package com.example.chevbook.Class;

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
    private int number_room_annonce;
    private int surface_annonce; //m²

    private String adresse_annonce;
    private String ville_annonce;
    private String code_postal_annonce;

    private String categorie_annonce;
    private String type_location_annonce;
    private String quartier_annonce;

    private ArrayList<String> url_images_annonces = new ArrayList<String>();

    //Constructor
    public Annonce() {
        //Todo : delete this -->
        id_annonce = 1212121;
        date_create_annonce = new Date();
        titre_annonce = "Appartement T3 meublé";
        prix_annonce = 378.35;
        description_annonce = "Studio de 18m² au RDC sur cour, comprenant pièce principale, cuisine, salle d'eau avec wc et dressing. Proche toutes commodités. Libre de suite ! Environ 10min à pied du RER A. Garantie et caution demandées.";
        email_user_annonce = "stephan.ugho@gmail.com";
        number_room_annonce = 4;
        surface_annonce = 30;

        adresse_annonce = "4 rue d'iéna";
        ville_annonce = "Angers";
        code_postal_annonce = "49000";

        categorie_annonce = "loft";
        quartier_annonce = "Belle-beille";
        type_location_annonce = "";

        url_images_annonces.add("http://media-cdn.tripadvisor.com/media/photo-s/03/8f/5c/42/appartements-riemergasse.jpg");
        url_images_annonces.add("http://www.apartmentbarcelona.com/fr/appartements-de-luxe/imageWeb/Apartamentos/fr/ID283/appartements-barcelone-283-0.jpg");
    }

    //Constructor
    public Annonce(int id, Date date) {
        id_annonce = id;
        date_create_annonce = date;
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

    public String getVille_annonce() {
        return ville_annonce;
    }

    public String getCode_postal_annonce() {
        return code_postal_annonce;
    }

    public String getCategorie_annonce() {
        return categorie_annonce;
    }

    public String getType_location_annonce() {
        return type_location_annonce;
    }

    public String getQuartier_annonce() {
        return quartier_annonce;
    }

    public ArrayList<String> getUrl_images_annonces() {
        return url_images_annonces;
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

    public void setVille_annonce(String ville_annonce) {
        this.ville_annonce = ville_annonce;
    }

    public void setCode_postal_annonce(String code_postal_annonce) {
        this.code_postal_annonce = code_postal_annonce;
    }

    public void setCategorie_annonce(String categorie_annonce) {
        this.categorie_annonce = categorie_annonce;
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
}