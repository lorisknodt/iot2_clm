# Dossier Network

Le dossier **network** centralise la configuration et la gestion de la communication avec l'API backend. Il contient principalement deux fichiers clés :

## 1. APIService

**Rôle :**  
L'interface `APIService` définit tous les endpoints de l'API ainsi que les méthodes HTTP à utiliser pour communiquer avec le serveur (GET, POST, etc.).  
  
**Exemple d'utilisation :**  
- Définir des fonctions suspendues pour récupérer des données (par exemple, `suspend fun getProducts(): List<Product>`)
- Définir des fonctions pour envoyer des données, comme créer une commande (`@POST("v1/order")`)

Ce fichier sert de contrat entre l'application et l'API.

## 2. RetrofitInstance

**Rôle :**  
`RetrofitInstance` est un objet singleton qui configure et crée l'instance Retrofit utilisée pour faire les appels réseau.  
  
**Principales tâches :**
- **Base URL** : Spécifie l'URL de base de l’API.
- **Converter Factory** : Utilise `GsonConverterFactory` (ou un autre convertisseur) pour sérialiser/désérialiser les objets JSON.
- **Configuration d’un client HTTP** 
