package de.dhbw.swe.betterjeweled.core;

import de.dhbw.swe.betterjeweled.impl.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.awt.*;

class GameManagerTest
{
  private static final Crystal RED = new Crystal(Color.RED);
  private static final Crystal GREEN = new Crystal(Color.GREEN);
  private static final Crystal BLUE = new Crystal(Color.BLUE);

  private static Player playerOne;
  private static Player playerTwo;
  private static Player playerThree;

  private GameManager manager;

  @BeforeAll
  static void setupClass()
  {
    playerOne = Mockito.mock(Player.class);
    Mockito.when(playerOne.getNextMove()).thenReturn(new Move(0,0,0,1, playerOne));
    playerTwo = Mockito.mock(Player.class);
    Mockito.when(playerTwo.getNextMove()).thenReturn(new Move(1,1,1,2, playerTwo));
    playerThree = Mockito.mock(Player.class);
  }

  @BeforeEach
  void setupTest()
  {
    CrystalGrid grid = new CrystalGrid(5,5, RED, GREEN, BLUE);
    grid.fillGrid();

    manager = new GameManager(grid, new DefaultRegionFinder(), new DefaultRegionScorer(),
        new DefaultPlayerRotator(playerOne, playerTwo, playerThree),
        new DefaultMoveExecutor(), playerOne, playerTwo, playerThree);
  }

  @Test
  @SuppressWarnings("UnstableApiUsage")
  void testAwaitNextMove()
  {
    manager.awaitNextMove();
    manager.getEventBus().post(playerOne.getNextMove());

    Mockito.verify(playerThree, Mockito.times(4)).handleChangeEvent(Mockito.any(CrystalEvent.class));
  }
}