package de.dhbw.swe.betterjeweled.impl;

import com.google.common.eventbus.*;
import de.dhbw.swe.betterjeweled.core.*;
import lombok.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;
import java.util.stream.*;

@SuppressWarnings("UnstableApiUsage")
class BusPlayerAdapterTest
{
  private Player playerOne;
  private Player playerTwo;

  private EventBus eventBus;
  private List<BusPlayerAdapter> adapters;

  @BeforeEach
  void setupTest()
  {
    playerOne = Mockito.mock(Player.class);
    Mockito.when(playerOne.getNextMove()).thenReturn(new Move(0,0,0,1, playerOne));
    playerTwo = Mockito.mock(Player.class);
    Mockito.when(playerTwo.getNextMove()).thenReturn(new Move(1,1,1,2, playerTwo));

    eventBus = new EventBus();
    adapters = Stream.of(playerOne, playerTwo)
        .map(player -> new BusPlayerAdapter(player, eventBus))
        .collect(Collectors.toList());
    adapters.forEach(Thread::start);
  }

  @Test
  void testAcceptEvent()
  {
    CrystalEvent event = new CrystalEvent.Move(new Crystal[0][0], new Crystal[0][0]);
    eventBus.post(event);

    Mockito.verify(playerOne).handleChangeEvent(event);
    Mockito.verify(playerTwo).handleChangeEvent(event);
  }

  @Test
  void testCancel()
  {
    adapters.forEach(adapter ->
    {
      Assertions.assertTrue(adapter.cancel());

      Assertions.assertTrue(adapter.getCancelled().get());
      Assertions.assertFalse(adapter.getRunning().get());
    });
  }
}