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
