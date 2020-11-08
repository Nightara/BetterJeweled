package de.dhbw.swe.betterjeweled.core;

import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CrystalGridTest
{
  private static final Crystal RED = new Crystal(Color.RED);
  private static final Crystal GREEN = new Crystal(Color.GREEN);
  private static final Crystal BLUE = new Crystal(Color.BLUE);

  @Test
  void generateEmptyGrid()
  {
    int sizeX = 3;
    int sizeY = 3;
    int size = sizeX * sizeY;

    CrystalGrid grid = new CrystalGrid(sizeX, sizeY);

    assertEquals(size, grid.getSize());
    assertTrue(Arrays.stream(grid.getField())
        .flatMap(Arrays::stream)
        .allMatch(Objects::isNull));
  }

  @Test
  void fillEmptyGrid()
  {
    int sizeX = 3;
    int sizeY = 5;
    int size = sizeX * sizeY;
    CrystalGrid grid = new CrystalGrid(sizeX, sizeY, RED, GREEN, BLUE);

    assertEquals(size, grid.fillGrid());
    assertTrue(Arrays.stream(grid.getField())
        .flatMap(Arrays::stream)
        .noneMatch(Objects::isNull));
  }

  @Test
  void fillGridOnlyOnce()
  {
    CrystalGrid grid = new CrystalGrid(3, 5, RED, GREEN, BLUE);
    grid.fillGrid();

    assertEquals(0, grid.fillGrid());
  }

  @Test
  void fillGridWithSeed()
  {
    int seed = new Random().nextInt();
    int sizeX = 3;
    int sizeY = 5;
    CrystalGrid gridOne = new CrystalGrid(sizeX, sizeY, seed, RED, GREEN, BLUE);
    CrystalGrid gridTwo = new CrystalGrid(sizeX, sizeY, seed, RED, GREEN, BLUE);

    gridOne.fillGrid();
    gridTwo.fillGrid();

    assertEquals(gridOne, gridTwo);
  }

  @Test
  void switchCrystals()
  {
    int sizeX = 3;
    int sizeY = 5;
    int posXOne = 0;
    int posYOne = 0;
    int posXTwo = 0;
    int posYTwo = 1;
    CrystalGrid grid = new CrystalGrid(sizeX, sizeY, RED, GREEN, BLUE);
    grid.fillGrid();

    Optional<Crystal> crysOne = grid.getCrystal(posXOne, posYOne);
    Optional<Crystal> crysTwo = grid.getCrystal(posXTwo, posYTwo);
    assertTrue(grid.switchCrystals(posXOne, posYOne, posXTwo, posYTwo));

    assertEquals(crysTwo, grid.getCrystal(posXOne, posYOne));
    assertEquals(crysOne, grid.getCrystal(posXTwo, posYTwo));
  }

  @Test
  void dontSwitchRemoteCrystals()
  {
    CrystalGrid grid = new CrystalGrid(3, 5, RED, GREEN, BLUE);
    grid.fillGrid();

    assertFalse(grid.switchCrystals(0,0,0,2));
    assertFalse(grid.switchCrystals(0,0,2,0));
    assertFalse(grid.switchCrystals(0,0,2,2));
  }

  @Test
  void dontSwitchCrystalWithItself()
  {
    CrystalGrid grid = new CrystalGrid(3, 5, RED, GREEN, BLUE);
    grid.fillGrid();

    assertFalse(grid.switchCrystals(0,0,0,0));
  }

  private CrystalGrid setupTriggerGrid()
  {
    CrystalGrid grid = new CrystalGrid(3, 5, RED, GREEN, BLUE);
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

    grid.setCrystal(2,0, GREEN);
    grid.setCrystal(2,1, BLUE);
    grid.setCrystal(2,2, RED);
    grid.setCrystal(2,3, RED);
    grid.setCrystal(2,4, GREEN);

    return grid;
  }

  @Test
  void findRegions()
  {
    CrystalGrid grid = setupTriggerGrid();

    Map<Crystal, List<CrystalRegion>> regions = grid.findRegions();

    assertEquals(3, regions.keySet().size());
    assertEquals(1, regions.get(RED).size());
    assertEquals(1, regions.get(GREEN).size());
    assertEquals(1, regions.get(BLUE).size());
  }

  @Test
  void triggerRegions()
  {
    CrystalGrid originalGrid = setupTriggerGrid();
    CrystalGrid grid = setupTriggerGrid();
    Map<Crystal, List<CrystalRegion>> regions = grid.findRegions();

    assertTrue(grid.triggerRegions() > 0);

    for(int x = 0; x < grid.getSizeX(); x++)
    {
      for(int y = 0; y < grid.getSizeY(); y++)
      {
        int finalX = x;
        int finalY = y;
        boolean shouldBePresent = originalGrid.getCrystal(x, y)
            .map(crystal -> regions.get(crystal).stream()
                .noneMatch(region -> region.contains(finalX, finalY)))
            .orElse(false);

        assertEquals(shouldBePresent, grid.getCrystal(x, y).isPresent(),
            "[" + x + ", " + y + "] should be " + (shouldBePresent ? originalGrid.getCrystal(x, y) : "Optional.empty")
                + " but was " + grid.getCrystal(x, y));
      }
    }
  }

  @Test
  void partialRefill()
  {
    CrystalGrid grid = setupTriggerGrid();
    grid.triggerRegions();
    long expectedRefill = grid.getSize() - Arrays.stream(grid.getField())
        .flatMap(Arrays::stream)
        .filter(Objects::nonNull)
        .count();

    assertEquals(expectedRefill, grid.fillGrid());
    assertTrue(Arrays.stream(grid.getField())
        .flatMap(Arrays::stream)
        .noneMatch(Objects::isNull));
  }

  @Test
  void scoreRegion()
  {
    CrystalRegion r1_1 = new CrystalRegion(0,0,1,1);
    CrystalRegion r1_2 = new CrystalRegion(3,2,4,3);
    CrystalRegion r2_1 = new CrystalRegion(0,0,2,1);
    CrystalRegion r3_1 = new CrystalRegion(3,2,6,3);
    CrystalRegion r3_2 = new CrystalRegion(3,2,4,5);
    CrystalRegion r4_1 = new CrystalRegion(1,0,5,1);
    CrystalRegion r5_1 = new CrystalRegion(8,7,13,8);

    assertEquals(CrystalGrid.scoreRegion(r1_1),CrystalGrid.scoreRegion(r1_2));
    assertEquals(CrystalGrid.scoreRegion(r3_1),CrystalGrid.scoreRegion(r3_2));

    assertEquals(0, CrystalGrid.scoreRegion(r1_1));
    assertEquals(0, CrystalGrid.scoreRegion(r2_1));

    assertTrue(CrystalGrid.scoreRegion(r4_1) > CrystalGrid.scoreRegion(r3_1));
    assertTrue(CrystalGrid.scoreRegion(r5_1) > CrystalGrid.scoreRegion(r4_1));

    assertTrue(CrystalGrid.scoreRegion(r4_1) > CrystalGrid.scoreRegion(r3_1) + CrystalGrid.scoreRegion(r3_2));
  }

  @Test
  void shiftGrid()
  {
    CrystalGrid grid = setupTriggerGrid();
    grid.triggerRegions();
    long emptyFields = Arrays.stream(grid.getField())
        .flatMap(Arrays::stream)
        .filter(Objects::isNull)
        .count();

    grid.shiftCrystals();

    assertEquals(emptyFields, Arrays.stream(grid.getField())
        .flatMap(Arrays::stream)
        .filter(Objects::isNull)
        .count());

    for(int x = 0; x < grid.getSizeX(); x++)
    {
      for(int y = 0; y < grid.getSizeY() - 1; y++)
      {
        // [x, y + 1] is empty => [x, y] is empty
        assertTrue(grid.getCrystal(x,y + 1).isPresent() || grid.getCrystal(x, y).isEmpty(),
            "[" + x + ", " + (y + 1) + "] is empty, but [" + x + ", " + y + "] isn't.");
      }
    }
  }
}