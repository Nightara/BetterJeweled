package de.dhbw.swe.betterjeweled.core;

import lombok.Value;

@Value
public class Game {

    CrystalGrid grid;
    RegionFinder finder;
    RegionScorer scorer;

    public boolean makeMove(int x1, int y1, int x2, int y2)
    {
        if (!grid.switchCrystals(x1, y1, x2, y2))
        {
            return false;
        }
        updateLoop();
        return true;
    }

    private void updateLoop()
    {
        while (grid.triggerRegions(finder, scorer) > 0)
        {
            grid.shiftCrystals();
            grid.fillGrid();
        }
    }

}
