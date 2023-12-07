CREATE TABLE "_user" (
    "id" serial NOT NULL PRIMARY KEY,
    "email" varchar(64) NOT NULL unique,
    "username" varchar(64) NOT NULL unique,
    "password" varchar(128) NOT NULL,
    "role" varchar(64),
    "passwordResetCode" int,
    "passwordResetCodeCreatedAt" timestamp
);

CREATE TABLE "token"(

    "id" serial NOT NULL PRIMARY KEY,
    "expired" boolean,
    "revoked" boolean,
    "token" text,
    "token_type" text,
    "user_id" integer,
    CONSTRAINT "fk_user" FOREIGN KEY(user_id) REFERENCES "_user"(id)
);