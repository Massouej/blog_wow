package fr.simplon.blog_wow.dao;

import fr.simplon.blog_wow.entity.Article;
import fr.simplon.blog_wow.entity.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    @Query("SELECT c FROM Commentaire c WHERE c.article = :article AND c.user = :user")
    List<Commentaire> findByArticleAndUser(Article article, String user);
}
