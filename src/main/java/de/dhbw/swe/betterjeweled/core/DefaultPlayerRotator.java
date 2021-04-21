package de.dhbw.swe.betterjeweled.core;

import java.util.*;

public class DefaultPlayerRotator implements PlayerRotator
{
  private int lastIndex = -1;

  public Player getFirstPlayer(List<Player> players)
  {
    lastIndex = 0;
    return players.get(0);
  }

  @Override
  public Player apply(List<Player> players)
  {
    lastIndex++;
    lastIndex %= players.size();

    return players.get(lastIndex);
  }
}
