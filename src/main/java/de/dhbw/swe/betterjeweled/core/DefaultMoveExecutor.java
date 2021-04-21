package de.dhbw.swe.betterjeweled.core;

import java.util.*;

public class DefaultMoveExecutor implements MoveExecutor
{
  @Override
  public List<CrystalEvent> executeMove(CrystalGrid grid, RegionFinder finder, RegionScorer scorer, Move move)
  {
    List<CrystalEvent> events = new LinkedList<>();
    Crystal[][] oldGrid = grid.viewField();
    if(grid.switchCrystals(move))
    {
      Crystal[][] moveGrid = grid.viewField();
      events.add(new CrystalEvent.Move(oldGrid, moveGrid));

      int scoreDelta = grid.triggerRegions(finder, scorer);
      Crystal[][] triggerGrid = grid.viewField();
      events.add(new CrystalEvent.Trigger(scoreDelta, moveGrid, triggerGrid));

      grid.shiftCrystals();
      Crystal[][] shiftGrid = grid.viewField();
      events.add(new CrystalEvent.Shift(triggerGrid, shiftGrid));

      grid.fillGrid();
      Crystal[][] newGrid = grid.viewField();
      events.add(new CrystalEvent.Fill(shiftGrid, newGrid));
    }

    return events;
  }
}
