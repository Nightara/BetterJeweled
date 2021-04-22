package de.dhbw.swe.betterjeweled.core;

import com.google.common.eventbus.*;

public interface Player extends Runnable
{
  Move getNextMove();
  void handleChangeEvent(CrystalEvent changeEvent);

  @Subscribe
  default void acceptChangeEvent(CrystalEvent changeEvent)
  {
    handleChangeEvent(changeEvent);
  }
}
