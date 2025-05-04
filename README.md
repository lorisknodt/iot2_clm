
# Projet Android - CLM

Ce projet est une application mobile Android développée en Kotlin utilisant l'architecture MVVM. L'application permet d'afficher une liste de produits, de gérer un panier d'achat, de filtrer les produits par catégorie et de gérer le détail d'un produit. Elle permet aussi de faire une commande sur base des produits, d'entrer une adresse de livraison, et de valider la commande à l'aide d'un payement 3DSecure.
Les commandes d'un utilisateur peuvent être affichés dans une page respective et pour chaque commande, on peut y visualiser le détail.


## 📋 Fonctionnalités mises en place

### Produits
Affichage d'une liste de produits sous forme de cartes avec :
- Une image du produit.
- Le nom du produit.
- Une description claire.
- Le prix du produit.
- Un bouton `+` pour ajouter le produit au panier.
- La possibilité de filtrer les produits par catégorie (ex. : "Laptop", "Smartphone", etc.), ou de tout afficher en sélectionnant la catégorie "Tout".

### Panier
Affichage des produits ajoutés au panier avec :
- Le nom du produit.
- La description.
- Le prix.
- Deux boutons : `+` et `-` pour ajuster la quantité.
- Un bouton pour retirer un produit du panier.
- Gestion du total des prix du panier (en cours de développement).

### Gestion des catégories
- Un filtre de produits basé sur les catégories.
- Les catégories sont récupérées depuis l'API et affichées horizontalement sous forme de boutons.
- Un clic sur une catégorie charge les produits associés à cette catégorie.
- La catégorie "Tout" permet d'afficher tous les produits sans filtre.

### Détail d'un produit
- Chaque produit peut être sélectionné pour afficher un détail complet.
- Affichage du produit avec toutes ses informations, telles que la description complète, les spécifications, etc.

### Finalisation d'une commande
- Ajout de produits dans le panier pour la finalisation de la commande.
- Enregistrement des informations nécessaires à la commande (produits, quantité, etc.).

### Commandes
- Numéro de la commande
- La date de creation de la commande
- Son statut de livraison
- L'addresse de livraison
- Le prix total de la commande

### Détail d'une commande
- Idem que la page commande
- Les produits de la commande

### Rafraichissement dynamique 
- Pour la page Produits
- Pour la page Commandes

### Paiement de la commande
- Redirection sur une page web pour insérer les informations bancaires
- Attente de la réponse de la page web pour l'affichage dédié

### Gestion du profil utilisateur
Cette page permet d'accéder à différentes actions :
- **Historique des commandes** : Redirige l'utilisateur vers la page contenant toutes ses commandes passées.
- **Paramètres du compte** : Redirige l'utilisateur vers une page de développement pour les futures fonctionnalités de modification du profil.
- **Déconnexion** : Vide le cache local (où est stocké le token d'authentification), met à jour le `currentUser` à `null` et redirige vers l'écran de connexion.


## 🧩 Architecture du projet

Le projet suit l'architecture MVVM (Model - View - ViewModel) pour une meilleure organisation du code :

- **Model** : Gère les données et la logique métier (ex : modèle `Product`).
- **View** : Affiche les données à l'utilisateur via les Fragments et les RecyclerView.
- **ViewModel** : Gère les interactions entre le Model et la View via des LiveData.

## Arborescence du projet
```bash
app/
├── java/com/example/clm_iot2/
│   ├── adapter/                  # Contient les classes d’adaptateurs pour lier les objets à l'interface graphique (ex : ProductAdapter, OrderAdapter, ...)
│   ├── model/                    # Définit les classes de données (ex : Product, Order, Category, etc.)
│   ├── network/                  # Gère la communication avec l’API (ex : ApiService avec Retrofit)
│   ├── repository/               # Fournit une couche d’abstraction pour accéder aux données (API, cache, etc.)
│   ├── ui/                       # Contient les Fragments organisés par fonctionnalité (ex : produits, panier, commandes)
│   │   ├── products/             # UI liée à l’accueil ou à la liste de produits
│   │   ├── singleproduct/        # UI liée à l'affichage du détail d'un produit
│   │   ├── category/             # UI liée à l'affichage des catégories
│   │   ├── cart/                 # UI liée au panier
│   │   ├── orders/               # UI liée aux commandes (liste, détails, etc.)
│   │   ├── singleorder/          # UI liée à l'affichage détaillé d'une commande
│   │   └── orderconfirmation/    # UI liée à la page de confirmation d'une commande (Ajout adresse, payement, ...)
│   └── MainActivity.kt           # Activité principale qui héberge les fragments
│
├── res/
│   ├── layout/                   # Fichiers XML décrivant l’interface utilisateur (fragments, items, etc.)
│   ├── drawable/                 # Icônes, badges de statut, images d’illustration, etc.
│   ├── values/                   # Ressources partagées : couleurs, chaînes, thèmes, dimensions, etc.
│
├── AndroidManifest.xml          # Déclaration des composants de l'application (activités, permissions, etc.)
```

## 🛠️ Technologies et outils

- **Kotlin** : Langage principal du projet.
- **RecyclerView** : Utilisé pour afficher les listes de produits et du panier.
- **LiveData** : Gère les changements dynamiques des données.
- **ViewModel** : Centralise la logique d'affaires pour chaque Fragment.
- **Coroutines** : Pour gérer les appels asynchrones et les appels réseau.
- **Retrofit** : Pour gérer les appels API afin de récupérer les produits et les catégories.

## 🚀 Instructions d'installation

1. Cloner le dépôt :

   ```bash
   git clone https://github.com/lorisknodt/iot2_clm.git
   ```
2. Ouvrir le projet dans IntelliJ IDEA ou Android Studio.
3. Compiler et exécuter le projet sur un émulateur ou un appareil physique.

## 📚 Prochaines étapes

- Finaliser le fragment `PaymentFailure` en configurant correctement le bouton "Réessayer".
- Implémenter la modification du profil utilisateur (adresse de facturation, numéro de téléphone, etc.).
- Ajouter un système de service après-vente (SAV) permettant aux utilisateurs de laisser des commentaires et des notes sur les produits.
- Supporter d'autres méthodes d'authentification (SMS, Masi-id).
- Améliorer la gestion du panier, notamment lorsqu'on ferme et rouvre l'application.
- Ajouter des services Android pour mieux gérer certains appels API en arrière-plan (ex. actualisation silencieuse des commandes).


