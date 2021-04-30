package de.dhbw.swe.betterjeweled.core;

// TODO: Replace handleChangeEvent with DDD name
public interface Player extends Runnable
{
  Move getNextMove();
  void handleChangeEvent(CrystalEvent changeEvent);
}
