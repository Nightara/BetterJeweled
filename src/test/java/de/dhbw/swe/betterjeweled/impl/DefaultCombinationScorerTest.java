package de.dhbw.swe.betterjeweled.impl;

import de.dhbw.swe.betterjeweled.core.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCombinationScorerTest
{
  @Test
  void scoreRegion()
  {
    CombinationScorer combinationScorer = new DefaultCombinationScorer();

    CrystalCombination r1_1 = new CrystalCombination(0,0,1,1);
    CrystalCombination r1_2 = new CrystalCombination(3,2,4,3);
    CrystalCombination r2_1 = new CrystalCombination(0,0,2,1);
    CrystalCombination r3_1 = new CrystalCombination(3,2,6,3);
    CrystalCombination r3_2 = new CrystalCombination(3,2,4,5);
    CrystalCombination r4_1 = new CrystalCombination(1,0,5,1);
    CrystalCombination r5_1 = new CrystalCombination(8,7,13,8);

    assertEquals(combinationScorer.scoreRegion(r1_1), combinationScorer.scoreRegion(r1_2));
    assertEquals(combinationScorer.scoreRegion(r3_1), combinationScorer.scoreRegion(r3_2));

    assertEquals(0, combinationScorer.scoreRegion(r1_1));
    assertEquals(0, combinationScorer.scoreRegion(r2_1));

    assertTrue(combinationScorer.scoreRegion(r4_1) > combinationScorer.scoreRegion(r3_1));
    assertTrue(combinationScorer.scoreRegion(r5_1) > combinationScorer.scoreRegion(r4_1));

    assertTrue(combinationScorer.scoreRegion(r4_1) > combinationScorer.scoreRegion(r3_1) + combinationScorer.scoreRegion(r3_2));
  }
}
