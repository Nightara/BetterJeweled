package de.dhbw.swe.betterjeweled.core;

import lombok.*;

import java.util.*;
import java.util.stream.*;

@Value
public class CrystalRegion
{
  @Getter(AccessLevel.PRIVATE)
  Set<Position> positions;

  private CrystalRegion(Collection<Position> positions)
  {
    this.positions = new HashSet<>(positions);
  }

  public CrystalRegion(int startX, int startY, int endX, int endY)
  {
    this(Collections.emptySet());

    for(int i = startX; i < endX; i++)
    {
      for(int j = startY; j < endY; j++)
      {
        positions.add(new Position(i, j));
      }
    }
  }

  public boolean contains(int posX, int posY)
  {
    return getPositions().contains(new Position(posX, posY));
  }

  public int getSize()
  {
    return getPositions().size();
  }

  public boolean intersects(CrystalRegion other)
  {
    return getPositions().stream()
        .anyMatch(other.getPositions()::contains);
  }

  public static CrystalRegion merge(CrystalRegion... regions)
  {
    return merge(Arrays.asList(regions));
  }

  public static CrystalRegion merge(Collection<CrystalRegion> regions)
  {
    Set<Position> fields = regions.stream()
        .map(CrystalRegion::getPositions)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    return new CrystalRegion(fields);
  }

  @Value
  public static class Position
  {
    int x;
    int y;
  }
}
