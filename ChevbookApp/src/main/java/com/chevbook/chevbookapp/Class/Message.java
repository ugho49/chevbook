package com.chevbook.chevbookapp.Class;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ugho on 24/02/14.
 */
public class Message implements Serializable {

    //Variables
    private int id_annonce_destinataire;
    private String email_emetteur;
    private Date date_create_message;

    private String objet_message;
    private String contenu_message;

    private String NomPrenom_emetteur;
    private String url_image_emetteur;

    private String NomPrenom_destinataire;
    private String url_image_destinataire;

    private Boolean est_lu;


    public Message() {
        //empty constructor
    }

    public Message(int id_annonce_destinataire, String email_emetteur, Date date_create_message, String objet_message, String contenu_message, String nomPrenom_emetteur, String url_image_emetteur, String nomPrenom_destinataire, String url_image_destinataire) {
        this.id_annonce_destinataire = id_annonce_destinataire;
        this.email_emetteur = email_emetteur;
        this.date_create_message = date_create_message;
        this.objet_message = objet_message;
        this.contenu_message = contenu_message;
        NomPrenom_emetteur = nomPrenom_emetteur;
        this.url_image_emetteur = url_image_emetteur;
        NomPrenom_destinataire = nomPrenom_destinataire;
        this.url_image_destinataire = url_image_destinataire;
    }


    //Getters & Setters
    public Date getDate_create_message() {
        return date_create_message;
    }

    public void setDate_create_message(Date date_create_message) {
        this.date_create_message = date_create_message;
    }

    public int getId_annonce_destinataire() {
        return id_annonce_destinataire;
    }

    public void setId_annonce_destinataire(int id_annonce_destinataire) {
        this.id_annonce_destinataire = id_annonce_destinataire;
    }

    public String getEmail_emetteur() {
        return email_emetteur;
    }

    public void setEmail_emetteur(String email_emetteur) {
        this.email_emetteur = email_emetteur;
    }

    public String getObjet_message() {
        return objet_message;
    }

    public void setObjet_message(String objet_message) {
        this.objet_message = objet_message;
    }

    public String getContenu_message() {
        return contenu_message;
    }

    public void setContenu_message(String contenu_message) {
        this.contenu_message = contenu_message;
    }

    public String getNomPrenom_emetteur() {
        return NomPrenom_emetteur;
    }

    public void setNomPrenom_emetteur(String nomPrenom_emetteur) {
        NomPrenom_emetteur = nomPrenom_emetteur;
    }

    public String getUrl_image_emetteur() {
        return url_image_emetteur;
    }

    public void setUrl_image_emetteur(String url_image_emetteur) {
        this.url_image_emetteur = url_image_emetteur;
    }

    public String getNomPrenom_destinataire() {
        return NomPrenom_destinataire;
    }

    public void setNomPrenom_destinataire(String nomPrenom_destinataire) {
        NomPrenom_destinataire = nomPrenom_destinataire;
    }

    public String getUrl_image_destinataire() {
        return url_image_destinataire;
    }

    public void setUrl_image_destinataire(String url_image_destinataire) {
        this.url_image_destinataire = url_image_destinataire;
    }

    public Boolean getEst_lu() {
        return est_lu;
    }

    public void setEst_lu(Boolean est_lu) {
        this.est_lu = est_lu;
    }
}
