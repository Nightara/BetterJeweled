package de.dhbw.swe.betterjeweled.core;

import lombok.*;
import lombok.experimental.*;

@Value
@NonFinal
public abstract class CrystalEvent
{
  int scoreDelta;
  Crystal[][] oldGrid;
  Crystal[][] updatedGrid;
  ModifierType modifierType;

  @ToString(callSuper=true)
  public static class Move extends CrystalEvent
  {
    public Move(Crystal[][] oldGrid, Crystal[][] updatedGrid)
    {
      super(0, oldGrid, updatedGrid, ModifierType.MOVE);
    }
  }

  @ToString(callSuper=true)
  public static class Trigger extends CrystalEvent
  {
    public Trigger(int scoreDelta, Crystal[][] oldGrid, Crystal[][] updatedGrid)
    {
      super(scoreDelta, oldGrid, updatedGrid, ModifierType.TRIGGER);
    }
  }

  @ToString(callSuper=true)
  public static class Shift extends CrystalEvent
  {
    public Shift(Crystal[][] oldGrid, Crystal[][] updatedGrid)
    {
      super(0, oldGrid, updatedGrid, ModifierType.SHIFT);
    }
  }

  @ToString(callSuper=true)
  public static class Fill extends CrystalEvent
  {
    public Fill(Crystal[][] oldGrid, Crystal[][] updatedGrid)
    {
      super(0, oldGrid, updatedGrid, ModifierType.FILL);
    }
  }

  @ToString(callSuper=true)
  public static class TurnEnd extends CrystalEvent
  {
    public TurnEnd()
    {
      super(0, new Crystal[0][0], new Crystal[0][0], ModifierType.TURN_END);
    }
  }

  public enum ModifierType
  {
    MOVE, TRIGGER, SHIFT, FILL, TURN_END
  }
}
