package de.dhbw.swe.betterjeweled.core;

public interface PlayerProvider
{
  Player peek();
  Player reset();
  Player nextPlayer();
}
