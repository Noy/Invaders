package me.noy.invaders.entity.entities;

import lombok.NonNull;
import me.noy.invaders.entity.Entity;
import me.noy.invaders.game.GameDelegate;
import me.noy.invaders.game.GameException;

import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public final class Survivor extends Entity implements GameDelegate {

    private int width;

    public Survivor() {
        String survivor = "images/survivor.png";
        @SuppressWarnings("ConstantConditions") @NonNull ImageIcon imageIcon = new ImageIcon(this.getClass().getClassLoader().getResource(survivor));
        width = imageIcon.getImage().getWidth(null);
        this.setImage(imageIcon.getImage());
        Integer startX = 270;
        this.setX(startX);
        Integer startY = 280;
        this.setY(startY);
    }

    public void go() {
        x += dx;
        if (x <= 2) x = 2;
        if (x >= windowWidth - 2 * width) x = windowWidth - 2 * width;
    }

    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                dx = -2;
                break;
            case KeyEvent.VK_RIGHT:
                dx = 2;
                break;
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                dx = 0;
                break;
            case KeyEvent.VK_RIGHT:
                dx = 0;
                break;
            default:
                break;
        }
    }

    @Override
    public void startGame() throws GameException {}

    @Override
    public void over() throws GameException {}
}