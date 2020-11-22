package de.dhbw.swe.betterjeweled.core;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static de.dhbw.swe.betterjeweled.core.CrystalGrid.*;

/**
 * The default implementation of RegionFinder.
 * This RegionFinder does not check for overlapping or fully contained regions and may result in multiple hits for
 * longer sequences.
 */
public class DefaultRegionFinder implements RegionFinder
{
  /**
   * Detects all regions of at least MIN_COMBO_SIZE length sorted by crystal type and returns them as a map of lists.
   *
   * @return All regions in the current grid
   */
  @Override
  public Map<Crystal, List<CrystalRegion>> findRegions(CrystalGrid grid, Collection<Crystal> crystals)
  {
    return crystals.stream().collect(Collectors.toMap(Function.identity(), color ->
    {
      List<CrystalRegion> regions = new LinkedList<>();

      Boolean[][] filteredGrid = Arrays.stream(grid.getField())
          .map(row -> Arrays.stream(row)
              .map(crystal -> crystal != null && crystal.countsAs(color))
              .toArray(Boolean[]::new))
          .toArray(Boolean[][]::new);

      // Search in X direction
      for(int y = 0; y < grid.getSizeY(); y++)
      {
        for(int startX = 0; startX <= grid.getSizeX() - MIN_COMBO_SIZE; startX++)
        {
          int endX = startX;
          while(endX < grid.getSizeX() && filteredGrid[endX][y])
          {
            endX++;
          }

          if(endX - startX >= MIN_COMBO_SIZE)
          {
            regions.add(new CrystalRegion(startX, y, endX,y + 1));
          }
        }
      }

      // Search in Y direction
      for(int x = 0; x < grid.getSizeX(); x++)
      {
        for(int startY = 0; startY <= grid.getSizeY() - MIN_COMBO_SIZE; startY++)
        {
          int endY = startY;
          while(endY < grid.getSizeY() && filteredGrid[x][endY])
          {
            endY++;
          }

          if(endY - startY >= MIN_COMBO_SIZE)
          {
            regions.add(new CrystalRegion(x, startY,x + 1, endY));
          }
        }
      }

      return regions;
    }));
  }
}
