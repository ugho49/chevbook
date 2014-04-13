package com.chevbook.chevbookapp.Class;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ugho on 24/02/14.
 */
public class Message implements Serializable {

    //Variables
    private Date date_create_message;

    private String titre_annonce; //titre annonce
    private String contenu_message;

    private String email_emetteur;
    private String NomPrenom_emetteur;
    private String url_image_emetteur; //avatar

    private int id_annonce_destinataire;
    private String NomPrenom_destinataire;
    private String url_image_destinataire; //avatar

    private Boolean est_lu;


    public Message() {
        //empty constructor
    }

    public Message(int id_annonce_destinataire, String email_emetteur, Date date_create_message, String titre_annonce, String contenu_message, String nomPrenom_emetteur, String url_image_emetteur, String nomPrenom_destinataire, String url_image_destinataire, Boolean est_lu) {
        this.id_annonce_destinataire = id_annonce_destinataire;
        this.email_emetteur = email_emetteur;
        this.date_create_message = date_create_message;
        this.titre_annonce = titre_annonce;
        this.contenu_message = contenu_message;
        this.NomPrenom_emetteur = nomPrenom_emetteur;
        this.url_image_emetteur = url_image_emetteur;
        this.NomPrenom_destinataire = nomPrenom_destinataire;
        this.url_image_destinataire = url_image_destinataire;
        this.est_lu = est_lu;
    }

    public void InstantiateByMessage(Message m) {
        this.id_annonce_destinataire = m.getId_annonce_destinataire();
        this.email_emetteur = m.getEmail_emetteur();
        this.date_create_message = m.getDate_create_message();
        this.titre_annonce = m.getTitre_annonce();
        this.contenu_message = m.getContenu_message();
        this.NomPrenom_emetteur = m.getNomPrenom_emetteur();
        this.url_image_emetteur = m.getUrl_image_emetteur();
        this.NomPrenom_destinataire = m.getNomPrenom_destinataire();
        this.url_image_destinataire = m.getUrl_image_destinataire();
        this.est_lu = m.getEst_lu();
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

    public String getTitre_annonce() {
        return titre_annonce;
    }

    public void setTitre_annonce(String titre_annonce) {
        this.titre_annonce = titre_annonce;
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
