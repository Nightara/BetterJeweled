package de.dhbw.swe.betterjeweled.impl;

import de.dhbw.swe.betterjeweled.core.*;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.*;
import java.util.List;

class DefaultRegionFinderTest
{
  private static final Crystal RED = new Crystal(Color.RED);
  private static final Crystal GREEN = new Crystal(Color.GREEN);
  private static final Crystal BLUE = new Crystal(Color.BLUE);

  private static CrystalGrid grid;

  @BeforeEach
  private void setupTriggerGrid()
  {
    grid = new CrystalGrid(5, 5, RED, GREEN, BLUE);
    grid.setCrystal(0,0, RED);
    grid.setCrystal(0,1, RED);
    grid.setCrystal(0,2, RED);
    grid.setCrystal(0,3, GREEN);
    grid.setCrystal(0,4, GREEN);

    grid.setCrystal(1,0, RED);
    grid.setCrystal(1,1, BLUE);
    grid.setCrystal(1,2, BLUE);
    grid.setCrystal(1,3, BLUE);
    grid.setCrystal(1,4, GREEN);

    grid.setCrystal(2,0, RED);
    grid.setCrystal(2,1, BLUE);
    grid.setCrystal(2,2, RED);
    grid.setCrystal(2,3, RED);
    grid.setCrystal(2,4, GREEN);

    grid.setCrystal(3,0, GREEN);
    grid.setCrystal(3,1, RED);
    grid.setCrystal(3,2, BLUE);
    grid.setCrystal(3,3, BLUE);
    grid.setCrystal(3,4, RED);

    grid.setCrystal(4,0, RED);
    grid.setCrystal(4,1, RED);
    grid.setCrystal(4,2, RED);
    grid.setCrystal(4,3, RED);
  }

  @Test
  void findDefaultRegions()
  {
    RegionFinder regionFinder = new DefaultRegionFinder();

    Map<Crystal, List<CrystalRegion>> regions = regionFinder.findRegions(grid, grid.getColors());

    Assertions.assertEquals(3, regions.keySet().size());
    Assertions.assertEquals(3, regions.get(RED).size());
    Assertions.assertEquals(1, regions.get(GREEN).size());
    Assertions.assertEquals(1, regions.get(BLUE).size());
  }

  @Test
  void findRegionsWithMerging()
  {
    Map<Crystal, List<CrystalRegion>> regions = new MergedRegionFinder(new DefaultRegionFinder()).findRegions(grid);

    Assertions.assertEquals(3, regions.keySet().size());
    Assertions.assertEquals(2, regions.get(RED).size());
    Assertions.assertEquals(1, regions.get(GREEN).size());
    Assertions.assertEquals(1, regions.get(BLUE).size());
  }

  @Test
  void testTransposeGrid()
  {
    Boolean[][] grid = new Boolean[][]
        {
            new Boolean[]{true, false, true, false},
            new Boolean[]{false, true, false, true},
            new Boolean[]{true, true, false, false},
        };
    Boolean[][] transposed = new Boolean[][]
        {
            new Boolean[]{true, false, true},
            new Boolean[]{false, true, true},
            new Boolean[]{true, false, false},
            new Boolean[]{false, true, false},
        };

    Assertions.assertArrayEquals(transposed, DefaultRegionFinder.transposeGrid(grid));
    Assertions.assertArrayEquals(grid, DefaultRegionFinder.transposeGrid(transposed));
    Assertions.assertArrayEquals(grid, DefaultRegionFinder.transposeGrid(DefaultRegionFinder.transposeGrid(grid)));
  }
}
