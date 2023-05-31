package fr.simplon.blog_wow.api;


import fr.simplon.blog_wow.dao.ArticleRepository;
import fr.simplon.blog_wow.entity.Article;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Contrôleur CRUD pour les articles.
 */
@RestController
@RequestMapping("/api")
public class ArticleController {
    private ArticleRepository mRepository;

    @Autowired
    public ArticleController(ArticleRepository pRepository) { mRepository = pRepository;}

    @GetMapping(path = "/articles")
    @ApiResponse(responseCode = "200", description = "Les ressources ont été trouvées et renvoyées avec succès.")
    public List<Article> articles()
    {
        List<Article> all = mRepository.findAll();
        return all;
    }

    @GetMapping(path = "/articles/{id}")
    @ApiResponse(responseCode = "200", description = "La ressource a été trouvée et renvoyée avec succès.")
    @ApiResponse(responseCode = "404", description = "La ressource n'existe pas.")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id)
    {
        return ResponseEntity.of(mRepository.findById(id));
    }

    @PostMapping(path = "/articles")
    @ApiResponse(responseCode = "201", description = "La ressource a été trouvée avec succès.")
    @ApiResponse(responseCode = "400", description = "En cas d'erreur de validation.")
    public ResponseEntity<?> createArticle(
            @Valid @RequestBody Article article, BindingResult validation, HttpServletRequest request)
    {
        if (validation.hasErrors()) // Vérifie les erreurs de validation.
        {
            List<String> errors = validation.getAllErrors().stream()//
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)//
                    .collect(Collectors.toList());

            return ResponseEntity.badRequest().body(errors);
        }

        // Force un identifiant à null dans le cas où le client envoie l'id d'un article qui existe déjà
        // pour tenter de le modifier
        article.setId(null);
        article = mRepository.save(article);

        // Construction de la réponse
        URI location = ServletUriComponentsBuilder.fromRequest(request)//
                .path("/{id}")//
                .buildAndExpand(article.getId())//
                .toUri();
        return ResponseEntity.created(location).body(article);
    }

    @PutMapping(path = "/articles/{id}")
    @ApiResponse(responseCode = "200", description = "La ressource a été mise à jour avec succès.")
    @ApiResponse(responseCode = "404", description = "La ressource à mettre à jour n'a pas été trouvée.")
    public ResponseEntity<?> updateArticle(
            @PathVariable Long id,
            @RequestBody @Valid Article article,
            BindingResult validation,
            HttpServletRequest request)
    {
        if (validation.hasErrors())
        {
            List<String> errors = validation.getAllErrors().stream()//
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)//
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        Article updated = mRepository.findById(id).map(a -> {
            a.setTitle(article.getTitle());
            a.setDescription(article.getDescription());
            a.setCreatedAt(article.getCreatedAt());
            a.setCreatedBy(article.getCreatedBy());
            return mRepository.save(a);
        }).orElseGet(() -> null);

        if (updated == null)
        {
            return ResponseEntity.notFound()//
                    .location(ServletUriComponentsBuilder.fromRequest(request).build().toUri())//
                    .build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(path ="/articles/{id}")
    @ApiResponse(responseCode = "200", description = "La ressource n'existe pas, requête ignorée.")
    @ApiResponse(responseCode = "204", description = "La ressource a été supprimée avec succès.")
    public ResponseEntity deleteArticle(@PathVariable Long id)
    {
        if (mRepository.existsById(id))
        {
            mRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }
}