package fr.simplon.blog_wow.dao;

import fr.simplon.blog_wow.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository  extends JpaRepository<Article, Long> {
    // Permet que ce soit les articles les plus récents qui apparaissent en premier sur la page.
    @Query("SELECT a FROM Article a ORDER BY a.createdAt DESC")
    Page<Article> findAllOrderByCreatedAt(Pageable pPageable);

}




