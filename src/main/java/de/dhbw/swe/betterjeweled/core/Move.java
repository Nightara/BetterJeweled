package de.dhbw.swe.betterjeweled.core;

import lombok.*;

@Value
// TODO: Rename to CrystalPair
public class Move
{
  int x1;
  int y1;
  int x2;
  int y2;
  Player source;

  @Override
  public boolean equals(Object o)
  {
    if(this == o) return true;
    if(o == null || getClass() != o.getClass()) return false;

    Move move = (Move) o;

    return (getX1() == move.getX1() && getY1() == move.getY1()
          && getX2() == move.getX2() && getY2() == move.getY2())
        || (getX1() == move.getX2() && getY1() == move.getY2()
          && getX2() == move.getX1() && getY2() == move.getY1());
  }

  @Override
  public int hashCode()
  {
    int result = getX1() * getX2();
    result = 31 * result + getY1() * getY2();
    return result;
  }
}
