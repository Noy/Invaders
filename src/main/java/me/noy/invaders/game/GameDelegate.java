package me.noy.invaders.game;

public interface GameDelegate {
    void startGame() throws GameException;
    void over() throws GameException;
    Integer windowWidth = 500;
    Integer windowHeight = 400;
    Integer bottomLine = 290;
    Integer rocketHeight = 5;
    Integer borderLeft = 5;
    Integer downAction = 15;
    Integer enemyHeight = 12;
    Integer enemyWidth = 12;
    Integer chances = 5;
    Integer difficulty = 11;
    Integer survivorWidth = 15;
    Integer survivorHeight = 20;
    Integer borderRight = 30;
    Integer enemiesToKill = 24;
}