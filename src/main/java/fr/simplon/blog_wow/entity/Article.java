package fr.simplon.blog_wow.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;



@Entity
public class Article {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Size(min = 3, max = 50)
    private String title;
    private String description;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    @NotBlank
    private String createdBy;

    @OneToMany(
            mappedBy = "article"
    )
    @OrderBy("commentAt DESC")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Collection<Commentaire> commentaires;


    public Article() {
        super();
        this.createdAt = LocalDateTime.now();
    }

    public Article(
            Long pId,
            String pTitle,
            String pDescription,
            LocalDateTime pCreatedAt,
            String pCreatedBy) {
        super();
        setId(pId);
        setTitle(pTitle);
        setDescription(pDescription);
        setCreatedAt(pCreatedAt);
        setCreatedBy(pCreatedBy);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long pId) {
        id = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime pCreatedAt) {
        createdAt = pCreatedAt;
        if (createdAt != null) {
            createdAt = createdAt.truncatedTo(ChronoUnit.SECONDS);
        }
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String pCreatedBy) {
        createdBy = pCreatedBy;
    }

    public Collection<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(Collection<Commentaire> pCommentaires) {
        commentaires = pCommentaires;
    }


}
