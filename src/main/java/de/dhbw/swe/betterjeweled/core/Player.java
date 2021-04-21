package de.dhbw.swe.betterjeweled.core;

import com.google.common.eventbus.*;

public interface Player extends Runnable
{
  Move getNextMove();
  void pushChanges(CrystalEvent changeEvent);

  @Subscribe
  default void acceptMove(CrystalEvent changeEvent)
  {
    pushChanges(changeEvent);
  }
}
