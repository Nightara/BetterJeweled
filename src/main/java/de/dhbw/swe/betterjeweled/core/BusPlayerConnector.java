package de.dhbw.swe.betterjeweled.core;

import com.google.common.eventbus.*;
import lombok.*;

import java.util.concurrent.atomic.*;

@Value
@AllArgsConstructor(access=AccessLevel.PRIVATE)
@SuppressWarnings("UnstableApiUsage")
public class BusPlayerConnector implements Runnable
{
  Player player;
  EventBus eventBus;
  AtomicBoolean running;
  AtomicBoolean cancelled;

  public BusPlayerConnector(Player player, EventBus eventBus)
  {
    this(player, eventBus, new AtomicBoolean(false), new AtomicBoolean(false));
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
    synchronized(this)
    {
      this.notifyAll();
    }
  }

  @SneakyThrows
  public boolean cancel()
  {
    boolean success = !getCancelled().getAndSet(true);

    while(getRunning().get())
    {
      synchronized(this)
      {
        this.wait();
      }
    }

    return success;
  }
}
