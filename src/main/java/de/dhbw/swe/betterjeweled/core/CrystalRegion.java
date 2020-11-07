package de.dhbw.swe.betterjeweled.core;

import lombok.*;
import lombok.experimental.NonFinal;

@Value
@NonFinal
public class CrystalRegion
{
  int startX;
  int startY;
  int endX;
  int endY;

  public boolean contains(int posX, int posY)
  {
    return posX >= getStartX() && posX < getEndX() && posY >= getStartY() && posY < getEndY();
  }

  public int getSize()
  {
    return Math.abs(getStartX() - getEndX()) * Math.abs(getStartY() - getEndY());
  }

  public boolean intersects(CrystalRegion other) {
    boolean intersects = false;
    for (int i = startX; i < endX; i++) {
      for (int j = startY; j < endY; j++) {
        intersects = intersects || other.contains(i, j);
      }
    }
    return intersects;
  }
}
