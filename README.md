# java-filmorate
Template repository for Filmorate project.
![BD](image/YandexPracticumBD.png)
## Примеры запросов
1. Получение названий фильмов в жанре комедий
```sql
    SELECT f.name 
    FROM film AS f 
    INNER JOIN genre AS gen ON f.id_genre = gen.id_genre 
    WHERE gen.genre = 'Comedy' 
    GROUP BY f.name; 
```
2. Получение 10 залайканных фильмов
```sql
SELECT f.name,
       COUNT(lf.id_user)
FROM film AS f
INNER JOIN likes_films AS lf ON f.id_films = lf.id_films
GROUP BY f.name
ORDER BY COUNT(lf.id_user) DESC
LIMIT 10;
```
3. Получение login друзей пользователя с email mail@mail.ru
```sql
SELECT fr.login
FROM user AS us
INNER JOIN user AS fr ON us.id_user = fr.id_friendship
WHERE us.email = 'mail@mail.ru'
      AND us.friendship_status = 'Сonfirmation'
      AND fr.friendship_status = 'Сonfirmation';
```