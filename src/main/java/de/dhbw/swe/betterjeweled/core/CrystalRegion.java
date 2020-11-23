package de.dhbw.swe.betterjeweled.core;

import java.util.*;
import java.util.stream.*;
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
  @Getter(AccessLevel.PRIVATE)
  Set<Position> positions;

  private CrystalRegion(Collection<Position> positions)
  {
    this.positions = new HashSet<>(positions);
  }

  public CrystalRegion(int startX, int startY, int endX, int endY)
  {
    this(Collections.emptySet());

    for(int i = startX; i < endX; i++)
    {
      for(int j = startY; j < endY; j++)
      {
        positions.add(new Position(i, j));
      }
    }
  }

  /**
   * Returns whether the field with the supplied coordinates are contained within this region.
   *
   * @param posX The x (Horizontal) coordinate
   * @param posY The y (Vertical) coordinate
   * @return True if the supplied position is contained within this region, false otherwise
   */
  public boolean contains(int posX, int posY)
  {
    return getPositions().contains(new Position(posX, posY));
  }

  /**
   * Returns the size of this region.
   *
   * @return The size of this region
   */
  public int getSize()
  {
    return getPositions().size();
  }

  /**
   * Returns whether or not two CrystalRegions intersect (e.g. both of them contain the same field(s) on the grid).
   *
   * @param other The second CrystalRegion to test
   * @return whether two regions intersect
   */
  public boolean intersects(CrystalRegion other)
  {
    return getPositions().stream()
        .anyMatch(other.getPositions()::contains);
  }

  /**
   * This method takes two or more CrystalRegions and merges them, returning a new Region composed of the merged ones.
   * @param regions An array of CrystalRegions to merge
   * @return the merged CrystalRegion
   */
  public static CrystalRegion merge(CrystalRegion... regions)
  {
    return merge(Arrays.asList(regions));
  }

  /**
   * This method takes two or more CrystalRegions and merges them, returning a new Region composed of the merged ones.
   * @param regions A Collection of CrystalRegions to merge
   * @return the merged CrystalRegion
   */
  public static CrystalRegion merge(Collection<CrystalRegion> regions)
  {
    Set<Position> fields = regions.stream()
        .map(CrystalRegion::getPositions)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    return new CrystalRegion(fields);
  }

  /**
   * An internal class for representing positions on the CrystalGrid. It's simply a data holder for an (x,y) Position.
   */
  @Value
  public static class Position
  {
    int x;
    int y;
  }
}
