package de.dhbw.swe.betterjeweled.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;

public class GameTest {

    private static final Crystal RED = new Crystal(Color.RED);
    private static final Crystal GREEN = new Crystal(Color.GREEN);
    private static final Crystal BLUE = new Crystal(Color.BLUE);

    private CrystalGrid triggerGridWithOverlap()
    {
        CrystalGrid grid = new CrystalGrid(3, 3, RED, GREEN, BLUE);
        grid.setCrystal(0,0, RED);
        grid.setCrystal(0,1, RED);
        grid.setCrystal(0,2, RED);

        grid.setCrystal(1,0, GREEN);
        grid.setCrystal(1,1, BLUE);
        grid.setCrystal(1,2, BLUE);

        grid.setCrystal(2,0, GREEN);
        grid.setCrystal(2,1, BLUE);
        grid.setCrystal(2,2, RED);

        return grid;
    }

    @Test
    void canMakeLegalMove() {
        Game game = new Game(triggerGridWithOverlap(), new MergedRegionFinder(new DefaultRegionFinder()), new DefaultRegionScorer());
        assertTrue(game.makeMove(0, 0, 0, 1));
        assertTrue(game.makeMove(0, 0, 1, 0));
    }

    @Test
    void cantMakeIllegalMove() {
        Game game = new Game(triggerGridWithOverlap(), new MergedRegionFinder(new DefaultRegionFinder()), new DefaultRegionScorer());
        assertFalse(game.makeMove(0, 0, 0, 2));
        assertFalse(game.makeMove(0, 0, 2, 0));
        assertFalse(game.makeMove(0, 0, 2, 2));
    }


}
