package de.dhbw.swe.betterjeweled.core;

import java.util.*;

public interface MoveExecutor
{
  List<GameUpdate> executeMove(CrystalGrid grid, CombinationFinder finder, CombinationScorer scorer, CrystalPair crystalPair);
}
