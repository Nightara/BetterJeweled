package de.dhbw.swe.betterjeweled.core;

import java.util.*;

@FunctionalInterface
public interface RegionFinder
{
  /**
   * Detects all regions of the given crystal types and returns them grouped by crystal type.
   *
   * @param grid The grid to test
   * @param crystals The crystal types to look for
   * @return A map containing all detected regions
   */
  Map<Crystal, List<CrystalRegion>> findRegions(CrystalGrid grid, Collection<Crystal> crystals);

  /**
   * A convenience Varargs wrapper for RegionFinder#findRegions.
   * If crystals is empty, grid#getColors will be used instead.
   *
   * @param grid The grid to test
   * @param crystals The crystal types to look for
   * @return A map containing all detected regions
   */
  default Map<Crystal, List<CrystalRegion>> findRegions(CrystalGrid grid, Crystal... crystals)
  {
    if(crystals.length == 0)
    {
      crystals = grid.getColors();
    }

    return findRegions(grid, Arrays.asList(crystals));
  }
}
