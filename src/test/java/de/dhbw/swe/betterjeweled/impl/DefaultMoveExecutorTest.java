package de.dhbw.swe.betterjeweled.impl;

import de.dhbw.swe.betterjeweled.core.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.awt.*;
import java.util.*;
import java.util.List;

class DefaultMoveExecutorTest
{
  private static final Crystal RED = new Crystal(Color.RED);
  private static final Crystal GREEN = new Crystal(Color.GREEN);
  private static final Crystal BLUE = new Crystal(Color.BLUE);

  private static CombinationFinder finder;
  private static CombinationScorer scorer;
  private static MoveExecutor executor;

  private CrystalGrid grid;

  @BeforeAll
  static void setupClass()
  {
    executor = new DefaultMoveExecutor();
    finder = Mockito.mock(CombinationFinder.class);
    Mockito.when(finder.findRegions(Mockito.any(CrystalGrid.class), Mockito.any(Crystal[].class)))
        .thenReturn(new HashMap<>());
    scorer = Mockito.mock(CombinationScorer.class);
    Mockito.when(scorer.scoreRegion(Mockito.any(CrystalCombination.class))).thenReturn(0);
  }

  @BeforeEach
  void setupTest()
  {
    grid = new CrystalGrid(5, 5, RED, GREEN, BLUE);
    grid.fillGrid();
  }

  @Test
  void testExecuteLegalMove()
  {
    CrystalPair legalCrystalPair = new CrystalPair(0,0,0,1,null);

    List<GameUpdate> events = executor.executeMove(grid, finder, scorer, legalCrystalPair);

    Assertions.assertEquals(5, events.size());
    Assertions.assertEquals(GameUpdate.Move.class, events.get(0).getClass());
    Assertions.assertEquals(GameUpdate.Trigger.class, events.get(1).getClass());
    Assertions.assertEquals(GameUpdate.Shift.class, events.get(2).getClass());
    Assertions.assertEquals(GameUpdate.Fill.class, events.get(3).getClass());
  }

  @Test
  void testExecuteIllegalMove()
  {
    CrystalPair illegalCrystalPair = new CrystalPair(0,0,1,1,null);
    Crystal[][] oldGrid = grid.viewGrid();

    List<GameUpdate> events = executor.executeMove(grid, finder, scorer, illegalCrystalPair);

    Assertions.assertEquals(0, events.size());
    Assertions.assertArrayEquals(oldGrid, grid.viewGrid());
  }
}