package de.dhbw.swe.betterjeweled.impl;

import de.dhbw.swe.betterjeweled.core.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

class DefaultPlayerRotatorTest
{
  private static Player playerOne;
  private static Player playerTwo;

  private PlayerRotator rotator;

  @BeforeAll
  static void setupClass()
  {
    playerOne = Mockito.mock(Player.class);
    playerTwo = Mockito.mock(Player.class);
  }

  @BeforeEach
  void setupTest()
  {
    rotator = new DefaultPlayerRotator(playerOne, playerTwo);
  }

  @Test
  void testReset()
  {
    Assertions.assertEquals(rotator.reset(), playerOne);
  }

  @Test
  void testNextPlayer()
  {
    Assertions.assertNotEquals(rotator.nextPlayer(), rotator.nextPlayer());
  }

  @Test
  void testNextPlayerLoops()
  {
    for(int x = 0; x < 100; x++)
    {
      Assertions.assertNotNull(rotator.nextPlayer());
    }
  }

  @Test
  void testPeek()
  {
    Assertions.assertEquals(rotator.peek(), rotator.peek());
    rotator.nextPlayer();
    Assertions.assertEquals(rotator.peek(), rotator.peek());
  }

  @Test
  void testPeekEqualsNext()
  {
    Assertions.assertEquals(rotator.peek(), rotator.nextPlayer());
    Assertions.assertEquals(rotator.peek(), rotator.nextPlayer());
  }
}