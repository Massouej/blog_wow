/**
 * @typedef Article
 * @param {number} Article.id
 * @param {string} Article.title
 * @param {string} Article.description
 * @param {string} Article.createdAt
 *@param {string} Article.createdBy
 */

const BASEURL_WEBSERVICE_ARTICLES = "/api/articles";
const BASEURL_FRAGMENT_ARTICLES = "/fragments/articles";
const BASEURL_WEBSERVICE_COMMENTAIRES = "/api/commentaires";

const BTN_CREATE = "btn-create-polling";
const BTN_SUBSCRIBE = "btn-subscribe";
const BTN_CHANGE_PASSWORD = "btn-change-password";
const BTN_REFRESH = "bnt-refresh-pollings";
const DIV_ARTICLES = "articles";
const FORM_CREATE_ARTICLE = "form-creation-article";

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
    }
}

/**
 * Redirige vers la page d'inscription.
 * @param {MouseEvent} event
 */
function subscribe(event) {
    window.location.href = "/inscription";
}

/** Recharge la liste des articles.
 * @param {MouseEvent} event
 */

function refreshAllArticles() {
    let pageCourante = document.querySelector(".page-courante").textContent;
    pageCourante -= 1;

    fetch( BASEURL_FRAGMENT_ARTICLES + '?page=' + pageCourante)
        .then(result => result.text())
        .then(text => {
            let div = document.getElementById(DIV_ARTICLES);
            let documentFragment = document.createRange().createContextualFragment(text);
            div.innerHTML = documentFragment.firstChild.innerHTML;
        });
}

/**
 * Ajoute un nouvel article dans la liste sans devoir tout recharger.
 * @param {Article} article
 */

function updateListWith(article ) {
    console.log("Mise à jour de la page avec le nouvel article :");
    console.log(article);

    fetch(BASEURL_FRAGMENT_ARTICLES + '/' + article.id)
        .then(response => response.text())
        .then(text => {
            const div = document.getElementById(DIV_ARTICLES);
            let documentFragment = document.createRange().createContextualFragment(text);
            div.prepend(documentFragment.firstChild);
        });
}

/**
 * Envoie les données de l'article vers l'URL "/api/articles" avec méthode POST.
 *
 * @param {Event} event l'évènement de click.
 */

function createNewPolling(event ) {

    // Pour éviter d'envoyer une requête POST par défaut via le navigateur
    // on désactive le comportement par défaut du bouton submit car on va
    // gérer nous-même la requête en mode 'fetch'.
    event.preventDefault();

    // On transforme les champs du formulaire au format JSON
    let form = document.getElementById(FORM_CREATE_ARTICLE);
    let formData = new FormData(form);
    let json = JSON.stringify(Object.fromEntries(formData.entries()));

    console.debug("Sending data to server : \n" + json);

    // Envoi des données au WebService
    fetch(BASEURL_WEBSERVICE_ARTICLES, {
        method: 'POST',
        body: json,
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => {
        if (response.ok) {
            // La requête s'est bien passée
            console.log("Article créé avec succès !");
            response.json().then(article => updateListWith(article));
        } else {
            // La requête a échoué.
            console.error("Erreur lors de la création de l'article !");
            response.json().then(err => console.error(err));
        }
    }).catch(error => {
        console.error("Erreur lors de la requête :", error);
    });

}