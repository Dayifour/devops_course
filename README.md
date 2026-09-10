# Gestion des étudiants

Application Spring Boot de gestion d'étudiants. Elle fournit une API REST, une page web simple et une documentation interactive Swagger/OpenAPI.

## Prérequis

- Java 17
- Aucun service de base de données externe : le projet utilise H2 en mémoire.

## Démarrer le projet

Depuis la racine du projet :

```bash
export JAVA_HOME="$HOME/.local/share/hermes-jdks/temurin-17"
export PATH="$JAVA_HOME/bin:$PATH"
./mvnw spring-boot:run
```

L'application écoute sur `http://localhost:8080`.

## Interfaces disponibles

| Interface | URL | Description |
| --- | --- | --- |
| Application web | http://localhost:8080/ | Ajout et consultation des étudiants |
| Swagger UI | http://localhost:8080/swagger-ui.html | Documentation interactive et tests de l'API |
| Spécification OpenAPI | http://localhost:8080/v3/api-docs | Document JSON OpenAPI |
| Console H2 | http://localhost:8080/h2-console | Console de la base locale en mémoire |

### Connexion à H2

Dans la console H2, utiliser les paramètres suivants :

- **JDBC URL** : `jdbc:h2:mem:studentsdb`
- **Utilisateur** : `sa`
- **Mot de passe** : laisser vide

> Les données sont conservées uniquement tant que l'application est lancée. Elles sont perdues au redémarrage.

## Modèle étudiant

| Champ | Type | Créé par |
| --- | --- | --- |
| `id` | nombre | Base de données |
| `nom` | texte | Client |
| `prenom` | texte | Client |
| `dateN` | date (`YYYY-MM-DD`) | Client |
| `createDate` | date et heure ISO-8601 | Serveur |
| `updateDate` | date et heure ISO-8601 | Serveur |

Pour un **POST**, n'envoyer que `nom`, `prenom` et `dateN`. Les champs `id`, `createDate` et `updateDate` sont générés automatiquement par l'application.

## API REST

Base URL : `http://localhost:8080/api/students`

### Lister les étudiants

```http
GET /api/students
```

Exemple avec `curl` :

```bash
curl http://localhost:8080/api/students
```

Réponse : `200 OK`

```json
[
  {
    "id": 1,
    "nom": "Dayif",
    "prenom": "Dayif",
    "dateN": "2026-09-10",
    "createDate": "2026-09-10T14:15:42.520",
    "updateDate": "2026-09-10T14:15:42.520"
  }
]
```

### Créer un étudiant

```http
POST /api/students
Content-Type: application/json
```

JSON à saisir dans Swagger, dans le navigateur ou avec `curl` :

```json
{
  "nom": "Dayif",
  "prenom": "Dayif",
  "dateN": "2026-09-10"
}
```

Exemple avec `curl` :

```bash
curl -X POST http://localhost:8080/api/students \
  -H 'Content-Type: application/json' \
  -d '{"nom":"Dayif","prenom":"Dayif","dateN":"2026-09-10"}'
```

Réponse : `201 Created`

```json
{
  "id": 1,
  "nom": "Dayif",
  "prenom": "Dayif",
  "dateN": "2026-09-10",
  "createDate": "2026-09-10T14:15:42.520",
  "updateDate": "2026-09-10T14:15:42.520"
}
```

### Modifier un étudiant

```http
PUT /api/students/{id}
Content-Type: application/json
```

Exemple pour l'étudiant `1` :

```json
{
  "nom": "Dayif",
  "prenom": "Nouveau prénom",
  "dateN": "2026-09-10"
}
```

Exemple avec `curl` :

```bash
curl -X PUT http://localhost:8080/api/students/1 \
  -H 'Content-Type: application/json' \
  -d '{"nom":"Dayif","prenom":"Nouveau prénom","dateN":"2026-09-10"}'
```

Réponses possibles :

- `200 OK` : l'étudiant a été modifié ; `updateDate` est actualisée.
- `404 Not Found` : aucun étudiant ne possède cet identifiant.

### Supprimer un étudiant

```http
DELETE /api/students/{id}
```

Exemple avec `curl` :

```bash
curl -X DELETE http://localhost:8080/api/students/1
```

Réponses possibles :

- `204 No Content` : l'étudiant a été supprimé.
- `404 Not Found` : aucun étudiant ne possède cet identifiant.

## Tester avec Swagger

1. Ouvrir http://localhost:8080/swagger-ui.html.
2. Déplier `students-controller` puis `POST /api/students`.
3. Cliquer sur **Try it out**.
4. Remplacer le corps de requête par le JSON minimal ci-dessous :

```json
{
  "nom": "Dayif",
  "prenom": "Dayif",
  "dateN": "2026-09-10"
}
```

5. Cliquer sur **Execute**.
6. La réponse doit être `201` et contenir les champs générés `id`, `createDate` et `updateDate`.

Ne pas copier-coller la réponse complète dans une nouvelle requête `POST` : cela créerait un autre étudiant. Pour modifier l'étudiant existant, utiliser `PUT /api/students/{id}`.

## Tests automatisés

Exécuter la suite complète :

```bash
export JAVA_HOME="$HOME/.local/share/hermes-jdks/temurin-17"
export PATH="$JAVA_HOME/bin:$PATH"
./mvnw clean test
```

Les tests couvrent notamment la création, la liste, la modification, la suppression et la génération de la spécification OpenAPI.
