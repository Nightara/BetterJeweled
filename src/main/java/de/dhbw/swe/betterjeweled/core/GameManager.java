package de.dhbw.swe.betterjeweled.core;

import com.google.common.eventbus.*;
import lombok.*;

import java.util.*;

@Value
@Builder
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class GameManager
{
  EventBus eventBus;
  CrystalGrid grid;
  RegionFinder finder;
  RegionScorer scorer;
  PlayerRotator rotator;
  MoveExecutor executor;
  List<Player> players;

  public GameManager(CrystalGrid grid, RegionFinder finder, RegionScorer scorer, PlayerRotator rotator,
                     MoveExecutor executor, Player... players)
  {
    this(new EventBus(), grid, finder, scorer, rotator, executor, Arrays.asList(players));
    Arrays.stream(players).forEach(this.eventBus::register);
  }

  // TODO: Use event bus to poll moves, not blocking operations
  protected void runGameCycle()
  {
    List<CrystalEvent> events = getExecutor().executeMove(getGrid(),getFinder(),getScorer(),
        getRotator().apply(getPlayers()).getNextMove());
    events.forEach(getEventBus()::post);
  }
}
