package de.dhbw.swe.betterjeweled.core;

import lombok.*;
import java.awt.*;

@Value
public class Crystal
{
  Color color;

  public boolean countsAs(Crystal condition)
  {
    return condition.equals(this);
  }
}
