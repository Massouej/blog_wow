package fr.simplon.blog_wow.api;

import fr.simplon.blog_wow.dao.ArticleRepository;
import fr.simplon.blog_wow.dao.CommentaireRepository;
import fr.simplon.blog_wow.entity.Article;
import fr.simplon.blog_wow.entity.Commentaire;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
            model.addAttribute("commentaire", commentaire.get());
        } else {
            throw new RecordNotFoundException(commentaireId);
        }
        return "edit-commentaire";
    }

    /**
     * Traite la soumission du formulaire de modification d'un commentaire.
     *
     * @param commentaireId      L'identifiant du commentaire à modifier.
     * @param updatedCommentaire Le commentaire modifié.
     * @param bindingResult      Le résultat de la validation du formulaire.
     * @return Redirige vers la page des commentaires de l'article.
     */
    @PostMapping("/commentaires/{commentaireId}/edit")
    public String editCommentaire(
            @PathVariable Long commentaireId,
            @Valid @ModelAttribute("commentaire") Commentaire updatedCommentaire,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-commentaire";
        }

        Optional<Commentaire> commentaire = mCommentaireRepository.findById(commentaireId);
        if (commentaire.isPresent()) {
            commentaire.get().setDescription(updatedCommentaire.getDescription());
            // Mettez à jour les autres propriétés du commentaire

            // Enregistrez les modifications en utilisant mCommentaireRepository

            return "redirect:/commentaires/" + commentaire.get().getArticle().getId();
        } else {
            throw new RecordNotFoundException(commentaireId);
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