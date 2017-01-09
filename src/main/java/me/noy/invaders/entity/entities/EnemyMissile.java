package me.noy.invaders.entity.entities;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.noy.invaders.entity.Entity;

import javax.swing.*;

public final class EnemyMissile extends Entity {
    @Getter private final String enemyMissile = "images/enemy_missile.png";
    @Setter @Getter private boolean destroyed;

    protected EnemyMissile(int x, int y) {
        this.setDestroyed(true);
        this.x = x;
        this.y = y;
        @SuppressWarnings("ConstantConditions") @NonNull ImageIcon imageIcon = new ImageIcon(this.getClass().getClassLoader().getResource(enemyMissile));
        this.setImage(imageIcon.getImage());
    }
}
