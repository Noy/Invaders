package me.noy.invaders.game.impl;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Synchronized;
import me.noy.invaders.scala.ScalaBae;
import me.noy.invaders.entity.*;
import me.noy.invaders.entity.entities.Enemy;
import me.noy.invaders.entity.entities.EnemyMissile;
import me.noy.invaders.entity.entities.Missile;
import me.noy.invaders.entity.entities.Survivor;
import me.noy.invaders.game.GameDelegate;
import me.noy.invaders.game.GameException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("ConstantConditions")
@GameStatus("Game Over!")
public final class InvaderGame extends JPanel implements Runnable, GameDelegate {

    /*
       Entities
    */
    private LinkedList<Enemy> enemies;
    private Survivor survivor;
    private Missile missile;

    /*
        Style/Logic
     */
    // legit made this because I wanted to test scala lol
    private ScalaBae scalaBae;
    private final Dimension dimension;
    private Integer direction = -1;
    private Integer deaths = 0;
    private boolean inGame = true;
    private Thread animations;
    private GameStatus gameStatus = this.getClass().getAnnotation(GameStatus.class);

    // Initializer
    {
        dimension = new Dimension(windowWidth, windowHeight);
        scalaBae = new ScalaBae();
        // JLabel
        addKeyListener(new KeyPressListener());
        setFocusable(true);
        setDoubleBuffered(true);

        // Delegate method
        try {
            startGame();
        } catch (GameException e) {
            e.printStackTrace();
            severeDisable(e);
        }
    }

    @Override
    @SneakyThrows
    public void addNotify() {
        super.addNotify();
        startGame();
    }

    @Override
    public void startGame() throws GameException {
        enemies = new LinkedList<>();
        String enemyPicture = "images/enemy.png";
        @NonNull ImageIcon imageIcon = new ImageIcon(this.getClass().getClassLoader().getResource(enemyPicture));
        for (int i=0; i < 4; i++) {
            for (int j=0; j < 6; j++) {
                Integer enemyX = 150;
                Integer enemyY = 5;
                Enemy enemy = new Enemy(enemyX + 18*j, enemyY + 18*i);
                enemy.setImage(imageIcon.getImage());
                enemies.add(enemy);
            }
        }
        survivor = new Survivor();
        missile = new Missile();
        if (animations == null || !inGame) {
            animations = new Thread(this);
            animations.start();
        }
    }

    private void severeDisable(Throwable t) {
        System.out.println(t.getMessage());
        System.exit(0);
    }

    private void restartGame() throws GameException { survivor.dead(); }

    @Synchronized // prevents thread interference
    private void createEnemies(Graphics g) {
        Iterator enemyIteration = enemies.iterator();
        //noinspection WhileLoopReplaceableByForEach, // autism
        while (enemyIteration.hasNext()) {
            Enemy enemy = (Enemy) enemyIteration.next();
            if (enemy.isVisible()) g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            if (enemy.isDying()) enemy.dead();
        }
    }

    @Synchronized
    private void createSurvivor(Graphics g) {
        if (survivor.isVisible()) g.drawImage(survivor.getImage(), survivor.getX(), survivor.getY(), this);
        if (survivor.isDying()) {
            survivor.dead();
            inGame = false;
        }
    }

    @Synchronized
    private void createMissile(Graphics g) { if (missile.isVisible()) g.drawImage(missile.getImage(), missile.getX(), missile.getY(), this); }

    @Synchronized
    private void createExplosions(Graphics g) {
        for (Enemy enemy : enemies) {
            EnemyMissile enemyMissile = enemy.getEnemyMissile();
            if (!enemyMissile.isDestroyed()) g.drawImage(enemyMissile.getImage(), enemyMissile.getX(), enemyMissile.getY(), this);
        }
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, dimension.width, dimension.height);
        scalaBae.randomColor(graphics);
        if (inGame) {
            graphics.drawLine(0, bottomLine, windowWidth, bottomLine);
            createEnemies(graphics);
            createSurvivor(graphics);
            createMissile(graphics);
            createExplosions(graphics);
        }
        Toolkit.getDefaultToolkit().sync();
        graphics.dispose();
    }

    @Override
    public void over() throws GameException {
        Graphics graphics = this.getGraphics();
        graphics.setColor(Color.DARK_GRAY);
        restartGame();
        graphics.fillRect(50, windowWidth / 2 - 30, windowWidth -100, 50);
        graphics.setColor(Color.white);
        graphics.drawRect(50, windowWidth / 2 - 30, windowWidth -100, 50);
        Font font = new Font(Font.SERIF, 1, 19);
        FontMetrics fontMetrics = this.getFontMetrics(font);
        graphics.setColor(Color.WHITE);
        graphics.setFont(font);
        graphics.drawString(gameStatus.value(),(windowWidth - fontMetrics.stringWidth(gameStatus.value()))/2, windowWidth /2);
        inGame = false;
    }

    @Synchronized
    private void slice()  {
        if (Objects.equals(deaths, enemiesToKill)) inGame = false;
        survivor.go();
        String explosionPic = "images/explosion.png"; // walmart logo lol
        if (missile.isVisible()) {
            Iterator enemyIteration = enemies.iterator();
            Integer shotX = missile.getX();
            Integer shotY = missile.getY();
            while (enemyIteration.hasNext()) {
                Enemy enemy = (Enemy) enemyIteration.next();
                Integer enemyX = enemy.getX();
                Integer enemyY = enemy.getY();
                if (enemy.isVisible() && missile.isVisible()) {
                    if (shotX >= (enemyX) && shotX <= (enemyX + enemyWidth) && shotY >= (enemyY) &&
                            shotY <= (enemyY+ enemyHeight) ) {
                        ImageIcon ii = new ImageIcon(getClass().getClassLoader().getResource(explosionPic));
                        enemy.setImage(ii.getImage());
                        enemy.setDying(true);
                        deaths++;
                        missile.dead();
                    }
                }
            }
            Integer y = missile.getY();
            y -= 4;
            if (y < 0) missile.dead();
            else missile.setY(y);
        }
        for (Enemy enemy1 : enemies) {
            Integer x = enemy1.getX();
            if (x >= windowWidth - borderRight && direction != -1) {
                direction = -1;
                for (Enemy e2 : enemies) {
                    e2.setY(e2.getY() + downAction);
                }
            }
            if (x <= borderLeft && direction != 1) {
                direction = 1;
                for (Enemy enemy : enemies) {
                    enemy.setY(enemy.getY() + downAction);
                }
            }
        }
        enemies.stream().filter(Entity::isVisible).forEach(enemy1 -> {
            Integer y = enemy1.getY();
            if (y > bottomLine - enemyHeight) inGame = false;
            enemy1.fireMissile(direction);
        });

        Iterator enemyIteration = enemies.iterator();
        while (enemyIteration.hasNext()) {
            Integer missile =  scalaBae.randomInt(15); //random.nextInt(15);
            Enemy enemy = (Enemy) enemyIteration.next();
            EnemyMissile enemyMissile = enemy.getEnemyMissile();
            if (Objects.equals(missile, chances) && enemy.isVisible() && enemyMissile.isDestroyed()) {
                enemyMissile.setDestroyed(false);
                enemyMissile.setX(enemy.getX());
                enemyMissile.setY(enemy.getY());
            }
            Integer enemyMissileX = enemyMissile.getX();
            Integer enemyMissileY = enemyMissile.getY();
            Integer survivorX = survivor.getX();
            Integer survivorY = survivor.getY();

            if (survivor.isVisible() && !enemyMissile.isDestroyed()) {
                if (enemyMissileX >= (survivorX) && enemyMissileX <= (survivorX+ survivorWidth) && enemyMissileY >= (survivorY) &&
                        enemyMissileY <= (survivorY+ survivorHeight) ) {
                    ImageIcon imageIcon = new ImageIcon(this.getClass().getClassLoader().getResource(explosionPic));
                    survivor.setImage(imageIcon.getImage());
                    survivor.setDying(true);
                    enemyMissile.setDestroyed(true);
                }
            }
            if (!enemyMissile.isDestroyed()) {
                enemyMissile.setY(enemyMissile.getY() + 1);
                if (enemyMissile.getY() >= bottomLine - rocketHeight) enemyMissile.setDestroyed(true);
            }
        }
    }

    @Override
    @SneakyThrows
    public void run() {
        long before, difference, sleep;
        before = System.currentTimeMillis();
        while (inGame) {
            repaint();
            slice();
            difference = System.currentTimeMillis() - before;
            sleep = difficulty - difference;
            if (sleep < 0) sleep = 2;
            Thread.sleep(sleep);
            before = System.currentTimeMillis();
        }
        over();
    }

    private final class KeyPressListener extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) { survivor.keyReleased(e); }

        @Override @SneakyThrows
        public void keyPressed(KeyEvent e) {
            survivor.keyPressed(e);
            Integer x = survivor.getX();
            Integer y = survivor.getY();
            if (inGame) if (e.getKeyCode() == KeyEvent.VK_SPACE) if (!missile.isVisible()) missile = new Missile(x, y);
        }
    }
}
