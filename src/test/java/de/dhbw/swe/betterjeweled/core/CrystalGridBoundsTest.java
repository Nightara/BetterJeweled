package de.dhbw.swe.betterjeweled.core;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CrystalGridBoundsTest {

    private static final Crystal RED = new Crystal(Color.RED);
    private static final Crystal GREEN = new Crystal(Color.GREEN);
    private static final Crystal BLUE = new Crystal(Color.BLUE);

    @Test
    void numberOfRowsCannotBeLessThanMinimumComboLength() {
        int rows = CrystalGrid.MIN_COMBO_SIZE - 1;
        while(rows >= 0) {
            int finalRows = rows;
            assertThrows(IllegalArgumentException.class, () -> {new CrystalGrid(1, finalRows);});
            rows--;
        }
    }

    @Test
    void numberOfColumnsCannotBeLessThanMinimumComboLength() {
        int cols = CrystalGrid.MIN_COMBO_SIZE - 1;
        while(cols >= 0) {
            int finalCols = cols;
            assertThrows(IllegalArgumentException.class, () -> {new CrystalGrid(1, finalCols);});
            cols--;
        }
    }
    @Test
    void getCrystalOutOfBounds()
    {
        int sizeX = 3;
        int sizeY = 5;
        CrystalGrid grid = new CrystalGrid(sizeX, sizeY);

        assertDoesNotThrow(() -> grid.getCrystal(0,0));
        assertDoesNotThrow(() -> grid.getCrystal(1,4));
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

        assertThrows(IndexOutOfBoundsException.class, () -> grid.switchCrystals(0,-1,0,0));
        assertThrows(IndexOutOfBoundsException.class, () -> grid.switchCrystals(2,4,3,4));
        assertThrows(IndexOutOfBoundsException.class, () -> grid.switchCrystals(-1,-1,-1,-2));
    }
}
