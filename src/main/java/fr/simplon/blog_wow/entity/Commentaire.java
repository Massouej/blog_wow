package fr.simplon.blog_wow.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class Commentaire {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @ManyToOne(
            optional = false
    )
    @JoinColumn(
            name = "id_article",
            referencedColumnName = "id",
            nullable = false
    )
    @JsonIgnore
    private Article article;
    private String description;


    @Column(
            nullable = false
    )
    private LocalDateTime commentAt;

    @Column(
            nullable = false
    )
    private @NotNull @NotBlank String user;

    @Column(nullable = false)
    private boolean flagged;

    public Commentaire(Long pId,String pDescription, Article pArticle, LocalDateTime pCommentAt, String pUser, Boolean pFlagged) {
        this.id = pId;
        this.description = pDescription;
        this.article = pArticle;
        this.commentAt = pCommentAt;
        this.user = pUser;
        this.flagged = pFlagged;
    }

    public Commentaire() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long pId) {
        this.id = pId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String pDescription) {
        this.description = pDescription;
    }

    public Article getArticle() {
        return this.article;
    }

    public void setArticle(Article pArticle) {
        this.article = pArticle;
    }



    public LocalDateTime getCommentAt() {
        return this.commentAt;
    }

    public void setCommentAt(LocalDateTime pCommentAt) {
        this.commentAt = pCommentAt;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String pUser) {
        this.user = pUser;
    }

    public boolean getFlagged() {
        return this.flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }


}
