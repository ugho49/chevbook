package com.example.chevbook.Class;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ugho on 24/02/14.
 */
public class Message implements Serializable {

    private int id_message;
    private Date create_date_message;
    private String sender_user_pseudo;
    private String receiver_user_pseudo;
    private String url_image_sender_user;
    private String url_image_receiver_user;
    private String objet_message;
    private String content_message;


    public Message() {
    }

    public Message(int id_message, Date create_date_message) {
        this.id_message = id_message;
        this.create_date_message = create_date_message;
    }
}
