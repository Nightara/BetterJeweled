package de.dhbw.swe.betterjeweled.core;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.stream.*;

class CrystalRegionTest
{
  private static Stream<Arguments> generateRegions()
  {
    return Stream.of(
        Arguments.of(0, 0, 2, 3),
        Arguments.of(0, 0, 7, 1),
        Arguments.of(3, 2, 8, 4),
        Arguments.of(0, 0, 0, 0)
    );
  }

  @ParameterizedTest
  @MethodSource("generateRegions")
  @SuppressWarnings("SuspiciousNameCombination")
  void testTransposed(int startX, int startY, int endX, int endY)
  {
    CrystalRegion region = new CrystalRegion(startX, startY, endX, endY);
    CrystalRegion transposed = new CrystalRegion(startY, startX, endY, endX);

    Assertions.assertEquals(transposed, region.transposed());
    Assertions.assertEquals(region, transposed.transposed());
    Assertions.assertEquals(region, region.transposed().transposed());
  }
}