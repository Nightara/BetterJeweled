package de.dhbw.swe.betterjeweled.core;

import lombok.*;

import java.util.*;

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
// TODO: Fix naming of grid vs field, fix naming switchCrystals / exchangeCrystals
public class CrystalGrid
{
  /**
   * The constant MIN_COMBO_SIZE.
   */
  public static final int MIN_COMBO_SIZE = 3;
  /**
   * The constant SCORE_BASE.
   */
  public static final int SCORE_BASE = 100;

  int sizeX;
  int sizeY;
  Crystal[] colors;
  @Getter(AccessLevel.PRIVATE)
  Crystal[][] field;
  
  @EqualsAndHashCode.Exclude
  @Getter(AccessLevel.PRIVATE)
  Random random;

  /**
   * The default constructor.
   *
   * @param sizeX  The x (Horizontal) size of the grid
   * @param sizeY  The y (Vertical) size of the grid
   * @param colors The colors available for the grid
   * @throws IllegalArgumentException if either of sizeX or sizeY is <= 0.
   */
  public CrystalGrid(int sizeX, int sizeY, Crystal... colors)
  {
    this(sizeX, sizeY, new Random().nextInt(), colors);
  }

  /**
   * The default constructor.
   *
   * @param sizeX  The x (Horizontal) size of the grid
   * @param sizeY  The y (Vertical) size of the grid
   * @param seed   The seed to initialize this grid's Random
   * @param colors The colors available for the grid
   * @throws IllegalArgumentException if either of sizeX or sizeY is <= 0.
   */
  public CrystalGrid(int sizeX, int sizeY, int seed, Crystal... colors)
  {
    if(sizeX < MIN_COMBO_SIZE)
    {
      throw new IllegalArgumentException("X size cannot be less than " + MIN_COMBO_SIZE + " but was " + sizeX);
    }
    if(sizeY < MIN_COMBO_SIZE)
    {
      throw new IllegalArgumentException("Y size cannot be less than " + MIN_COMBO_SIZE + " but was " + sizeY);
    }

    this.sizeX = sizeX;
    this.sizeY = sizeY;
    this.colors = colors;
    this.random = new Random(seed);
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
    int generated = 0;
    for(int posX = 0; posX < getSizeX(); posX++)
    {
      for(int posY = 0; posY < getSizeY(); posY++)
      {
        if(getCrystal(posX, posY).isEmpty())
        {
          setCrystal(posX, posY, getColors()[getRandom().nextInt(getColors().length)]);
          generated++;
        }
      }
    }

    return generated;
  }

  /**
   * Generates and returns a read-only copy of the field data, formatted as a 2D array.
   * The X coordinate translates into the first dimension, the Y coordinate into the second dimension of the array.
   *
   * @return A copy of the internal grid
   */
  public Crystal[][] viewField()
  {
    Crystal[][] grid = new Crystal[getSizeX()][getSizeY()];
    for(int x = 0; x < getSizeX(); x++)
    {
      for(int y = 0; y < getSizeY(); y++)
      {
        grid[x][y] = getCrystal(x, y).orElse(null);
      }
    }

    return grid;
  }

  /**
   * Switches two adjacent fields, returning true if the requested action resulted in a change of the grid, and false
   * otherwise. Any switch request that is within bounds of the grid and targets two fields that are orthogonally
   * adjacent to each other is considered to be a "change" of the grid, even if the two targeted fields contain the same
   * type of crystal (Or null).
   *
   * @param move The move object representing the move to be done.
   * @return True if the request resulted in a change of the grid, false otherwise
   * @throws IndexOutOfBoundsException if the supplied coordinates are outside of the grid.
   */
  public boolean switchCrystals(Move move)
  {
    return switchCrystals(move.getX1(), move.getY1(), move.getX2(), move.getY2());
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
      exchangeCrystals(posXOne, posYOne, posXTwo, posYTwo);
      return true;
    }

    return false;
  }

  /**
   * Swaps the positions of two crystals, or a crystal and an empty field.
   * @throws IndexOutOfBoundsException if the supplied coordinates are outside of the grid.
   */
  private void exchangeCrystals(int posXOne, int posYOne, int posXTwo, int posYTwo)
  {
    Optional<Crystal> crysOne = getCrystal(posXOne, posYOne);
    Optional<Crystal> crysTwo = getCrystal(posXTwo, posYTwo);

    setCrystal(posXOne, posYOne, crysTwo.orElse(null));
    setCrystal(posXTwo, posYTwo, crysOne.orElse(null));
  }

  protected int triggerRegions(Map<Crystal, List<CrystalRegion>> regions, RegionScorer scorer)
  {
    return regions.values().stream()
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
          return scorer.scoreRegion(region);
        }).sum();
  }

  /**
   * Detects all regions using CrystalGrid#findRegions and deleted all crystals contained within them, returning the
   * total score for the creation of these regions according to CrystalGrid#scoreRegion. Crystals are deleted from left
   * to right, then top to bottom.
   *
   * @return The total scores for the deleted regions
   */
  public int triggerRegions(RegionFinder finder, RegionScorer scorer)
  {
    return triggerRegions(finder.findRegions(this), scorer);
  }

  /**
   * Implements the "gravity" portion of the CrystalGrid - e.g. after all combos are removed from the grid, it shifts
   * all crystals to the lowermost position available.
   */
  public void shiftCrystals()
  {
    for(int x = 0; x < getSizeX(); x++)
    {
      for(int y = getSizeY() - 1; y > 0; y--)
      {
        int shift = 0;
        while(shift <= y && getCrystal(x, y - shift).isEmpty())
        {
          shift++;
        }

        if(shift <= y)
        {
          exchangeCrystals(x, y, x,y - shift);
        }
      }
    }
  }
}
