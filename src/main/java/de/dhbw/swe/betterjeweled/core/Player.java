package de.dhbw.swe.betterjeweled.core;

import com.google.common.eventbus.*;

public interface Player
{
  CrystalChangeEvent getNextMove();
  void pushChanges(CrystalChangeEvent changeEvent);

  @Subscribe
  default void acceptMove(CrystalChangeEvent changeEvent)
  {
    pushChanges(changeEvent);
  }
}
