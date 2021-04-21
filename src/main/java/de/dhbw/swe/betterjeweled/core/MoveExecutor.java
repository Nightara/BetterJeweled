package de.dhbw.swe.betterjeweled.core;

import java.util.*;

public interface MoveExecutor
{
  List<CrystalEvent> executeMove(CrystalGrid grid, RegionFinder finder, RegionScorer scorer, Move move);
}
