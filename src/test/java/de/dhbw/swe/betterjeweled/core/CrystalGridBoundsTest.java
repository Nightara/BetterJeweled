package de.dhbw.swe.betterjeweled.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CrystalGridBoundsTest {

    private static final Crystal RED = new Crystal(Color.RED);
    private static final Crystal GREEN = new Crystal(Color.GREEN);
    private static final Crystal BLUE = new Crystal(Color.BLUE);

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void cannotCreateGridWithNonPositiveRows(int row) {
        assertThrows(IllegalArgumentException.class, () -> new CrystalGrid(1, row));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void cannotCreateGridWithNonPositiveColumns(int cols) {
        assertThrows(IllegalArgumentException.class, () -> new CrystalGrid(cols, 1));
    }

    @Test
    void getCrystalOutOfBounds()
    {
        int sizeX = 2;
        int sizeY = 5;
        CrystalGrid grid = new CrystalGrid(sizeX, sizeY);

        assertDoesNotThrow(() -> grid.getCrystal(0,0));
        assertDoesNotThrow(() -> grid.getCrystal(1,4));
        assertTrue(assertThrows(IndexOutOfBoundsException.class, () -> grid.getCrystal(2, 0))
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
        CrystalGrid grid = new CrystalGrid(2, 5, RED, GREEN, BLUE);
        grid.fillGrid();

        assertThrows(IndexOutOfBoundsException.class, () -> grid.switchCrystals(0,-1,0,0));
        assertThrows(IndexOutOfBoundsException.class, () -> grid.switchCrystals(1,4,2,4));
        assertThrows(IndexOutOfBoundsException.class, () -> grid.switchCrystals(-1,-1,-1,-2));
    }
}
