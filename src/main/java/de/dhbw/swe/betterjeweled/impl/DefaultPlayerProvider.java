package de.dhbw.swe.betterjeweled.impl;

import de.dhbw.swe.betterjeweled.core.*;
import lombok.*;

import java.util.*;

public class DefaultPlayerProvider implements PlayerProvider
{
  @Setter
  private List<Player> players;

  private int nextIndex = 0;

  public DefaultPlayerProvider(Player... players)
  {
    this(Arrays.asList(players));
  }

  public DefaultPlayerProvider(List<Player> players)
  {
    this.players = players;
  }

  public Player reset()
  {
    nextIndex = 0;
    return nextPlayer();
  }

  @Override
  public Player nextPlayer()
  {
    Player nextPlayer = peek();
    nextIndex++;
    nextIndex %= players.size();

    return nextPlayer;
  }

  @Override
  public Player peek()
  {
    return players.get(nextIndex);
  }
}
