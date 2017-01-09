package me.noy.invaders.entity.entities;

import lombok.NonNull;
import me.noy.invaders.entity.Entity;

import javax.swing.ImageIcon;

public final class Missile extends Entity {

    public Missile() {}

    public Missile(Integer x, Integer y) {
        String missile = "images/missile.png";
        @SuppressWarnings("ConstantConditions") @NonNull ImageIcon imageIcon = new ImageIcon(this.getClass().getClassLoader().getResource(missile));
        this.setImage(imageIcon.getImage());
        int h_SPACE = 6;
        this.setX(x + h_SPACE);
        int v_SPACE = 1;
        this.setY(y - v_SPACE);
    }
}
