package de.dhbw.swe.betterjeweled.core;

import com.google.common.eventbus.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;

import java.util.*;
import java.util.stream.*;

class BusPlayerConnectorTest
{
  private static List<Player> players = new LinkedList<>();

  private EventBus eventBus;
  private List<Thread> threads;
  private List<BusPlayerConnector> connectors;

  @BeforeAll
  static void setupClass()
  {
    Player playerOne = Mockito.mock(Player.class);
    Mockito.when(playerOne.getNextMove()).thenReturn(new Move(0,0,0,1, playerOne));
    Player playerTwo = Mockito.mock(Player.class);
    Mockito.when(playerTwo.getNextMove()).thenReturn(new Move(1,1,1,2, playerTwo));

    players.add(playerOne);
    players.add(playerTwo);
  }

  @BeforeEach
  void setupTest()
  {
    eventBus = new EventBus();
    players.forEach(eventBus::register);
    connectors = players.stream()
        .map(player -> new BusPlayerConnector(player, eventBus))
        .collect(Collectors.toList());
    threads = connectors.stream()
        .map(Thread::new)
        .peek(Thread::start)
        .collect(Collectors.toList());
  }

  @Test
  void testAcceptEvent()
  {
    CrystalEvent event = new CrystalEvent.Move(new Crystal[0][0], new Crystal[0][0]);
    eventBus.post(event);

    for(Player player : players)
    {
      Mockito.verify(player, Mockito.times(1)).acceptChangeEvent(event);
    }
  }

  @ParameterizedTest
  @ValueSource(ints={0, 1})
  void testCancel(int x)
  {
    Assertions.assertTrue(connectors.get(x).getRunning().get());
    Mockito.verify(players.get(x), Mockito.atLeastOnce()).getNextMove();

    Assertions.assertTrue(connectors.get(x).cancel());

    Assertions.assertTrue(connectors.get(x).getCancelled().get());
    Mockito.clearInvocations(players.get(x));
    Assertions.assertFalse(connectors.get(x).getRunning().get());
    Mockito.verifyNoMoreInteractions(players.get(x));
  }

  @AfterEach
  void tearDownTest()
  {
    threads.forEach(Thread::stop);
  }
}