package de.dhbw.swe.betterjeweled.player;

import de.dhbw.swe.betterjeweled.core.*;
import lombok.*;

import java.awt.*;
import java.util.*;

@Getter
@SuppressWarnings("java:S110")
@EqualsAndHashCode(callSuper=true)
public class CrystalButton extends CoordinateToggleButton
{
  private final Crystal crystal;

  public CrystalButton(Crystal crystal, int posX, int posY)
  {
    super("     ", posX, posY);
    this.crystal = crystal;
    this.updateStyle();
  }

  @Override
  public void fire()
  {
    super.fire();
    updateStyle();
  }

  private void updateStyle()
  {
    Optional<Color> color = Optional.ofNullable(getCrystal())
        .map(Crystal::getColor);
    this.setStyle(
        "-fx-padding: 6px;" +
        "-fx-background-radius: 0;" +
        "-fx-background-color: " + getColorCode(color) + ";" +
        "-fx-border-width: 4px;" +
        "-fx-border-style: solid;" +
        "-fx-border-color: " +
            getColorCode(color.map(isSelected() ? CrystalButton::darken : CrystalButton::brighten)) + " " +
            getColorCode(color.map(isSelected() ? CrystalButton::brighten : CrystalButton::darken)) + " " +
            getColorCode(color.map(isSelected() ? CrystalButton::brighten : CrystalButton::darken)) + " " +
            getColorCode(color.map(isSelected() ? CrystalButton::darken : CrystalButton::brighten)) + ";"
    );
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private static String getColorCode(Optional<Color> color)
  {
    return color.map(CrystalButton::getColorCode)
        .orElse("#0000");
  }

  private static String getColorCode(Color color)
  {
    return String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
  }

  protected static Color brighten(Color color)
  {
    double fraction = 0.33;
    int red = (int) Math.round(Math.min(255, color.getRed() + 255 * fraction));
    int green = (int) Math.round(Math.min(255, color.getGreen() + 255 * fraction));
    int blue = (int) Math.round(Math.min(255, color.getBlue() + 255 * fraction));

    int alpha = color.getAlpha();

    return new Color(red, green, blue, alpha);
  }

  protected static Color darken(Color color)
  {
    double fraction = 0.33;
    int red = (int) Math.round(Math.max(0, color.getRed() - 255 * fraction));
    int green = (int) Math.round(Math.max(0, color.getGreen() - 255 * fraction));
    int blue = (int) Math.round(Math.max(0, color.getBlue() - 255 * fraction));

    int alpha = color.getAlpha();

    return new Color(red, green, blue, alpha);
  }
}
