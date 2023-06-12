# Blog World Of Warcraft

![Accueil](https://zupimages.net/up/23/24/6piv.png)

## Description du projet 

Cette application a été réalisée dans le cadre d'un projet de fin de formation. 
Elle permet, sous forme de blog, de présenter les stratégies de combat du nouveau raid, 
"Aberrus, le Creuset obscur" à travers des articles.

Les utilisateurs peuvent consulter une liste d'articles et y ajouter des commentaires.

Côté Back-end, le site utilise les technologies suivantes : Spring Boot MVC,
Thymeleaf, Validation, Spring Web, Spring Boot Security et Spring Data JPA.

## Langages et Framework utilisés

![Image](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

![Image](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)


![MySQL Badge](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=fff&style=for-the-badge)

![Image](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)

![Hibernate Badge](https://img.shields.io/badge/Hibernate-59666C?logo=hibernate&logoColor=fff&style=for-the-badge)

![Image](https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white)


![Thymeleaf Badge](https://img.shields.io/badge/Thymeleaf-005F0F?logo=thymeleaf&logoColor=fff&style=for-the-badge)

![Image](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)

![Spring Boot Badge](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=fff&style=for-the-badge)

--------

## Fonctionnalités

**1. Afficher les articles**

Notre site affiche sur la page d'accueil les articles par ordre de parution, du plus récent au plus ancien.
Lorsqu'un utilisateur clique sur "Lire la suite", il lui sera demandé de s'identifier et il sera redirigé vers la page de l'article en question, où il pourra ajouter un commentaire.

![Article](https://zupimages.net/up/23/24/tjva.png)

![AddCommentaire](https://zupimages.net/up/23/24/ewnp.png)

Il a la possibilité de modifier ou supprimer uniquement les commentaires qu'il a créés lui-même.

![UpdateCommentKo](https://zupimages.net/up/23/24/dqy7.png)

**2. Créer un article**

Notre site permet uniquement à un utilisateur ayant le rôle "Admin" de créer, modifier ou supprimer un article. 
Seul un "Admin" a accès, après s'être identifié, au bouton permettant d'accéder au formulaire de création d'un nouvel article. 

![NavbarAdmin](https://zupimages.net/up/23/24/h7eg.png)

L'accès sera refusé en cas de tentative par un utilisateur n'ayant pas les droits requis.

Une fois sur la page d'ajout, l'administrateur a la possibilité d'annuler sa saisie en utilisant le bouton 'Effacer' ou de valider l'ajout. 
Ensuite, il sera redirigé vers la page d'accueil où il pourra constater la mise à jour des articles.

![AddArticle](https://zupimages.net/up/23/24/6egv.png)

![AddArticleOk](https://zupimages.net/up/23/24/q6r8.png)

**3. Modifier / Supprimer un article**

Seul l'administrateur a les droits de modifier ou de supprimer un article. 
Lorsqu'il clique sur 'Modifier', un formulaire reprenant le contenu de l'article en question s'affiche et il peut effectuer les modifications avant d'appuyer sur 'Enregistrer

![UpdateArticle](https://zupimages.net/up/23/24/x2fo.png)

S'il clique sur 'Supprimer', un message de confirmation apparaîtra avant la suppression définitive de l'article.

![DeleteArticle](https://zupimages.net/up/23/24/kbyu.png)

------

## Prérequis

**1. Créer une base de données MySQL**

Dans ce projet, la base de données 'blog_wow' doit être créée avant de lancer l'application. 
Cependant, les tables 'article', 'authorities', 'commentaire' et 'users' n'ont pas besoin d'être créées au préalable, car elles seront générées automatiquement à partir du code.

**2. Cloner le repository**

Vous pouvez consulter la documentation correspondante en cliquant sur le lien suivant : 
https://docs.github.com/fr/repositories/creating-and-managing-repositories/cloning-a-repository. 
Elle contient toutes les informations nécessaires pour cloner le dépôt.

**3. Modification de application.properties**

Pour pouvoir vous connecter à votre base de données, 
vous devrez adapter votre configuration dans le fichier 'application.properties' situé dans resources/application.properties :

Exemple pour MySQL :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blog_wow
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Pour modifier le port du serveur (par défaut 8080), vous devez modifier le même fichier :
```properties
server.port=8081
```

### API

Par défaut l'API est documentée sur l'URL `http://localhost:8081/swagger-ui.html`.

![Swagger](https://zupimages.net/up/23/24/ksmo.png)

- Les services RESTFul (CRUD) pour les articles sont disponibles sur l'URL
  `/api/articles`.

### Tests unitaires

Un test unitaire a été réalisé sur la méthode articles() de notre classe ArticleController. 
Nous utilisons Mockito et JUnit pour ce test. 
Nous créons deux objets Article de test avec des valeurs fictives. 
Ces objets sont ensuite ajoutés à une liste fictive articles. 
Le comportement du mock mRepository est défini pour renvoyer la liste articles lorsque la méthode findAll() est appelée. 
Nous appelons la méthode articles() de notre ArticleController pour obtenir le résultat, puis nous vérifions la taille de la liste, l'ID, le titre, la description, etc. de chaque article.

Cela nous permet de vérifier si la méthode articles() renvoie bien la liste d'articles attendue en utilisant le mock mRepository
