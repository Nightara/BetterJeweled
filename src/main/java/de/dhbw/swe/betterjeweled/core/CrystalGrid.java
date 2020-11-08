package de.dhbw.swe.betterjeweled.core;

import lombok.*;

import java.util.*;
import java.util.List;
import java.util.function.*;
import java.util.stream.*;

/**
 * The CrystalGrid class represents a rectangular grid of variable size containing any number of crystals within its
 * bounds.
 * <br />
 * The class offers basic functionality like group detection and general methods required for grid manipulation, like
 * deletion of groups, switch of neighboring crystal, random refill of empty spaces, and gravitational pull. The grid
 * does not contain any game logic or order of actions, it merely acts as a modifiable two-dimensional container for
 * Crystal objects.
 * <br />
 * The number and list of valid Crystals for any grid as well as its size is set during creation and can not be modified
 * afterwards.
 * <br />
 * Any CrystalGrid holds a private Random object that will be used for any required randomness. Creating two CrystalGrid
 * objects with the same size, color list and seeds for their respective Random objects results in two similar grids
 * that can be expected to behave equally if treated the same way. Currently the only method calling the Random object
 * is CrystalGrid#fillGrid.
 * <br />
 * The grid coordinates are recommended to be interpreted like monitor coordinates, with x depicting the horizontal
 * axis, y depicting the vertical axis, and [0, 0] representing the top left corner. The x coordinate can be inverted
 * without causing any problems, but it is strongly recommended to not invert the y axis, because the default
 * implementation of the CrystalGrid#shiftCrystals "gravitation" method shifts every Crystal towards positive y and all
 * empty fields towards negative y.
 */
@Value
public class CrystalGrid
{
  /**
   * The constant TRIGGER_SIZE.
   */
  public static final int TRIGGER_SIZE = 3;
  /**
   * The constant SCORE_BASE.
   */
  public static final int SCORE_BASE = 100;

  int sizeX;
  int sizeY;
  Crystal[] colors;
  Crystal[][] field;

  /**
   * The default constructor.
   *
   * @param sizeX  The x (Horizontal) size of the grid
   * @param sizeY  The y (Vertical) size of the grid
   * @param colors The colors available for the grid
   */
  public CrystalGrid(int sizeX, int sizeY, Crystal... colors)
  {
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    this.colors = colors;
    this.field = new Crystal[sizeX][sizeY];
  }

  /**
   * Returns the total number of fields contained in the grid.
   *
   * @return The size of the grid
   */
  public int getSize()
  {
    return getSizeX() * getSizeY();
  }

  /**
   * Returns the Crystal stored at the supplied coordinates.
   *
   * @param posX The x (Horizontal) coordinate of the crystal
   * @param posY The y (Vertical) coordinate of the crystal
   * @return The requested Crystal or Optional#empty if none is present
   * @throws IndexOutOfBoundsException if the supplied coordinates are outside of the grid.
   */
  public Optional<Crystal> getCrystal(int posX, int posY)
  {
    if(posX >= 0 && posX < getSizeX()
        && posY >= 0 && posY < getSizeY())
    {
      return Optional.ofNullable(getField()[posX][posY]);
    }

    throw new IndexOutOfBoundsException("The coordinates [" + posX + ", " + posY +
        "] are not within the field bounds [" + getSizeX() + ", " + getSizeY() + "].");
  }

  /**
   * Sets a supplied Crystal at the supplied coordinates. This method does not check for duplication or validity of the
   * supplied Crystal for this grid and should be used with caution.
   *
   * @param posX    The x (Horizontal) coordinate of the crystal
   * @param posY    The y (Vertical) coordinate of the crystal
   * @param crystal The Crystal object to be placed
   * @throws IndexOutOfBoundsException if the supplied coordinates are outside of the grid.
   */
  public void setCrystal(int posX, int posY, Crystal crystal)
  {
    getField()[posX][posY] = crystal;
  }

  /**
   * Fills all empty fields of this grid by randomly picking crystals from this grid's color list using the internal
   * Random object. Like CrystalGrid#setCrystal, this method does not create copies of the reference crystals, but
   * simply creates a new reference to those objects instead.
   *
   * @return The total number of modified fields
   */
  public int fillGrid()
  {
    return fillGrid(new Random());
  }

  public int fillGrid(Random seed)
  {
    int generated = 0;
    for(int posX = 0; posX < getSizeX(); posX++)
    {
      for(int posY = 0; posY < getSizeY(); posY++)
      {
        if(getCrystal(posX, posY).isEmpty())
        {
          setCrystal(posX, posY, getColors()[seed.nextInt(getColors().length)]);
          generated++;
        }
      }
    }

    return generated;
  }

  /**
   * Switches two adjacent fields, returning true if the requested action resulted in a change of the grid, and false
   * otherwise. Any switch request that is within bounds of the grid and targets two fields that are orthogonally
   * adjacent to each other is considered to be a "change" of the grid, even if the two targeted fields contain the same
   * type of crystal (Or null).
   *
   * @param posXOne The x (Horizontal) coordinate of the first crystal
   * @param posYOne The y (Vertical) coordinate of the first crystal
   * @param posXTwo The x (Horizontal) coordinate of the second crystal
   * @param posYTwo The y (Vertical) coordinate of the second crystal
   * @return True if the request resulted in a change of the grid, false otherwise
   * @throws IndexOutOfBoundsException if the supplied coordinates are outside of the grid.
   */
  public boolean switchCrystals(int posXOne, int posYOne, int posXTwo, int posYTwo)
  {
    if(Math.abs(posXOne - posXTwo) + Math.abs(posYOne - posYTwo) == 1)
    {
      Optional<Crystal> crysOne = getCrystal(posXOne, posYOne);
      Optional<Crystal> crysTwo = getCrystal(posXTwo, posYTwo);

      setCrystal(posXOne, posYOne, crysTwo.orElse(null));
      setCrystal(posXTwo, posYTwo, crysOne.orElse(null));

      return true;
    }

    return false;
  }

  /**
   * Detects all regions of at least TRIGGER_SIZE length sorted by crystal type and returns them as a map of lists.
   *
   * @return All regions in the current grid
   */
  public Map<Crystal, List<CrystalRegion>> findRegions()
  {
    return Arrays.stream(getColors()).collect(Collectors.toMap(Function.identity(), color ->
    {
      List<CrystalRegion> regions = new LinkedList<>();

      Boolean[][] filteredGrid = Arrays.stream(getField())
          .map(row -> Arrays.stream(row)
              .map(crystal -> crystal != null && crystal.countsAs(color))
              .toArray(Boolean[]::new))
          .toArray(Boolean[][]::new);

      // Search in X direction
      for(int y = 0; y < getSizeY(); y++)
      {
        for(int startX = 0; startX <= getSizeX() - TRIGGER_SIZE; startX++)
        {
          int endX = startX;
          while(endX < getSizeX() && filteredGrid[endX][y])
          {
            endX++;
          }

          if(endX - startX >= TRIGGER_SIZE)
          {
            regions.add(new CrystalRegion(startX, y, endX,y + 1));
          }
        }
      }

      // Search in Y direction
      for(int x = 0; x < getSizeX(); x++)
      {
        for(int startY = 0; startY <= getSizeY() - TRIGGER_SIZE; startY++)
        {
          int endY = startY;
          while(endY < getSizeY() && filteredGrid[x][endY])
          {
            endY++;
          }

          if(endY - startY >= TRIGGER_SIZE)
          {
            regions.add(new CrystalRegion(x, startY,x + 1, endY));
          }
        }
      }

      return regions;
    }));
  }

  /**
   * Detects all regions using CrystalGrid#findRegions and deleted all crystals contained within them, returning the
   * total score for the creation of these regions according to CrystalGrid#scoreRegion. Crystals are deleted from left
   * to right, then top to bottom.
   *
   * @return The total scores for the deleted regions
   */
  public int triggerRegions()
  {
    return findRegions().values().stream()
        .flatMap(List::stream)
        .mapToInt(region ->
        {
          for(int x = 0; x < getSizeX(); x++)
          {
            for(int y = 0; y < getSizeY(); y++)
            {
              if(region.contains(x, y))
              {
                setCrystal(x, y, null);
              }
            }
          }
          return scoreRegion(region);
        }).sum();
  }

  /**
   * Scores a supplied region. This method is expected to always fulfill the following requirements:<br />
   * <ol>
   *   <li>Regions shorter than TRIGGER_SIZE receive a score of zero.</li>
   *   <li>Regions that can be converted into each other by movement, mirroring and / or rotation receive the same
   *   score.</li>
   *   <li>Regions of size x receive a higher score than two regions of size x - 1.</li>
   * </ol>
   *
   * @param region The region to score
   * @return A score for deleting the supplied region
   */
  public static int scoreRegion(CrystalRegion region)
  {
    if(region.getSize() < TRIGGER_SIZE)
    {
      return 0;
    }
    else
    {
      int scoreSize = 1 + region.getSize() - TRIGGER_SIZE;
      return scoreSize * scoreSize * SCORE_BASE;
    }
  }
}
