# Dossier UI

Le dossier **UI** contient l'ensemble des fragments et leur logique associée (ViewModels) qui forment l'interface utilisateur de l'application. Chaque sous-dossier regroupe les fragments liés à une fonctionnalité précise.

## Structure du dossier UI

L'organisation actuelle se présente ainsi :

- **cart**  
- **category**  
- **product**  
- **orderConfirmation**  
- **orders**  
- **profile**  
- **singleproduct**

---

## 1. Cart

**Rôle :**  
Ce dossier gère tout ce qui est lié au panier d'achat.

- **Fragments** :  
  - `CartFragment` : Affiche les produits ajoutés au panier, permet d'ajuster les quantités et de supprimer un produit du panier.  
- **ViewModel** :  
  - Le `CartViewModel` centralise la gestion des articles du panier, la mise à jour des quantités et les calculs du total.

---

## 2. Category

**Rôle :**  
Ce dossier est dédié à la gestion et à l'affichage des catégories de produits.

- **Fragments** :  
  - Habituellement, ce dossier contient le fragment ou les composants UI (par exemple via un RecyclerView horizontal) permettant d’afficher la liste des catégories.  
- **ViewModel** :  
  - Le `CategoryViewModel` est responsable de la récupération des catégories depuis l'API et de la gestion des mises à jour de la liste.  
- **Utilisation** :  
  - Permet de filtrer les produits affichés dans le fragment "Produits". Une catégorie spéciale "Tout" permet de recharger tous les produits.

---

## 3. Product

**Rôle :**  
Ce dossier gère l'affichage de la liste des produits.

- **Fragments** :  
  - `ProductsFragment` : Affiche l'ensemble des produits sous forme de cartes.  
- **ViewModel** :  
  - Le `ProductViewModel` (ou, dans une refonte récente, la gestion des produits peut être déléguée au `CategoryViewModel` lorsque le filtrage par catégorie est utilisé) gère la récupération (soit de tous les produits, soit filtrés par catégorie) et expose ces données via des LiveData ou StateFlow pour actualiser l'interface utilisateur.

---

## 4. OrderConfirmation

**Rôle :**  
Ce dossier est consacré au processus de confirmation de la commande.

- **Fragments** :  
  - `OrderConfirmationFragment` : Permet à l'utilisateur de saisir ses informations d'adresse et de paiement, puis de finaliser sa commande.  
- **ViewModel / Repository** :  
  - Un `OrderRepository` est utilisé pour communiquer avec l'API (création de commande, envoi des détails de la commande, etc.).  
  - Le ViewModel associé (si présent) centralise la logique de validation et d'envoi de la commande à l'API.

---

## 5. Orders

**Rôle :**  
Ce dossier regroupe les écrans liés à la gestion des commandes passées.

- **Fragments** :  
  - Par exemple, `OrdersFragment` affiche une liste des commandes passées et leur statut.  
- **ViewModel** :  
  - Le ViewModel associé gère la récupération et la mise à jour des commandes depuis la base de données ou l'API.

---

## 6. Profile

**Rôle :**  
Ce dossier gère les informations liées au profil utilisateur.

- **Fragments** :  
  - `ProfileFragment` permet d'afficher et de modifier les informations personnelles de l'utilisateur (nom, email, etc.).  
- **ViewModel** :  
  - Le `ProfileViewModel` centralise la récupération et la mise à jour des données du profil utilisateur.

---

## 7. SingleProduct

**Rôle :**  
Ce dossier permet d'afficher le détail complet d’un produit sélectionné.

- **Fragments** :  
  - `SingleProductFragment` : Affiche toutes les informations détaillées d’un produit (images, description complète, spécifications, etc.).  
- **ViewModel** :  
  - Le ViewModel associé (par exemple, `SingleProductViewModel`) peut récupérer ou préparer les données nécessaires pour afficher en détail le produit sélectionné.  
- **Utilisation** :  
  - Permet à l'utilisateur de cliquer sur un produit dans la liste et d'être dirigé vers une page de détail.
