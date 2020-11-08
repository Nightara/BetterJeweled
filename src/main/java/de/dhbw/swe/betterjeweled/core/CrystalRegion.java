package de.dhbw.swe.betterjeweled.core;

import lombok.*;

/**
 * The CrystalRegion class represents a simple rectangular shape and offers utility functionality to check whether a
 * field is contained within this region.
 * <br />
 * A CrystalRegion is always defined by its starting and ending coordinates. The region coordinates are considered to be
 * start-inclusive and end-exclusive in both directions.
 * <br />
 * Examples (Region coordinates given below the grid in the shape of [StartX, StartY; EndX, EndY]):
 * <br />
 * OXO
 * OXO
 * OOO
 * ---
 * [1, 0; 2, 3]
 * <br />
 * XXXO
 * XXXO
 * XXXO
 * XXXO
 * OOOO
 * ---
 * [0, 0; 3, 4]
 * <br />
 * OO
 * OO
 * ---
 * [0, 0; 0, 0]
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
