package de.dhbw.swe.betterjeweled.impl;

import de.dhbw.swe.betterjeweled.core.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.awt.*;
import java.util.*;
import java.util.List;

class GameManagerTest
{
  private static final Crystal RED = new Crystal(Color.RED);
  private static final Crystal GREEN = new Crystal(Color.GREEN);
  private static final Crystal BLUE = new Crystal(Color.BLUE);

  private Player playerOne;
  private Player playerTwo;
  private Player playerThree;
  private GameManager manager;

  @BeforeEach
  void setupTest()
  {
    List<GameUpdate> updates = new LinkedList<>();
    updates.add(new GameUpdate.Move(new Crystal[0][0], new Crystal[0][0]));
    updates.add(new GameUpdate.Trigger(0, new Crystal[0][0], new Crystal[0][0]));
    updates.add(new GameUpdate.Shift(new Crystal[0][0], new Crystal[0][0]));
    updates.add(new GameUpdate.Fill(new Crystal[0][0], new Crystal[0][0]));
    updates.add(new GameUpdate.TurnEnd());

    playerOne = Mockito.mock(Player.class);
    Mockito.when(playerOne.getNextMove()).thenReturn(new CrystalPair(0,0,0,1, playerOne));
    playerTwo = Mockito.mock(Player.class);
    Mockito.when(playerTwo.getNextMove()).thenReturn(new CrystalPair(1,1,1,2, playerTwo));
    playerThree = Mockito.mock(Player.class);
    CombinationFinder finder = Mockito.mock(CombinationFinder.class);
    Mockito.when(finder.findRegions(Mockito.any(CrystalGrid.class), Mockito.any(Crystal[].class)))
        .thenReturn(new HashMap<>());
    CombinationScorer scorer = Mockito.mock(CombinationScorer.class);
    Mockito.when(scorer.scoreRegion(Mockito.any(CrystalCombination.class))).thenReturn(0);
    PlayerProvider provider = Mockito.mock(PlayerProvider.class);
    Mockito.when(provider.peek()).thenReturn(playerOne);
    MoveExecutor executor = Mockito.mock(MoveExecutor.class);
    Mockito.when(executor.executeMove(Mockito.any(CrystalGrid.class), Mockito.eq(finder), Mockito.eq(scorer),
        Mockito.any(CrystalPair.class)))
        .thenReturn(updates);

    CrystalGrid grid = new CrystalGrid(5,5, RED, GREEN, BLUE);
    grid.fillGrid();

    manager = new GameManager(grid, finder, scorer, provider, executor,false,
        playerOne, playerTwo, playerThree);
  }

  @Test
  @SuppressWarnings("UnstableApiUsage")
  void testAwaitNextMove()
  {
    manager.awaitNextMove();
    manager.getEventBus().post(playerOne.getNextMove());

    Mockito.verify(playerThree, Mockito.times(5)).handleGameUpdate(Mockito.any(GameUpdate.class));
  }

  @Test
  @SuppressWarnings("UnstableApiUsage")
  void testUnregister()
  {
    manager.awaitNextMove();
    manager.getEventBus().post(new GameUpdate.TurnEnd());

    Assertions.assertFalse(manager.isListening());

    manager.getEventBus().post(playerOne.getNextMove());
    Mockito.verify(playerTwo).handleGameUpdate(Mockito.any(GameUpdate.class));
  }

  @Test
  @SuppressWarnings("UnstableApiUsage")
  void testRotatePlayer()
  {
    manager.awaitNextMove();
    manager.getEventBus().post(new GameUpdate.TurnEnd());

    Mockito.verify(manager.getProvider()).nextPlayer();
  }
}