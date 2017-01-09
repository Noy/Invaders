package me.noy.invaders.entity;

import lombok.Data;

import java.awt.Image;

@Data
public abstract class Entity {

    private boolean visible;
    protected boolean dying;
    private Image image;
    protected int x;
    protected int y;
    protected int dx;

    protected Entity() { visible = true; }

    public final boolean dead() { return visible = false; }
}
