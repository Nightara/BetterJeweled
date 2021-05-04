package de.dhbw.swe.betterjeweled.impl;

import de.dhbw.swe.betterjeweled.core.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

class DefaultPlayerProviderTest
{
  private static Player playerOne;
  private static Player playerTwo;

  private PlayerProvider provider;

  @BeforeAll
  static void setupClass()
  {
    playerOne = Mockito.mock(Player.class);
    playerTwo = Mockito.mock(Player.class);
  }

  @BeforeEach
  void setupTest()
  {
    provider = new DefaultPlayerProvider(playerOne, playerTwo);
  }

  @Test
  void testReset()
  {
    Assertions.assertEquals(provider.reset(), playerOne);
  }

  @Test
  void testNextPlayer()
  {
    Assertions.assertNotEquals(provider.nextPlayer(), provider.nextPlayer());
  }

  @Test
  void testNextPlayerLoops()
  {
    for(int x = 0; x < 100; x++)
    {
      Assertions.assertNotNull(provider.nextPlayer());
    }
  }

  @Test
  void testPeek()
  {
    Assertions.assertEquals(provider.peek(), provider.peek());
    provider.nextPlayer();
    Assertions.assertEquals(provider.peek(), provider.peek());
  }

  @Test
  void testPeekEqualsNext()
  {
    Assertions.assertEquals(provider.peek(), provider.nextPlayer());
    Assertions.assertEquals(provider.peek(), provider.nextPlayer());
  }
}