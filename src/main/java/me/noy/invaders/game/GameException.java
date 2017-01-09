package me.noy.invaders.game;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by noy on 08/12/2016.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public final class GameException extends Throwable { private final String errorMessage; }
