package de.dhbw.swe.betterjeweled.core;

// TODO: Change "rotator" to more domain specific name(?)
public interface PlayerRotator
{
  Player peek();
  Player reset();
  Player nextPlayer();
}
