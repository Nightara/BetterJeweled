package de.dhbw.swe.betterjeweled.player;

import javafx.scene.control.*;
import lombok.*;

@Value
@EqualsAndHashCode(callSuper=true)
@SuppressWarnings("java:S110")
public class CoordinateToggleButton extends ToggleButton
{
  int posX;
  int posY;

  public CoordinateToggleButton(String s, int posX, int posY)
  {
    super(s);
    this.posX = posX;
    this.posY = posY;
  }
}
