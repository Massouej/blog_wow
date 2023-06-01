package fr.simplon.blog_wow.web;

import fr.simplon.blog_wow.dao.ArticleRepository;
import fr.simplon.blog_wow.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Contrôleur pour URLs qui retournent des pages HTML sans JSON
 */

@Controller
public class WebController {

    /** Nombre d'articles par page de résultats. */

    public static final int DEFAULT_PAGE_COUNT = 5;

    private ArticleRepository mArticleRepository;

    /**
     * Constructeur.
     *
     * @param pArticleRepository
     */
    @Autowired
    public WebController(ArticleRepository pArticleRepository) { mArticleRepository = pArticleRepository;}

    /**
     * Page d'accueil.
     *
     * @param page Numéro de page d'articles.
     * @param model Modèle Thymeleaf.
     * @return La page d'accueil HTML.
     */

    @GetMapping(path = {"/", "/index"})
    public String index(
        @RequestParam(required = false, defaultValue = "0") Integer page, Model model)
    {
        fillModelWithPaginationAttributes(model, page);
        model.addAttribute("newArticle", new Article());

        return "index";
    }


    /**
     * Fournit le HTML correspondant à la liste de tous les articles (avec pagination).
     *
     * @param page Numéro de la page demandée.
     * @param model Modèle Thymeleaf.
     * @return Le HTML correspondant à la liste des articles demandés.
     */
    @GetMapping(path = "/fragments/articles")
    public String fragmentArticles(
            @RequestParam(required = false, defaultValue = "0") Integer page, Model model)
    {
        fillModelWithPaginationAttributes(model, page);
        return "index :: all-articles";
    }

    /**
     * Fournit le HTML correspondant à un seul article (fragment).
     *
     * @param id Identifiant de l'article.
     * @param model Modèle Thymeleaf.
     * @return Le contenu du fragment Thymeleaf correspondant à l'article demandé.
     */

    @GetMapping(path = "/fragments/articles/{id}")
    public String fragmentArticle(@PathVariable Long id, Model model)
    {
        Optional<Article> article = mArticleRepository.findById(id);
        model.addAttribute("article", article.get());
        return "fragment-article :: single-article";
    }

    /**
     * Remplissage du modèle avec les attributs liés à la pagination.
     *
     * @param model Modèle à remplir.
     * @param page Numéro de page courante.
     */

    private void fillModelWithPaginationAttributes(Model model, int page)
    {
        long count = mArticleRepository.count();
        long pageCount = count % DEFAULT_PAGE_COUNT > 0L ? (count / DEFAULT_PAGE_COUNT + 1) : (count / DEFAULT_PAGE_COUNT);
        List<Article> all = getArticles(page);
        model.addAttribute("articles", all);
        model.addAttribute("page", page);
        model.addAttribute("pageCount", pageCount);
    }

    /**
     * Retourne la liste des articles avec d'abord les articles les plus récents.
     *
     * @param page Le numéro de page demandée.
     * @return
     */

    private List<Article> getArticles(int page)
    {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_COUNT, Sort.by("createdAt").ascending());
        Page<Article> all = mArticleRepository.findAllOrderByCreatedAt(pageable);
        List<Article> openArticles = all.filter(article -> article.getCreatedAt().compareTo(LocalDateTime.now()) > 0).stream().collect(Collectors.toList());
        List<Article> closedArticles = all.filter(article -> article.getCreatedAt().compareTo(LocalDateTime.now()) < 0).stream().collect(Collectors.toList());
        openArticles.addAll(closedArticles);
        return openArticles;
    }

    /**
     * Affiche le formulaire de création d'article.
     * @param model Le modèle Thymeleaf pour transmettre les données à la vue.
     * @return Le nom de la vue Thymeleaf pour afficher le formulaire de création d'article.
     */
    @GetMapping("/admin/create_new_article")
    public String showCreateArticleForm(Model model) {
        model.addAttribute("newArticle", new Article());
        return "admin/create_new_article";
    }







}
