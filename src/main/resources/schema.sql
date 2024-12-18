BEGIN TRANSACTION;

CREATE TABLE IF NOT EXISTS "mpa_rating" (
  "mpa_rating_id" int PRIMARY KEY,
  "name" varchar(5) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS "genre" (
  "genre_id" int PRIMARY KEY,
  "name" varchar(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS "film" (
  "film_id" BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "name" varchar(255) NOT NULL,
  "description" varchar(200) NOT NULL,
  "release_date" date NOT NULL,
  "duration" int NOT NULL,
  "mpa_rating_id" int REFERENCES "mpa_rating" ("mpa_rating_id") ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS "film_genre" (
  "film_id" bigint REFERENCES "film" ("film_id") ON DELETE CASCADE,
  "genre_id" int REFERENCES "genre" ("genre_id") ON DELETE RESTRICT,
  PRIMARY KEY ("film_id", "genre_id")
);

CREATE TABLE IF NOT EXISTS "user" (
  "user_id" BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "email" varchar(255) NOT NULL,
  "login" varchar(255) NOT NULL,
  "name" varchar(255) NOT NULL,
  "birthday" date NOT NULL
);

CREATE TABLE IF NOT EXISTS "user_user_friend" (
  "user_id" bigint REFERENCES "user" ("user_id") ON DELETE CASCADE,
  "friend_id" bigint REFERENCES "user" ("user_id") ON DELETE CASCADE,
  PRIMARY KEY ("user_id", "friend_id")
);

CREATE TABLE IF NOT EXISTS "user_film_like" (
  "user_id" bigint REFERENCES "user" ("user_id") ON DELETE CASCADE,
  "film_id" bigint REFERENCES "film" ("film_id") ON DELETE CASCADE,
  PRIMARY KEY ("user_id", "film_id")
);

CREATE INDEX IF NOT EXISTS "user_user_friend_reverse" ON "user_user_friend" ("friend_id", "user_id");

CREATE INDEX IF NOT EXISTS "user_film_like_reverse" ON "user_film_like" ("film_id", "user_id");

COMMIT;