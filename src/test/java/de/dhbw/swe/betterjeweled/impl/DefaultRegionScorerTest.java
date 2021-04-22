package de.dhbw.swe.betterjeweled.impl;

import de.dhbw.swe.betterjeweled.core.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DefaultRegionScorerTest
{
  @Test
  void scoreRegion()
  {
    RegionScorer regionScorer = new DefaultRegionScorer();

    CrystalRegion r1_1 = new CrystalRegion(0,0,1,1);
    CrystalRegion r1_2 = new CrystalRegion(3,2,4,3);
    CrystalRegion r2_1 = new CrystalRegion(0,0,2,1);
    CrystalRegion r3_1 = new CrystalRegion(3,2,6,3);
    CrystalRegion r3_2 = new CrystalRegion(3,2,4,5);
    CrystalRegion r4_1 = new CrystalRegion(1,0,5,1);
    CrystalRegion r5_1 = new CrystalRegion(8,7,13,8);

    assertEquals(regionScorer.scoreRegion(r1_1), regionScorer.scoreRegion(r1_2));
    assertEquals(regionScorer.scoreRegion(r3_1), regionScorer.scoreRegion(r3_2));

    assertEquals(0, regionScorer.scoreRegion(r1_1));
    assertEquals(0, regionScorer.scoreRegion(r2_1));

    assertTrue(regionScorer.scoreRegion(r4_1) > regionScorer.scoreRegion(r3_1));
    assertTrue(regionScorer.scoreRegion(r5_1) > regionScorer.scoreRegion(r4_1));

    assertTrue(regionScorer.scoreRegion(r4_1) > regionScorer.scoreRegion(r3_1) + regionScorer.scoreRegion(r3_2));
  }
}
