package de.dhbw.swe.betterjeweled.impl;

import com.google.common.eventbus.*;
import de.dhbw.swe.betterjeweled.core.*;
import lombok.*;
import lombok.experimental.*;

import java.util.*;
import java.util.function.*;

/**
 * The default game coordinator class utilizing the <code>com.google.common.eventbus</code> package to coordinate
 * messages between Player classes and the board.
 *
 * Supplied Player objects are wrapped into BusPlayerAdapter to connect them
 * to the EventBus without adding a dependency to the Player class. The BusPlayerAdapter is launched immediately and
 * continuously polls <code>Player::getNextMove</code> into the EventBus.
 *
 * The core method of this class is <code>GameManager::awaitNextMove</code>, which registers the coordinator to its
 * EventBus and waits for a move provided by the player determined to be next by the supplied PlayerRotator.
 */
@Value
@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
@SuppressWarnings("UnstableApiUsage")
public class GameManager
{
  // TODO: Add score tracking per player
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

  public GameManager(CrystalGrid grid, RegionFinder finder, RegionScorer scorer, PlayerRotator rotator,
                        MoveExecutor executor, Player... players)
  {
    this(grid, finder, scorer, rotator, executor,true, players);
  }

  @SuppressWarnings("java:S2234")
  protected GameManager(CrystalGrid grid, RegionFinder finder, RegionScorer scorer, PlayerRotator rotator,
                     MoveExecutor executor, boolean autoLaunch, Player... players)
  {
    this(new EventBus(), grid, finder, scorer, rotator, executor, new LinkedList<>());
    Arrays.stream(players)
        .map(player -> new BusPlayerAdapter(player, this.eventBus))
        .forEach(this.players::add);

    if(autoLaunch)
    {
      getPlayers().forEach(Thread::start);
    }
  }

  /**
   * Launches all not running threads.
   * This method is called automatically with the public constructor or when calling the protected constructor with
   * autoLaunch = true.
   */
  // TODO: Test
  protected void launchThreads()
  {
    getPlayers().stream()
        .filter(Predicate.not(Thread::isAlive))
        .forEach(Thread::start);
  }

  /**
   * The core method of GameManager.
   * The GameManager is registered to its GameManager as listener, waiting for the next Move to be posted on the bus.
   *
   * Any Moves on the EventBus will be passed to GameManager::acceptMove, which in turn compares its
   * <code>Move::getSource</code> to the player supplied by the internal PlayerRotator. Any Move objects not matching
   * will be discarded.
   *
   * The emission of a CrystalEvent.TurnEnd will unregister the GameManager from the EventBus, requiring
   * another call of <code>GameManger::awaitNextMove</code> to listen again.
   */
  public void awaitNextMove()
  {
    setListening(true);
    getEventBus().register(this);
  }

  @Subscribe
  private void acceptMove(Move move)
  {
    if(isListening() && move.getSource() == getRotator().peek())
    {
      getExecutor().executeMove(getGrid(), getFinder(), getScorer(), move)
          .forEach(getEventBus()::post);
    }
  }

  @Subscribe
  private void endTurn(CrystalEvent.TurnEnd event)
  {
    if(isListening())
    {
      getRotator().nextPlayer();
      setListening(false);
      getEventBus().unregister(this);
    }
  }
}
