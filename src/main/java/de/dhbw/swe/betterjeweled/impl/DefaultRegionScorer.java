package de.dhbw.swe.betterjeweled.impl;

import de.dhbw.swe.betterjeweled.core.*;

import static de.dhbw.swe.betterjeweled.core.CrystalGrid.*;

/**
 * The default implementation of RegionScorer.
 */
public class DefaultRegionScorer implements RegionScorer
{
  /**
   * Scores a supplied region. This class uses a square function to meet the requirements specified in the interface.
   *
   * @param region The region to score
   * @return A score for deleting the supplied region
   */
  public int scoreRegion(CrystalRegion region)
  {
    if(region.getSize() < MIN_COMBO_SIZE)
    {
      return 0;
    }
    else
    {
      int scoreSize = 1 + region.getSize() - MIN_COMBO_SIZE;
      return scoreSize * scoreSize * SCORE_BASE;
    }
  }
}
