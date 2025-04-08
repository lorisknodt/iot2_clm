Les adaptateurs (adapters) jouent un rôle essentiel dans la liaison entre les données (fournies par les modèles ou récupérées via une API) et les composants de l'interface utilisateur, notamment les listes et les RecyclerView.

- **Fonction principale :**  
  *Adapter les données au format attendu par l'interface utilisateur*.  
  Par exemple, pour une RecyclerView, un adapter va créer les ViewHolders pour chaque élément et associer les données à ces ViewHolders.

- **Responsabilités :**
  - Créer les vues individuelles (ViewHolder) pour chaque élément de la liste.
  - Lier les données à chaque vue (remplissage des champs de texte, images, etc.).
  - Gérer les interactions avec les éléments de la liste (clics, modifications de quantité, suppression, etc.).
