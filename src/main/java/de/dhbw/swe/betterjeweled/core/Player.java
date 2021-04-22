package de.dhbw.swe.betterjeweled.core;

public interface Player extends Runnable
{
  Move getNextMove();
  void handleChangeEvent(CrystalEvent changeEvent);
}
