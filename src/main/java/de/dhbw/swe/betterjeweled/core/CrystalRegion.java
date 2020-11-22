package de.dhbw.swe.betterjeweled.core;

import lombok.*;
import lombok.experimental.NonFinal;

import java.util.HashSet;
import java.util.Set;

@Value
@NonFinal
public class CrystalRegion
{

  Set<Position> positions;

  public CrystalRegion(int startX, int startY, int endX, int endY) {
    positions = new HashSet<>();
    for (int i = startX; i < endX; i++) {
      for (int j = startY; j < endY; j++) {
        positions.add(new Position(i, j));
      }
    }
  }

  public boolean contains(int posX, int posY)
  {
    return positions.contains(new Position(posX, posY));
  }

  public int getSize()
  {
    return positions.size();
  }

  public boolean intersects(CrystalRegion other) {
    Set<Position> temp = new HashSet<>();
    temp.addAll(positions);
    temp.addAll(other.getPositions());
    return temp.size() < (other.getSize() + this.getSize());
  }

  public void mergeWith(CrystalRegion other) {
    positions.addAll(other.getPositions());
  }

  @Value
  private class Position {

    private int x;
    private int y;

  }
}
