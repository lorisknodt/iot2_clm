Les layouts définissent la structure de l'interface utilisateur de l'application. Ils sont écrits en XML et servent à organiser les vues (TextView, Button, RecyclerView, etc.).

- **Fonction principale :**  
  *Décrire la disposition et l'apparence de l'interface* utilisateur en spécifiant les composants qui la composent et leur disposition.

- **Types de Layouts couramment utilisés :**
  - **ConstraintLayout** : Offre une grande flexibilité pour positionner les vues à l'aide de contraintes.
  - **LinearLayout** : Organise les vues de manière linéaire (verticale ou horizontale).
  - **RelativeLayout** / **FrameLayout** : Permet de superposer des vues ou de les positionner relativement les unes par rapport aux autres.
  - **RecyclerView** : Spécifiquement utilisé pour afficher des listes (souvent en combinaison avec des adaptateurs).

- **Avantages :**
  - Permet de séparer la logique de présentation (définie dans les layouts XML) de la logique métier (définie dans les ViewModels et autres composants).
  - Facilite la maintenance et la modification de l'interface utilisateur sans toucher au code Java/Kotlin.
