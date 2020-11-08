package de.dhbw.swe.betterjeweled.core;

import lombok.*;
import java.awt.*;

/**
 * The Crystal class represents a plain crystal with just a color and no other special qualities.
 * "Special" crystals can extend this class and override the Crystal#countsAs method to specify their behavior.
 */
@Value
public class Crystal
{
  Color color;

  /**
   * Checks whether this Crystal considers itself to be equal to the supplied Crystal. It is recommended to use this
   * method over Crystal#equals for region detections to allow proper usage of "special" Crystals.
   *
   * @param condition The Crystal to compare this Crystal to
   * @return True if this Crystal considers itself to be equal to the supplied Crystal, false otherwise
   */
  public boolean countsAs(Crystal condition)
  {
    return condition.equals(this);
  }
}
