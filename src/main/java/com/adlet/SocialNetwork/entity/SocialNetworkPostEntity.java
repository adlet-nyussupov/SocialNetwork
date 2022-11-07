package com.adlet.SocialNetwork.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "Posts")
public class SocialNetworkPostEntity implements Serializable {

    private static final long serialVersionUID = 6657425562040976418L;

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String postId;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(nullable = false)
    private Date postDate;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, length = 100)
    private long viewCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}
