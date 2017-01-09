package me.noy.invaders.scala

import java.awt.{Color, Graphics}

/**
  * Created by noy on 09/12/2016.
  */

final class ScalaBae {

  /**
    * Honestly, this isn't needed, but I can't say no to practice and experimenting lol
    */

  val random = scala.util.Random

  def randomInt(int: Int): Int = {
    random.nextInt(int)
  }

  def randomColor(graphics: Graphics): Unit = {
    val red = random.nextFloat()
    val green = random.nextFloat()
    val blue = random.nextFloat()
    val randomColor = new Color(red, green, blue)
    graphics.setColor(randomColor)
  }
}
