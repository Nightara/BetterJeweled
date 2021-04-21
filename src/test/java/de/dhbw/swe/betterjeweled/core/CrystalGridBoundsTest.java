package de.dhbw.swe.betterjeweled.core;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CrystalGridBoundsTest
{
  private static final Crystal RED = new Crystal(Color.RED);
  private static final Crystal GREEN = new Crystal(Color.GREEN);
  private static final Crystal BLUE = new Crystal(Color.BLUE);

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2})
  void minimumGridHeight(int sizeY)
  {
    assertThrows(IllegalArgumentException.class, () -> new CrystalGrid(3, sizeY));
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2})
  void minimumGridWidth(int sizeX)
  {
    assertThrows(IllegalArgumentException.class, () -> new CrystalGrid(sizeX, 3));
  }

  @Test
  void getCrystalOutOfBounds()
  {
    int sizeX = 3;
    int sizeY = 5;
    CrystalGrid grid = new CrystalGrid(sizeX, sizeY);

    assertTrue(assertThrows(IndexOutOfBoundsException.class, () -> grid.getCrystal(3, 0))
        .getMessage().contains("[" + sizeX + ", " + sizeY + "]"));
    assertTrue(assertThrows(IndexOutOfBoundsException.class, () -> grid.getCrystal(0, 5))
        .getMessage().contains("[" + sizeX + ", " + sizeY + "]"));
    assertTrue(assertThrows(IndexOutOfBoundsException.class, () -> grid.getCrystal(-1, 0))
        .getMessage().contains("[" + sizeX + ", " + sizeY + "]"));
    assertTrue(assertThrows(IndexOutOfBoundsException.class, () -> grid.getCrystal(0, -1))
        .getMessage().contains("[" + sizeX + ", " + sizeY + "]"));
  }

  @Test
  void switchCrystalsOutOfBounds()
  {
    CrystalGrid grid = new CrystalGrid(3, 5, RED, GREEN, BLUE);
    grid.fillGrid();

    assertThrows(IndexOutOfBoundsException.class, () -> grid.switchCrystals(0, -1, 0, 0));
    assertThrows(IndexOutOfBoundsException.class, () -> grid.switchCrystals(2, 4, 3, 4));
    assertThrows(IndexOutOfBoundsException.class, () -> grid.switchCrystals(-1, -1, -1, -2));
  }
}
