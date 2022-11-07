package com.adlet.SocialNetwork.model.request;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@RequestMapping
public class SocialNetworkPostRequestModel {

    private String author;
    private String content;
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
