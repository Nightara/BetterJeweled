package de.dhbw.swe.betterjeweled.core;

import lombok.*;

@Value
public class CrystalPair
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

    CrystalPair crystalPair = (CrystalPair) o;

    return (getX1() == crystalPair.getX1() && getY1() == crystalPair.getY1()
          && getX2() == crystalPair.getX2() && getY2() == crystalPair.getY2())
        || (getX1() == crystalPair.getX2() && getY1() == crystalPair.getY2()
          && getX2() == crystalPair.getX1() && getY2() == crystalPair.getY1());
  }

  @Override
  public int hashCode()
  {
    int result = getX1() * getX2();
    result = 31 * result + getY1() * getY2();
    return result;
  }
}
