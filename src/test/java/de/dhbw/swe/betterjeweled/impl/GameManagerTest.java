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
    List<CrystalEvent> events = new LinkedList<>();
    events.add(new CrystalEvent.Move(new Crystal[0][0], new Crystal[0][0]));
    events.add(new CrystalEvent.Trigger(0, new Crystal[0][0], new Crystal[0][0]));
    events.add(new CrystalEvent.Shift(new Crystal[0][0], new Crystal[0][0]));
    events.add(new CrystalEvent.Fill(new Crystal[0][0], new Crystal[0][0]));
    events.add(new CrystalEvent.TurnEnd());

    playerOne = Mockito.mock(Player.class);
    Mockito.when(playerOne.getNextMove()).thenReturn(new Move(0,0,0,1, playerOne));
    playerTwo = Mockito.mock(Player.class);
    Mockito.when(playerTwo.getNextMove()).thenReturn(new Move(1,1,1,2, playerTwo));
    playerThree = Mockito.mock(Player.class);
    RegionFinder finder = Mockito.mock(RegionFinder.class);
    Mockito.when(finder.findRegions(Mockito.any(CrystalGrid.class), Mockito.any(Crystal[].class)))
        .thenReturn(new HashMap<>());
    RegionScorer scorer = Mockito.mock(RegionScorer.class);
    Mockito.when(scorer.scoreRegion(Mockito.any(CrystalRegion.class))).thenReturn(0);
    PlayerRotator rotator = Mockito.mock(PlayerRotator.class);
    Mockito.when(rotator.peek()).thenReturn(playerOne);
    MoveExecutor executor = Mockito.mock(MoveExecutor.class);
    Mockito.when(executor.executeMove(Mockito.any(CrystalGrid.class), Mockito.eq(finder), Mockito.eq(scorer),
        Mockito.any(Move.class)))
        .thenReturn(events);

    CrystalGrid grid = new CrystalGrid(5,5, RED, GREEN, BLUE);
    grid.fillGrid();

    manager = new GameManager(grid, finder, scorer, rotator, executor,false,
        playerOne, playerTwo, playerThree);
  }

  @Test
  @SuppressWarnings("UnstableApiUsage")
  void testAwaitNextMove()
  {
    manager.awaitNextMove();
    manager.getEventBus().post(playerOne.getNextMove());

    Mockito.verify(playerThree, Mockito.times(5)).handleChangeEvent(Mockito.any(CrystalEvent.class));
  }

  @Test
  @SuppressWarnings("UnstableApiUsage")
  void testUnregister()
  {
    manager.awaitNextMove();
    manager.getEventBus().post(new CrystalEvent.TurnEnd());

    Assertions.assertFalse(manager.isListening());

    manager.getEventBus().post(playerOne.getNextMove());
    Mockito.verify(playerTwo).handleChangeEvent(Mockito.any(CrystalEvent.class));
  }

  @Test
  @SuppressWarnings("UnstableApiUsage")
  void testRotatePlayer()
  {
    manager.awaitNextMove();
    manager.getEventBus().post(new CrystalEvent.TurnEnd());

    Mockito.verify(manager.getRotator()).nextPlayer();
  }
}