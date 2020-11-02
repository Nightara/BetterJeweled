package de.dhbw.swe.betterjeweled.core;

import lombok.*;

/**
 * The CrystalRegion class represents a simple rectangular shape and offers utility functionality to check whether a
 * field is contained within this region.
 */
@Value
public class CrystalRegion
{
  int startX;
  int startY;
  int endX;
  int endY;

  /**
   * Returns whether the field with the supplied coordinates are contained within this region.
   *
   * @param posX The x (Horizontal) coordinate
   * @param posY The y (Vertical) coordinate
   * @return True if the supplied position is contained within this region, false otherwise
   */
  public boolean contains(int posX, int posY)
  {
    return posX >= getStartX() && posX < getEndX() && posY >= getStartY() && posY < getEndY();
  }

  /**
   * Returns the size of this region.
   *
   * @return The size of this region
   */
  public int getSize()
  {
    return Math.abs(getStartX() - getEndX()) * Math.abs(getStartY() - getEndY());
  }
}
