package fr.simplon.blog_wow.api;

import fr.simplon.blog_wow.dao.ArticleRepository;
import fr.simplon.blog_wow.dao.CommentaireRepository;
import fr.simplon.blog_wow.entity.Article;
import fr.simplon.blog_wow.entity.Commentaire;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur des commentaires.
 */
@Controller
public class CommentaireController {

    private ArticleRepository mRepository;
    private CommentaireRepository mCommentaireRepository;

    @Autowired
    public CommentaireController(ArticleRepository pRepository, CommentaireRepository pCommentaireRepository) {
        mRepository = pRepository;
        mCommentaireRepository = pCommentaireRepository;
    }

    /**
     * Traite la soumission du formulaire de création d'un commentaire.
     *
     * @param articleId     L'identifiant de l'article auquel le commentaire sera associé.
     * @param commentaire   Le commentaire à créer.
     * @param bindingResult Le résultat de la validation du formulaire.
     * @return Redirige vers la page des commentaires de l'article.
     */
    @PostMapping("/articles/{articleId}/commentaires")
    public String createCommentaire(
            @PathVariable("articleId") Long articleId,
            @Valid @ModelAttribute("commentaire") Commentaire commentaire,
            BindingResult bindingResult,
            Principal principal) {
        if (bindingResult.hasErrors()) {
            return "create-commentaire";
        }

        Optional<Article> article = mRepository.findById(articleId);
        System.out.println(articleId);
        if (article.isPresent()) {
            commentaire.setArticle(article.get());
            commentaire.setCommentAt(LocalDateTime.now());

            // Récupérer le nom d'utilisateur de l'objet Principal
            String username = principal.getName();
            commentaire.setUser(username);
            // Enregistrez le commentaire en utilisant mCommentaireRepository
            mCommentaireRepository.save(commentaire);

            return "redirect:/articles/" + articleId;
        } else {
            throw new RecordNotFoundException(articleId);
        }
    }


    /**
     * Affiche la page des commentaires d'un article.
     *
     * @param articleId L'identifiant de l'article.
     * @param model     Le modèle Thymeleaf.
     * @return La page de listing des commentaires d'un article.
     */
    @GetMapping(path = "/commentaires/{articleId}")
    public String commentairesByArticle(@PathVariable Long articleId, Model model) {
        Optional<Article> article = mRepository.findById(articleId);
        if (article.isPresent()) {
            model.addAttribute("article", article.get());
            model.addAttribute("commentaires", article.get().getCommentaires());
        } else {
            throw new RecordNotFoundException(articleId);
        }
        return "commentaires";
    }

    /**
     * Affiche le formulaire de modification d'un commentaire.
     *
     * @param commentaireId L'identifiant du commentaire à modifier.
     * @param model         Le modèle Thymeleaf.
     * @return La page de modification d'un commentaire.
     */
    @GetMapping("/commentaires/{commentaireId}/edit")
    public String showEditCommentaireForm(@PathVariable Long commentaireId, Model model) {
        Optional<Commentaire> commentaire = mCommentaireRepository.findById(commentaireId);
        if (commentaire.isPresent()) {
            model.addAttribute("newCommentaire", new Commentaire());
            model.addAttribute("commentaire", commentaire.get());
            return "edit_commentaire";
        } else {
            throw new RecordNotFoundException(commentaireId);
        }

    }

    /**
     * Traite la soumission du formulaire de modification d'un commentaire.
     *
     * @param commentaireId      L'identifiant du commentaire à modifier.
     * @param updatedCommentaire Le commentaire modifié.
     * @return Redirige vers la page des commentaires de l'article.
     */
    @PostMapping("/commentaires/{commentaireId}/edit")
    public String updateCommentaire(@PathVariable Long commentaireId,
                                    @ModelAttribute("commentaire") Commentaire updatedCommentaire) {
        Optional<Commentaire> commentaire = mCommentaireRepository.findById(commentaireId);
        if (commentaire.isPresent()) {
            Commentaire existingCommentaire = commentaire.get();

            // Récupérer l'utilisateur connecté
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            // Vérifier si l'utilisateur connecté est l'auteur du commentaire
            if (existingCommentaire.getUser().equals(currentUsername)) {
                existingCommentaire.setDescription(updatedCommentaire.getDescription());
                mCommentaireRepository.save(existingCommentaire);
                return "redirect:/fragments/articles/" + existingCommentaire.getArticle().getId();
            } else {
                throw new AccessDeniedException("Vous n'êtes pas autorisé à modifier ce commentaire.");
            }
        } else {
            throw new RecordNotFoundException(commentaireId);
        }
    }



    @DeleteMapping("/commentaires/{commentaireId}/delete")
    @ApiResponse(responseCode = "200", description = "La ressource n'existe pas, requête ignorée.")
    @ApiResponse(responseCode = "204", description = "La ressource a été supprimée avec succès.")
    public ResponseEntity deleteCommentaire(@PathVariable Long commentaireId) {
        Optional<Commentaire> commentaire = mCommentaireRepository.findById(commentaireId);
        if (commentaire.isPresent()) {
            Commentaire existingCommentaire = commentaire.get();

            // Récupérer l'utilisateur connecté
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();

            // Vérifier si l'utilisateur connecté est l'auteur du commentaire
            if (existingCommentaire.getUser().equals(currentUsername)) {
                mCommentaireRepository.deleteById(commentaireId);
                return ResponseEntity.noContent().build();
            } else {
                throw new AccessDeniedException("Vous n'êtes pas autorisé à supprimer ce commentaire.");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Signale un commentaire.
     *
     * @param commentaireId L'identifiant du commentaire à signaler.
     * @return Redirige vers la page des commentaires de l'article.
     */
    @PostMapping("/commentaires/{commentaireId}/flag")
    public String flagCommentaire(@PathVariable Long commentaireId) {
        Optional<Commentaire> commentaire = mCommentaireRepository.findById(commentaireId);
        if (commentaire.isPresent()) {
            Commentaire commentaireSignale = commentaire.get();
            commentaireSignale.setFlagged(true);
            // Enregistrez la modification en utilisant mCommentaireRepository

            return "redirect:/commentaires/" + commentaireSignale.getArticle().getId();
        } else {
            throw new RecordNotFoundException(commentaireId);
        }
    }


}