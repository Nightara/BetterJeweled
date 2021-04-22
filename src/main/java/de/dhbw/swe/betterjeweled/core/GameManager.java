package de.dhbw.swe.betterjeweled.core;

import com.google.common.eventbus.*;
import lombok.*;
import lombok.experimental.*;

import java.util.*;
import java.util.stream.*;

@Value
@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
@SuppressWarnings("UnstableApiUsage")
public class GameManager
{
  EventBus eventBus;
  CrystalGrid grid;
  RegionFinder finder;
  RegionScorer scorer;
  PlayerRotator rotator;
  MoveExecutor executor;
  List<BusPlayerAdapter> players;

  @NonFinal
  @Setter(AccessLevel.PRIVATE)
  boolean listening = false;

  @SuppressWarnings("java:S2234")
  public GameManager(CrystalGrid grid, RegionFinder finder, RegionScorer scorer, PlayerRotator rotator,
                     MoveExecutor executor, Player... players)
  {
    this(new EventBus(), grid, finder, scorer, rotator, executor, new LinkedList<>());
    Arrays.stream(players)
        .map(player -> new BusPlayerAdapter(player, this.eventBus))
        .forEach(this.players::add);
    getPlayers().forEach(Thread::start);
  }

  public void awaitNextMove()
  {
    setListening(true);
    getEventBus().register(this);
  }

  @Subscribe
  private void acceptMove(Move move)
  {
    try
    {
      if(isListening() && move.getSource() == getRotator().peek())
      {
        getExecutor().executeMove(getGrid(), getFinder(), getScorer(), move)
            .forEach(getEventBus()::post);
        setListening(false);
        getEventBus().unregister(this);
      }
    }
    catch(Exception ex)
    {ex.printStackTrace();}
  }
}
