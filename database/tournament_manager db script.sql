CREATE DATABASE IF NOT EXISTS tournament_manager DEFAULT CHARSET = utf8mb4;
USE tournament_manager;

/* Uncomment to reset the database */ 
/*
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS tournaments;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS game_dates;
DROP TABLE IF EXISTS tournament_game_dates;
DROP TABLE IF EXISTS matches;
DROP TABLE IF EXISTS team_matches;
SET FOREIGN_KEY_CHECKS=1;
*/


CREATE TABLE IF NOT EXISTS tournaments(
	id INT UNIQUE AUTO_INCREMENT,
    tournament_name VARCHAR(255) NOT NULL,
    sport VARCHAR(100) NOT NULL,
    tournament_mode VARCHAR(50) NOT NULL,
    sign_up_deadline DATETIME NOT NULL,
    PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS teams(
	id INT UNIQUE AUTO_INCREMENT,
    team_name VARCHAR(100) NOT NULL UNIQUE,
    still_in_tournament TINYINT DEFAULT 1,
    points INT DEFAULT 0,
    goals_made INT DEFAULT 0,
    goals_let_through INT DEFAULT 0,
    fk_tournament_id INT,
    PRIMARY KEY(id),
    FOREIGN KEY(fk_tournament_id) REFERENCES tournaments(id)
)ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS players(
	id INT UNIQUE AUTO_INCREMENT,
    player_name VARCHAR(255),
    fk_team_id INT,
    PRIMARY KEY(id),
    FOREIGN KEY(fk_team_id) REFERENCES teams(id)
)ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS game_dates(
	id INT UNIQUE AUTO_INCREMENT,
    game_date Date UNIQUE NOT NULL,
    PRIMARY KEY(id)
)ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS tournament_game_dates(
	id INT UNIQUE AUTO_INCREMENT,
    fk_tournament_id INT NOT NULL,
    fk_game_date_id INT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(fk_tournament_id) REFERENCES tournaments(id),
    FOREIGN KEY(fk_game_date_id) REFERENCES game_dates(id)
)ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS matches(
	id INT UNIQUE AUTO_INCREMENT,
    fk_tournament_game_dates_id INT,
    PRIMARY KEY(id),
    FOREIGN KEY(fk_tournament_game_dates_id) REFERENCES tournament_game_dates(id)
)ENGINE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS team_matches(
	id INT UNIQUE AUTO_INCREMENT,
    fk_match_id INT,
    fk_team_id INT,
    goals INT,
    PRIMARY KEY(id),
    FOREIGN KEY(fk_match_id) REFERENCES matches(id),
    FOREIGN KEY(fk_team_id) REFERENCES teams(id)
)ENGINE=InnoDB AUTO_INCREMENT=1;


   
   
   
   