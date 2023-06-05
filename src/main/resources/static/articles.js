/**
 * @typedef {Object} Article - Représente un article.
 * @property {number} Article.id - L'identifiant de l'article.
 * @property {string} Article.title - Le titre de l'article.
 * @property {string} Article.description - La description de l'article.
 * @property {string} Article.createdAt - La date de création de l'article.
 * @property {string} Article.createdBy - L'auteur de l'article.
 */

const BASEURL_WEBSERVICE_ARTICLES = "/api/articles";
const BASEURL_FRAGMENT_ARTICLES = "/fragments/articles";
const BASEURL_WEBSERVICE_COMMENTAIRES = "/articles/{articleId}/commentaires";

const BTN_CREATE = "btn-create-polling";
const BTN_SUBSCRIBE = "btn-subscribe";
const BTN_CHANGE_PASSWORD = "btn-change-password";
const BTN_REFRESH = "bnt-refresh-pollings";
const DIV_ARTICLES = "articles";
const FORM_CREATE_ARTICLE = "form-creation-article";
const FORM_CREATE_COMMENTAIRE = "form-create-commentaire"; // Ajout de l'ID du formulaire de création de commentaire

document.onreadystatechange = () => {
    if (document.readyState === "complete") {
        let btnRefreshPollings = document.getElementById(BTN_REFRESH);
        if (btnRefreshPollings) {
            btnRefreshPollings.addEventListener("click", refreshAllArticles);
        }
        let formCreatePolling = document.getElementById(FORM_CREATE_ARTICLE);
        if (formCreatePolling) {
            formCreatePolling.addEventListener("submit", createNewPolling);
        }
        let btnSubscribe = document.getElementById(BTN_SUBSCRIBE);
        if (btnSubscribe) {
            btnSubscribe.addEventListener("click", subscribe);
        }
        let btnChangePassword = document.getElementById(BTN_CHANGE_PASSWORD);
        if (btnChangePassword) {
            btnChangePassword.addEventListener("click", changePassword);
        }
        let formCreateCommentaire = document.getElementById(FORM_CREATE_COMMENTAIRE); // Ajout de l'écouteur pour le formulaire de création de commentaire
        if (formCreateCommentaire) {
            formCreateCommentaire.addEventListener("submit", createNewComment);
        }

        let deleteButtons = document.querySelectorAll(".btn-danger");
        deleteButtons.forEach(function (button) {
            button.addEventListener("click", function () {
                let articleId = this.getAttribute("data-article-id");
                confirmDeleteArticle(articleId);
            });
        });
    }

    /**
     * Redirige vers la page d'édition de l'article.
     * @param {MouseEvent} event - L'événement de clic.
     */
    function editArticle(event) {
        event.preventDefault();
        let articleId = event.target.getAttribute("data-article-id");
        redirectToEditArticle(event); // Appeler la fonction redirectToEditArticle
        window.location.href = "/admin/articles/" + articleId + "/edit";
        updateArticle(event); // Appeler la fonction updateArticle après la redirection
    }

    /**
     * Met à jour l'article en envoyant les données au serveur.
     * @param {MouseEvent} event - L'événement de clic.
     */
    function updateArticle(event) {
        event.preventDefault();
        let form = event.target;
        let articleId = form.getAttribute("data-article-id");
        let formData = new FormData(form);

        fetch("/admin/articles/" + articleId + "/edit", {
            method: "POST",
            body: formData,
        })
            .then((response) => {
                if (response.ok) {
                    console.log("Article mis à jour avec succès !");
                    window.location.href = "/fragments/articles/" + articleId;
                } else {
                    console.error("Erreur lors de la mise à jour de l'article !");
                    response.json().then((err) => console.error(err));
                }
            })
            .catch((error) => {
                console.error("Erreur lors de la requête :", error);
            });
    }

    /**
     * Confirme la suppression de l'article.
     * @param {MouseEvent} event - L'événement de clic.
     */
    function confirmDeleteArticle(articleId) {
        if (confirm("Êtes-vous sûr de vouloir supprimer cet article ?")) {
            var deleteUrl = "/admin/articles/" + articleId + "/delete";

            fetch(deleteUrl, {
                method: "DELETE",
            })
                .then(function (response) {
                    if (response.ok) {
                        console.log("Article supprimé avec succès !");
                        // Faire d'autres actions après la suppression réussie
                    } else {
                        console.error("Erreur lors de la suppression de l'article !");
                    }
                })
                .catch(function (error) {
                    console.error("Erreur lors de la requête :", error);
                });
        }
    }


    /**
     * Redirige vers la page d'inscription.
     * @param {MouseEvent} event - L'événement de clic.
     */
    function subscribe(event) {
        window.location.href = "/inscription";
    }

    /**
     * Recharge la liste des articles.
     * @param {MouseEvent} event - L'événement de clic.
     */
    function refreshAllArticles(event) {
        let pageCourante = document.querySelector(".page-courante").textContent;
        pageCourante -= 1;

        fetch(BASEURL_FRAGMENT_ARTICLES + "?page=" + pageCourante)
            .then((result) => result.text())
            .then((text) => {
                let div = document.getElementById(DIV_ARTICLES);
                let documentFragment = document
                    .createRange()
                    .createContextualFragment(text);
                div.innerHTML = documentFragment.firstChild.innerHTML;
            });
    }

    /**
     * Ajoute un nouvel article dans la liste sans devoir tout recharger.
     * @param {Article} article - Le nouvel article à ajouter.
     */
    function updateListWith(article) {
        console.log("Mise à jour de la page avec le nouvel article :");
        console.log(article);

        fetch(BASEURL_FRAGMENT_ARTICLES + "/" + article.id)
            .then((response) => response.text())
            .then((text) => {
                const div = document.getElementById(DIV_ARTICLES);
                let documentFragment = document
                    .createRange()
                    .createContextualFragment(text);
                div.prepend(documentFragment.firstChild);
            });
    }

    /**
     * Envoie les données de l'article vers l'URL "/api/articles" avec la méthode POST.
     * @param {Event} event - L'événement de clic.
     */
    function createNewPolling(event) {
        event.preventDefault();

        let form = document.getElementById(FORM_CREATE_ARTICLE);
        let formData = new FormData(form);

        console.debug("Envoi des données au serveur : \n" + formData);

        fetch(BASEURL_WEBSERVICE_ARTICLES, {
            method: "POST",
            body: formData,
        })
            .then((response) => {
                if (response.ok) {
                    console.log("Article créé avec succès !");
                    response.json().then((article) => updateListWith(article));
                    window.location.href = "/"; // Redirige vers la page d'accueil à jour.
                } else {
                    console.error("Erreur lors de la création de l'article !");
                    response.json().then((err) => console.error(err));
                }
            })
            .catch((error) => {
                console.error("Erreur lors de la requête :", error);
            });
    }

    /**
     * Envoie les données du commentaire vers l'URL "/api/commentaires" avec la méthode POST.
     * @param {Event} event - L'événement de clic.
     */
    function createNewComment(event) {
        event.preventDefault();

        let form = event.target;
        let articleId = form.querySelector('input[name="id_article"]').value;
        let formData = new FormData(form);

        formData.set("id_article", articleId);

        fetch(BASEURL_WEBSERVICE_COMMENTAIRES.replace("{articleId}", articleId), {
            method: "POST",
            body: formData,
        })
            .then((response) => {
                if (response.ok) {
                    console.log("Commentaire ajouté avec succès !");
                    // Redirection vers la page des commentaires de l'article
                    window.location.href = "/fragments/articles/" + articleId;
                } else {
                    console.error("Erreur lors de l'ajout du commentaire !");
                    response.json().then((err) => console.error(err));
                }
            })
            .catch((error) => {
                console.error("Erreur lors de la requête :", error);
            });
    }
};
