package de.dhbw.swe.betterjeweled.core;

@FunctionalInterface
public interface CombinationScorer
{
  /**
   * Scores a supplied region. This method is expected to always fulfill the following requirements:<br />
   * <ol>
   *   <li>Regions shorter than MIN_COMBO_SIZE receive a score of zero.</li>
   *   <li>Regions that can be converted into each other by movement, mirroring and / or rotation receive the same
   *   score.</li>
   *   <li>Regions of size x receive a higher score than two regions of size x - 1.</li>
   * </ol>
   *
   * @param region The regions to score
   * @return A score for deleting the supplied region
   */
  int scoreRegion(CrystalCombination region);
}
