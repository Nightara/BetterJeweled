package de.dhbw.swe.betterjeweled.impl;

import de.dhbw.swe.betterjeweled.core.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static de.dhbw.swe.betterjeweled.core.CrystalGrid.*;

/**
 * The default implementation of RegionFinder.
 * This RegionFinder does not check for overlapping or fully contained regions and may result in multiple hits for
 * longer sequences.
 */
public class DefaultCombinationFinder implements CombinationFinder
{
  /**
   * Detects all regions of at least MIN_COMBO_SIZE length sorted by crystal type and returns them as a map of lists.
   *
   * @return All regions in the current grid
   */
  @Override
  public Map<Crystal, List<CrystalCombination>> findRegions(CrystalGrid grid, Collection<Crystal> crystals)
  {
    return crystals.stream().collect(Collectors.toMap(Function.identity(), color ->
    {
      Boolean[][] filteredGrid = Arrays.stream(grid.viewGrid())
          .map(row -> Arrays.stream(row)
              .map(crystal -> crystal != null && crystal.countsAs(color))
              .toArray(Boolean[]::new))
          .toArray(Boolean[][]::new);

      List<CrystalCombination> regions = new LinkedList<>(findRegionsInY(filteredGrid));
      findRegionsInY(transposeGrid(filteredGrid)).forEach(region -> regions.add(region.transposed()));

      return regions;
    }));
  }

  @SuppressWarnings("java:S127")
  private static List<CrystalCombination> findRegionsInY(Boolean[][] filteredGrid)
  {
    List<CrystalCombination> regions = new LinkedList<>();

    for(int x = 0; x < filteredGrid.length; x++)
    {
      for(int startY = 0; startY <= filteredGrid[x].length - MIN_COMBO_SIZE; startY++)
      {
        int endY = startY;
        while(endY < filteredGrid[x].length && Boolean.TRUE.equals(filteredGrid[x][endY]))
        {
          endY++;
        }

        if(endY - startY >= MIN_COMBO_SIZE)
        {
          regions.add(new CrystalCombination(x, startY,x + 1, endY));
          startY = endY - 1;
        }
      }
    }

    return regions;
  }

  protected static Boolean[][] transposeGrid(Boolean[][] oldGrid)
  {
    Boolean[][] newGrid = new Boolean[oldGrid[0].length][oldGrid.length];
    for(int x = 0; x < oldGrid.length; x++)
    {
      for(int y = 0; y < oldGrid[x].length; y++)
      {
        newGrid[y][x] = oldGrid[x][y];
      }
    }

    return newGrid;
  }
}
