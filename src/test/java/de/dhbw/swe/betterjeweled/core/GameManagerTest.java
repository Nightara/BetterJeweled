package de.dhbw.swe.betterjeweled.core;

import com.google.common.eventbus.*;
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

  private CrystalGrid grid;
  private GameManager manager;

  @BeforeAll
  static void setupClass()
  {
    playerOne = Mockito.mock(Player.class);
    Mockito.when(playerOne.getNextMove()).thenReturn(new Move(0,0,0,1));
    playerTwo = Mockito.mock(Player.class);
    Mockito.when(playerTwo.getNextMove()).thenReturn(new Move(1,1,1,2));
    playerThree = Mockito.mock(Player.class);
  }

  @BeforeEach
  void setupTest()
  {
    grid = new CrystalGrid(5,5, RED, GREEN, BLUE);
    grid.fillGrid();

    manager = new GameManager(grid, new DefaultRegionFinder(), new DefaultRegionScorer(), new DefaultPlayerRotator(),
        new DefaultMoveExecutor(), playerOne, playerTwo, playerThree);
  }

  @Test
  void testRunGameCycle()
  {
    manager.getEventBus().register(new Object()
    {
      @Subscribe
      public void registerEvent(CrystalEvent changeEvent)
      {
        switch(changeEvent.getModifierType())
        {
          case MOVE:
            Assertions.assertNotEquals(changeEvent.getUpdatedGrid(), changeEvent.getOldGrid());
          default:
            System.out.println(changeEvent);
        }
      }
    });

    manager.runGameCycle();
    manager.runGameCycle();
  }
}