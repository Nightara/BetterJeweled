package de.dhbw.swe.betterjeweled.impl;

import com.google.common.eventbus.*;
import de.dhbw.swe.betterjeweled.core.*;
import lombok.*;

import java.util.concurrent.atomic.*;

/**
 * The adapter class between the GameManager and Player classes.
 * Upon launch, it starts its supplied Player object's run method and registers itself to the supplied EventBus,
 * listening to CrystalEvents and passing them to the underlying Player object.
 */
@Value
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor(access=AccessLevel.PRIVATE)
@SuppressWarnings("UnstableApiUsage")
public class BusPlayerAdapter extends Thread
{
  Object[] synchronizer = new Object[0];

  Player player;
  EventBus eventBus;
  Thread playerThread;
  AtomicBoolean running;
  AtomicBoolean cancelled;

  /**
   * The default constructor.
   * Registers itself to the supplied EventBus and sets its run status to NOT RUNNING NOT CANCELLED.
   *
   * @param player The Player object to wrap.
   * @param eventBus The EventBus to register this instance to.
   */
  public BusPlayerAdapter(Player player, EventBus eventBus)
  {
    this(player, eventBus, new Thread(player), new AtomicBoolean(false), new AtomicBoolean(false));
    this.eventBus.register(this);
    this.playerThread.start();
  }

  /**
   * Launch this adapter, constantly polling Move events from the underlying Player object and passing them on to the
   * EventBus.
   */
  @Override
  public void run()
  {
    getRunning().set(true);
    while(!getCancelled().get())
    {
      CrystalPair nextMove = getPlayer().getNextMove();
      if(nextMove != null)
      {
        getEventBus().post(nextMove);
      }
    }

    getRunning().set(false);
    synchronized(getSynchronizer())
    {
      getSynchronizer().notifyAll();
    }
  }

  /**
   * Stop the execution of this adapter, returning a boolean representing the success of the stop operation.
   * The <code>Player::getNextMove</code> method may still be called asynchronously after calling this method.
   *
   * @return <code>true</code> if this runnable was stopped due to this call, <code>false</code> otherwise.
   */
  @SneakyThrows
  public boolean cancel()
  {
    boolean success = !getCancelled().getAndSet(true);

    while(getRunning().get())
    {
      synchronized(getSynchronizer())
      {
        getSynchronizer().wait();
      }
    }

    return success;
  }

  @Subscribe
  protected void handleGameUpdate(GameUpdate update)
  {
    getPlayer().handleGameUpdate(update);
  }
}
