package de.dhbw.swe.betterjeweled.core;

public interface Player extends Runnable
{
  CrystalPair getNextMove();
  void handleGameUpdate(GameUpdate gameUpdate);
}
