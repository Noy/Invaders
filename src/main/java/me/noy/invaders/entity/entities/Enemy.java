package me.noy.invaders.entity.entities;

import lombok.Getter;
import lombok.NonNull;
import me.noy.invaders.entity.Entity;

import javax.swing.ImageIcon;

public final class Enemy extends Entity {

    @Getter private EnemyMissile enemyMissile;

    public Enemy(Integer xCord, Integer yCord) { // Discord? lololol
        String enemyPic = "images/enemy.png";
        @NonNull ImageIcon imageIcon = new ImageIcon(this.getClass().getClassLoader().getResource(enemyPic));
        this.x = xCord;
        this.y = yCord;
        enemyMissile = new EnemyMissile(xCord, yCord);
        setImage(imageIcon.getImage());
    }

    public void fireMissile(Integer direction) { this.x += direction; }
}