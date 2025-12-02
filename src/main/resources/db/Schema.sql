-- Sam Custom Tables
create table Team(
    id_team int not null auto_increment,
    name_team varchar(50) not null,
    constraint team_pk primary key(id_team),
    constraint unqiue_team_name unique key(name_team)
);
create table Game(
    id_game int not null auto_increment,
    date date,
    time time,
    location varchar(100),
    completed boolean,
    voided boolean,
    constraint game_pk primary key(id_game)
);

create table Team_Plays_Game(
    id_team int not null,
    id_game int not null,
    points int,
    constraint tpg_pk primary key(id_team, id_game),
    constraint tpg_team_fk foreign key(id_team) references Team(id_team),
    constraint tpg_game_fk foreign key(id_game) references Game(id_Game)
);

CREATE TABLE players(
    id_player INT NOT NULL AUTO_INCREMENT,
    name_player VARCHAR(100) NOT NULL,
    grade ENUM('FRESHMAN', 'SOPHOMORE', 'JUNIOR', 'SENIOR') NOT NULL,
    draft_pick INT,
    is_captain BOOLEAN NOT NULL DEFAULT FALSE,
    id_team INT NOT NULL,

    CONSTRAINT players_pk PRIMARY KEY(id_player),
    CONSTRAINT players_team_fk FOREIGN KEY(id_team) REFERENCES Team(id_team)
);


-- Default Spring Security Schema
create table users(username varchar(50) not null primary key,password varchar(500) not null,enabled boolean not null);
create table authorities (username varchar(50) not null,authority varchar(50) not null,constraint fk_authorities_users foreign key(username) references users(username));
create unique index ix_auth_username on authorities (username,authority);