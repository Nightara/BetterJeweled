package de.dhbw.swe.betterjeweled.impl;

import com.google.common.eventbus.*;
import de.dhbw.swe.betterjeweled.core.*;
import lombok.*;

import java.util.concurrent.atomic.*;

@Value
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor(access=AccessLevel.PRIVATE)
@SuppressWarnings("UnstableApiUsage")
public class BusPlayerAdapter extends Thread
{
  Object[] synchronizer = new Object[0];

  Player player;
  EventBus eventBus;
  AtomicBoolean running;
  AtomicBoolean cancelled;

  public BusPlayerAdapter(Player player, EventBus eventBus)
  {
    this(player, eventBus, new AtomicBoolean(false), new AtomicBoolean(false));
    this.eventBus.register(this);
  }

  @Override
  public void run()
  {
    getRunning().set(true);
    while(!getCancelled().get())
    {
        getEventBus().post(getPlayer().getNextMove());
    }

    getRunning().set(false);
    synchronized(getSynchronizer())
    {
      getSynchronizer().notifyAll();
    }
  }

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
  public void handleChangeEvent(CrystalEvent changeEvent)
  {
    getPlayer().handleChangeEvent(changeEvent);
  }
}
