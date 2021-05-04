package de.dhbw.swe.betterjeweled.impl;

import de.dhbw.swe.betterjeweled.core.*;

import java.util.*;

/**
 * The default implementation of the MoveExecutor interface.
 * A supplied move is first checked for validity against the supplied grid, and if the check returns <code>true</code>,
 * the following actions will be executed in order, each releasing a matching CrystalEvent:
 * - Execute the Move.
 * - Trigger the CrystalGrid.
 * - Shift the CrystalGrid.
 * - Refill the CrystalGrid.
 * - End the current turn.
 */
public class DefaultMoveExecutor implements MoveExecutor
{
  @Override
  public List<GameUpdate> executeMove(CrystalGrid grid, CombinationFinder finder, CombinationScorer scorer, CrystalPair move)
  {
    List<GameUpdate> updates = new LinkedList<>();
    Crystal[][] oldGrid = grid.viewGrid();
    if(grid.switchNeighbors(move))
    {
      Crystal[][] moveGrid = grid.viewGrid();
      updates.add(new GameUpdate.Move(oldGrid, moveGrid));

      int scoreDelta = grid.triggerRegions(finder, scorer);
      Crystal[][] triggerGrid = grid.viewGrid();
      updates.add(new GameUpdate.Trigger(scoreDelta, moveGrid, triggerGrid));

      grid.shiftCrystals();
      Crystal[][] shiftGrid = grid.viewGrid();
      updates.add(new GameUpdate.Shift(triggerGrid, shiftGrid));

      grid.fillGrid();
      Crystal[][] newGrid = grid.viewGrid();
      updates.add(new GameUpdate.Fill(shiftGrid, newGrid));

      updates.add(new GameUpdate.TurnEnd());
    }

    return updates;
  }
}
