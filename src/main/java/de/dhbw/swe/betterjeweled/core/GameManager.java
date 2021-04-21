package de.dhbw.swe.betterjeweled.core;

import com.google.common.eventbus.*;
import lombok.*;
import lombok.experimental.*;

@Value
@Builder
@AllArgsConstructor
public class GameManager
{
  EventBus bus;
  CrystalGrid grid;
  Player playerOne;
  Player playerTwo;
  RegionFinder finder;
  RegionScorer scorer;

  @Setter
  @NonFinal
  Player nextPlayer;

  private GameManager(CrystalGrid grid, RegionFinder finder, RegionScorer scorer, Player playerOne, Player playerTwo)
  {
    this(new EventBus(), grid, playerOne, playerTwo, finder, scorer, playerOne);
    this.bus.register(playerOne);
    this.bus.register(playerTwo);
  }

  private void runGameCycle()
  {
    getBus().post(getNextPlayer().getNextMove());
    setNextPlayer(getNextPlayer() == getPlayerOne() ? getPlayerTwo() : getPlayerOne());
  }
}
