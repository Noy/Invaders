package me.noy.invaders;

import lombok.extern.java.Log;
import me.noy.invaders.game.GameDelegate;
import me.noy.invaders.game.GameException;
import me.noy.invaders.game.impl.InvaderGame;

import javax.swing.*;

@Log
public final class Invaders implements GameDelegate {

    public static void main(String[] args) {
        log.info("Attempting to start game..");
        try {
            Invaders invaders = new Invaders();
            invaders.startGame();
            print("Enjoy!");
        } catch (GameException e) {
            e.printStackTrace();
            print("Could not load the Game! \nSee console log errors for info!");
        } finally {
            print("Created by Noy Hillel");
            print("https://www.github.com/NoyH");
        }
    }

    @SafeVarargs
    private static <T> void print(T... args) {
        for (T t : args) System.out.println(t);
    }

    @Override
    public void startGame() throws GameException {
        JFrame jFrame = new JFrame();
        jFrame.add(new InvaderGame());
        jFrame.setTitle("Invaders - Game by Noy Hillel");
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(windowWidth, windowHeight);
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
    }

    @Override
    public void over() throws GameException {}
}
