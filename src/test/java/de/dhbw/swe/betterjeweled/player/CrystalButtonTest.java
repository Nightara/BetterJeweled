package de.dhbw.swe.betterjeweled.player;

import de.dhbw.swe.betterjeweled.core.*;
import javafx.css.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import lombok.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.testfx.framework.junit5.*;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.*;

@ExtendWith(ApplicationExtension.class)
class CrystalButtonTest
{
  private static final Crystal RED = new Crystal(Color.RED);
  private static final Crystal GREEN = new Crystal(Color.GREEN);
  private static final Crystal BLUE = new Crystal(Color.BLUE);

  @Getter
  private static final List<CrystalButton> buttons = new LinkedList<>();

  private CrystalButton nullButton;

  @Start
  void start(Stage stage)
  {
    nullButton = new CrystalButton(null,1,1);

    getButtons().add(new CrystalButton(RED,1,0));
    getButtons().add(new CrystalButton(BLUE,0,0));
    getButtons().add(new CrystalButton(GREEN,0,1));
    getButtons().add(nullButton);

    VBox root = new VBox(buttons.toArray(new Node[0]));
    stage.setTitle("CrystalButton Test");
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Test
  void testButtonSizes()
  {
    buttons.forEach(button -> Assertions.assertEquals(nullButton.getWidth(), button.getWidth()));
    buttons.forEach(button -> Assertions.assertEquals(nullButton.getHeight(), button.getHeight()));
  }

  @Test
  void testFire()
  {
    buttons.forEach(button ->
    {
      List<Color> oldBorder = Arrays.stream(button.getStyle().split(";"))
          .filter(line -> line.startsWith("-fx-border-color"))
          .map(line -> line.split(":")[1])
          .map(String::trim)
          .flatMap(line -> Arrays.stream(line.split(" ")))
          .map(Color::decode)
          .collect(Collectors.toList());
      button.fire();
      List<Color> newBorder = Arrays.stream(button.getStyle().split(";"))
          .filter(line -> line.startsWith("-fx-border-color"))
          .map(line -> line.split(":")[1])
          .map(String::trim)
          .flatMap(line -> Arrays.stream(line.split(" ")))
          .map(Color::decode)
          .collect(Collectors.toList());

      Assertions.assertEquals(4, oldBorder.size());
      Assertions.assertEquals(4, newBorder.size());

      Assertions.assertEquals(oldBorder.get(0), oldBorder.get(3));
      Assertions.assertEquals(oldBorder.get(1), oldBorder.get(2));

      Assertions.assertEquals(newBorder.get(0), newBorder.get(3));
      Assertions.assertEquals(newBorder.get(1), newBorder.get(2));

      Assertions.assertEquals(oldBorder.get(0), newBorder.get(1));
      Assertions.assertEquals(oldBorder.get(1), newBorder.get(0));

      if(button == nullButton)
      {
        Assertions.assertEquals(255, oldBorder.get(0).getAlpha());
        Assertions.assertEquals(255, oldBorder.get(2).getAlpha());
      }
      else
      {
        double topLightness = getLightness(oldBorder.get(0));
        double bottomLightness = getLightness(oldBorder.get(2));
        Assertions.assertTrue(topLightness > bottomLightness);

        topLightness = getLightness(newBorder.get(0));
        bottomLightness = getLightness(newBorder.get(2));
        Assertions.assertTrue(topLightness < bottomLightness);
      }
    });
  }

  private static double getLightness(Color color)
  {
    double r = color.getRed() / 255.0;
    double b = color.getBlue() / 255.0;
    double g = color.getGreen() / 255.0;

    double min = Math.min(r, Math.min(g, b));
    double max = Math.max(r, Math.max(g, b));

    return (max + min) / 2;
  }
}
