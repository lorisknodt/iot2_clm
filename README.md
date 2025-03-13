Projet Android - CLM

Ce projet est une application mobile Android développée en Kotlin utilisant l'architecture MVVM. L'application permet d'afficher une liste de produits et de gérer un panier d'achat.

📋 Fonctionnalités mises en place

Produits

Affichage d'une liste de produits sous forme de cartes avec :

Une image du produit.

Le nom du produit.

Une description succincte.

Le prix du produit.

Un bouton + pour ajouter le produit au panier.

Panier

Affichage des produits ajoutés au panier avec :

Le nom du produit.

La description.

Le prix.

Deux boutons : + et - pour ajuster la quantité.

Un bouton pour retirer un produit du panier.

Gestion du total des prix du panier (en cours de développement).

🧩 Architecture du projet

Le projet suit l'architecture MVVM (Model - View - ViewModel) pour une meilleure organisation du code :

Model : Gère les données et la logique métier (ex : modèle Product).

View : Affiche les données à l'utilisateur via les Fragments et les RecyclerView.

ViewModel : Gère les interactions entre le Model et la View via des LiveData.

Organisation des fichiers

├── model
│   ├── Product.kt
│
├── ui
│   ├── products
│   │   ├── ProductsFragment.kt
│   │   ├── ProductsAdapter.kt
│   │
│   ├── cart
│   │   ├── CartFragment.kt
│   │   ├── CartAdapter.kt
│
├── viewmodel
│   ├── ProductsViewModel.kt
│   ├── CartViewModel.kt

🛠️ Technologies et outils

Kotlin : Langage principal du projet.

RecyclerView : Utilisé pour afficher les listes de produits et du panier.

LiveData : Gère les changements dynamiques des données.

ViewModel : Centralise la logique d'affaires pour chaque Fragment.

🚀 Instructions d'installation

Cloner le dépôt :

git clone <URL_DU_DEPOT>

Ouvrir le projet dans IntelliJ IDEA ou Android Studio.

Compiler et exécuter le projet sur un émulateur ou un appareil physique.

📚 Prochaines étapes

Mise en place d'une gestion plus avancée du panier (prix total, validation de commande).

Ajout d'une page de détail pour chaque produit.

Amélioration du design et de l'interface utilisateur.
