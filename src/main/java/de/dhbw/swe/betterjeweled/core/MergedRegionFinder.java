package de.dhbw.swe.betterjeweled.core;

import lombok.*;
import lombok.experimental.NonFinal;

import java.util.*;

@Value
@NonFinal
public class MergedRegionFinder implements RegionFinder
{
  @Getter(AccessLevel.PRIVATE)
  RegionFinder regionFinder;

  /**
   * This method takes regions found by another region finder (usually the default one) and merges the overlapping ones.
   *
   * @param grid The grid to look through
   * @param crystals The crystal types to look for
   * @return a map of merged CrystalRegions for each color of crystals.
   */
  @Override
  public Map<Crystal, List<CrystalRegion>> findRegions(CrystalGrid grid, Collection<Crystal> crystals)
  {
    Map<Crystal, List<CrystalRegion>> regions = getRegionFinder().findRegions(grid, crystals);
    Map<Crystal, List<CrystalRegion>> toReturn = new HashMap<>();
    for(Crystal crystal : regions.keySet())
    {
      List<CrystalRegion> temp = regions.get(crystal);
      for(int i = 0; i < temp.size(); i++)
      {
        CrystalRegion firstRegion = temp.get(i);
        for(int j = temp.size() - 1; j > i; j--)
        {
          CrystalRegion secondRegion = temp.get(j);
          if(firstRegion.intersects(secondRegion))
          {
            temp.set(i, CrystalRegion.merge(firstRegion, secondRegion));
            temp.remove(secondRegion);
          }
        }
      }
      toReturn.put(crystal, temp);
    }

    return toReturn;
  }
}
