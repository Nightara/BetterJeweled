package de.dhbw.swe.betterjeweled.core;

import lombok.*;

@Value
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
}
