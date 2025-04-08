Les modèles (models) sont des classes de données qui représentent les entités manipulées par ton application.

- **Fonction principale :**  
  *Représenter les données sous forme d’objets Kotlin* qui seront utilisées par d’autres parties du code (ViewModels, adaptateurs, repositories).

- **Exemples de modèles :**
  - `Product` : Contient les informations d’un produit (id, nom, description, prix, quantité, etc.).
  - `Category` : Représente une catégorie de produit (id, nom, description).
  - `OrderRequest`, `OrderItem`, `NewAddress` : Utilisés pour la gestion de la commande et la communication avec l'API.

- **Avantages :**
  - Facilite la sérialisation/désérialisation des données JSON avec des bibliothèques comme Gson.
  - Permet de centraliser la logique métier liée aux entités.
