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
    int posXOne = 2;
    int posYOne = 1;
    CrystalGrid grid = new CrystalGrid(sizeX, sizeY, RED, GREEN, BLUE);
    grid.fillGrid();

    for(int posXTwo = 0; posXTwo < sizeX; posXTwo++)
    {
      for(int posYTwo = 0; posYTwo < sizeY; posYTwo++)
      {
        Optional<Crystal> crysOne = grid.getCrystal(posXOne, posYOne);
        Optional<Crystal> crysTwo = grid.getCrystal(posXTwo, posYTwo);

        if(Math.abs(posXTwo - posXOne) + Math.abs(posYTwo - posYOne) == 1
            && crysOne.isPresent()
            && crysTwo.isPresent())
        {
          assertTrue(grid.switchCrystals(posXOne, posYOne, posXTwo, posYTwo));

          assertEquals(crysTwo, grid.getCrystal(posXOne, posYOne));
          assertEquals(crysOne, grid.getCrystal(posXTwo, posYTwo));
        }
      }
    }
  }

  @Test
  void dontSwitchRemoteCrystals()
  {
    int sizeX = 3;
    int sizeY = 5;
    int posXOne = 2;
    int posYOne = 1;
    CrystalGrid grid = new CrystalGrid(sizeX, sizeY, RED, GREEN, BLUE);
    grid.fillGrid();

    for(int posXTwo = 0; posXTwo < sizeX; posXTwo++)
    {
      for(int posYTwo = 0; posYTwo < sizeY; posYTwo++)
      {
        Optional<Crystal> crysOne = grid.getCrystal(posXOne, posYOne);
        Optional<Crystal> crysTwo = grid.getCrystal(posXTwo, posYTwo);

        if(Math.abs(posXTwo - posXOne) + Math.abs(posYTwo - posYOne) > 1
            && crysOne.isPresent()
            && crysTwo.isPresent())
        {
          assertFalse(grid.switchCrystals(posXOne, posYOne, posXTwo, posYTwo));

          assertEquals(crysOne, grid.getCrystal(posXOne, posYOne));
          assertEquals(crysTwo, grid.getCrystal(posXTwo, posYTwo));
        }
      }
    }
  }

  @Test
  void dontSwitchCrystalWithItself()
  {
    int sizeX = 3;
    int sizeY = 5;
    CrystalGrid grid = new CrystalGrid(sizeX, sizeY, RED, GREEN, BLUE);
    grid.fillGrid();

    for(int posX = 0; posX < sizeX; posX++)
    {
      for(int posY = 0; posY < sizeY; posY++)
      {
        Optional<Crystal> crystal = grid.getCrystal(posX, posY);

        assertFalse(grid.switchCrystals(posX, posY, posX, posY));

        assertEquals(crystal, grid.getCrystal(posX, posY));
        assertEquals(crystal, grid.getCrystal(posX, posY));
      }
    }
  }

  private static Map<Crystal, List<CrystalRegion>> generateTriggerRegions()
  {
    Map<Crystal, List<CrystalRegion>> regions = new HashMap<>();
    regions.put(RED, new LinkedList<>());
    regions.get(RED).add(new CrystalRegion(0,1,1,1));
    regions.get(RED).add(new CrystalRegion(0,2,2,4));

    return regions;
  }

  @Test
  void triggerRegions()
  {
    int sizeX = 3;
    int sizeY = 5;
    int seed = new Random().nextInt();
    CrystalGrid grid = new CrystalGrid(sizeX, sizeY, seed, RED, GREEN, BLUE);
    CrystalGrid originalGrid = new CrystalGrid(sizeX, sizeY, seed, RED, GREEN, BLUE);
    RegionScorer regionScorer = CrystalRegion::getSize;
    RegionFinder regionFinder = (grid1, crystals) -> generateTriggerRegions();

    assertTrue(grid.triggerRegions(regionFinder, regionScorer) > 0);

    for(int x = 0; x < grid.getSizeX(); x++)
    {
      for(int y = 0; y < grid.getSizeY(); y++)
      {
        int finalX = x;
        int finalY = y;
        boolean shouldBePresent = generateTriggerRegions().values().stream()
            .flatMap(Collection::stream)
            .noneMatch(region -> region.contains(finalX, finalY));
        Optional<Crystal> expected = originalGrid.getCrystal(x, y).filter(c -> shouldBePresent);

        assertEquals(originalGrid.getCrystal(x, y).filter(c -> shouldBePresent), grid.getCrystal(x, y),
            "[" + x + ", " + y + "] should be " + expected + " but was " + grid.getCrystal(x, y));
      }
    }
  }

  @Test
  void partialRefill()
  {
    int sizeX = 3;
    int sizeY = 5;
    CrystalGrid grid = new CrystalGrid(sizeX, sizeY, RED, GREEN, BLUE);
    RegionScorer regionScorer = CrystalRegion::getSize;
    RegionFinder regionFinder = (grid1, crystals) -> generateTriggerRegions();

    grid.triggerRegions(regionFinder, regionScorer);
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
  void shiftGrid()
  {
    int sizeX = 3;
    int sizeY = 5;
    CrystalGrid grid = new CrystalGrid(sizeX, sizeY, RED, GREEN, BLUE);

    RegionScorer regionScorer = CrystalRegion::getSize;
    RegionFinder regionFinder = (grid1, crystals) -> generateTriggerRegions();

    grid.triggerRegions(regionFinder, regionScorer);
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
        // Condition tested: [x, y + 1] is empty => [x, y] is empty
        assertTrue(grid.getCrystal(x,y + 1).isPresent() || grid.getCrystal(x, y).isEmpty(),
            "[" + x + ", " + (y + 1) + "] is empty, but [" + x + ", " + y + "] isn't.");
      }
    }
  }
}