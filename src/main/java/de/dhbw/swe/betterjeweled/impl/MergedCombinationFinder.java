package de.dhbw.swe.betterjeweled.impl;

import de.dhbw.swe.betterjeweled.core.*;
import lombok.*;
import lombok.experimental.*;

import java.util.*;

@Value
@NonFinal
public class MergedCombinationFinder implements CombinationFinder
{
  @Getter(AccessLevel.PRIVATE)
  CombinationFinder combinationFinder;

  /**
   * This method takes regions found by another region finder (usually the default one) and merges the overlapping ones.
   *
   * @param grid The grid to look through
   * @param crystals The crystal types to look for
   * @return a map of merged CrystalRegions for each color of crystals.
   */
  @Override
  public Map<Crystal, List<CrystalCombination>> findRegions(CrystalGrid grid, Collection<Crystal> crystals)
  {
    Map<Crystal, List<CrystalCombination>> regions = getCombinationFinder().findRegions(grid, crystals);
    Map<Crystal, List<CrystalCombination>> toReturn = new HashMap<>();
    for(Map.Entry<Crystal, List<CrystalCombination>> entry : regions.entrySet())
    {
      Crystal crystal = entry.getKey();
      List<CrystalCombination> temp = entry.getValue();
      for(int i = 0; i < temp.size(); i++)
      {
        CrystalCombination firstRegion = temp.get(i);
        for(int j = temp.size() - 1; j > i; j--)
        {
          CrystalCombination secondRegion = temp.get(j);
          if(firstRegion.intersects(secondRegion))
          {
            firstRegion = CrystalCombination.merge(firstRegion, secondRegion);
            temp.remove(secondRegion);
          }
        }
        temp.set(i, firstRegion);
      }
      toReturn.put(crystal, temp);
    }

    return toReturn;
  }
}
