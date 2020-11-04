package de.dhbw.swe.betterjeweled.core;

import lombok.*;

import java.util.*;
import java.util.List;
import java.util.function.*;
import java.util.stream.*;

@Value
public class CrystalGrid
{
  public static final int TRIGGER_SIZE = 3;
  public static final int SCORE_BASE = 100;

  int sizeX;
  int sizeY;
  Crystal[] colors;
  Crystal[][] field;

  public CrystalGrid(int sizeX, int sizeY, Crystal... colors)
  {
    this.sizeX = sizeX;
    this.sizeY = sizeY;
    this.colors = colors;
    this.field = new Crystal[sizeX][sizeY];
  }

  public int getSize()
  {
    return getSizeX() * getSizeY();
  }

  /**
   *
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
   *
   * @throws IndexOutOfBoundsException if the supplied coordinates are outside of the grid.
   */
  public void setCrystal(int posX, int posY, Crystal crystal)
  {
    getField()[posX][posY] = crystal;
  }

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
   *
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
            startX = endX - 1;
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
            startY = endY - 1;
          }
        }
      }

      return regions;
    }));
  }


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
