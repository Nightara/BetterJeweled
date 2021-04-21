package de.dhbw.swe.betterjeweled.core;

import lombok.*;

@Value
public class CrystalChangeEvent
{
  int oldScore;
  int updatedScore;
  Crystal[][] oldGrid;
  Crystal[][] updatedGrid;
  ModifierType modifierType;
}
