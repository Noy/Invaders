package me.noy.invaders.game.impl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by noy on 08/12/2016.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface GameStatus {
    String value();
}
