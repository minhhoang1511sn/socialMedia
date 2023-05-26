package com.social.socialnetwork.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image")
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imgLink;
    @JsonBackReference(value = "images-post")
    @OneToOne(cascade = {CascadeType.ALL})
    private Post post;
    @JsonBackReference
    @ManyToOne(cascade = {CascadeType.ALL})
    private User user;
    private PostType postType;

    public Image(Object o, String imgLink, Post post) {
        this.imgLink = imgLink;
        this.post = post;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }
}
