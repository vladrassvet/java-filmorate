--DROP TABLE friends,likes,users, genre, film_genre, MPA, films;

create table if not exists MPA(
    MPA_id integer primary key auto_increment,
    MPA_name varchar(10),
    constraint MPA_PK primary key (MPA_id)
);

create table if not exists films(
    film_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY key,
    name varchar(50) not null,
    description varchar(200) not null,
    release_data date NOT NULL,
    duration integer,
    mpa integer REFERENCES MPA(MPA_id)
);

create table if not exists genre(
    genre_id integer primary key auto_increment,
    name varchar(255) not null
);

create table if not exists film_genre(
    film_id integer REFERENCES films (film_id),
	genre_id integer REFERENCES genre (genre_id),
	PRIMARY KEY (film_id,genre_id)
);

create table if not exists users(
    user_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY key,
    email varchar(20) NOT NULL,
    login varchar(20) NOT NULL,
    name varchar(20),
    birthday date NOT NULL
);

create table if not exists likes(
    film_id integer REFERENCES films (film_id),
	user_id integer REFERENCES users (user_id),
	PRIMARY KEY (user_id,film_id)
);

create table if not exists friends(
    user_id integer REFERENCES users (user_id),
	friend_id integer REFERENCES users (user_id),
	status varchar(12),
	PRIMARY KEY (user_id, friend_id)
);