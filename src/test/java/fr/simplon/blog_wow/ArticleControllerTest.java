package fr.simplon.blog_wow;

import fr.simplon.blog_wow.api.ArticleController;
import fr.simplon.blog_wow.dao.ArticleRepository;
import fr.simplon.blog_wow.entity.Article;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class ArticleControllerTest {

    @Mock
    private ArticleRepository mRepository;

    @InjectMocks
    private ArticleController articleController;

    @Test
    public void testArticles() {
        // Création des données de test
        Article article1 = new Article(1L, "Titre 1", "Description 1", LocalDateTime.now(), "Créateur 1");
        Article article2 = new Article(2L, "Titre 2", "Description 2", LocalDateTime.now(), "Créateur 2");
        List<Article> articles = new ArrayList<>();
        articles.add(article1);
        articles.add(article2);

        // Définir le comportement du mock
        when(mRepository.findAll()).thenReturn(articles);

        // Appeler la méthode à tester
        List<Article> result = articleController.articles();

        // Vérifier le résultat
        assertEquals(articles.size(), result.size());
        assertEquals(article1.getId(), result.get(0).getId());
        assertEquals(article2.getId(), result.get(1).getId());
        assertEquals(article1.getTitle(), result.get(0).getTitle());
        assertEquals(article2.getTitle(), result.get(1).getTitle());
        assertEquals(article1.getDescription(), result.get(0).getDescription());
        assertEquals(article2.getDescription(), result.get(1).getDescription());
        assertEquals(article1.getCreatedAt().isEqual(result.get(0).getCreatedAt()), true);
        assertEquals(article2.getCreatedAt().isEqual(result.get(1).getCreatedAt()), true);
        assertEquals(article1.getCreatedBy(), result.get(0).getCreatedBy());
        assertEquals(article2.getCreatedBy(), result.get(1).getCreatedBy());
    }
}
