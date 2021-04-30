package de.dhbw.swe.betterjeweled.player;

import de.dhbw.swe.betterjeweled.core.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.testfx.framework.junit5.*;

import java.awt.*;
import java.util.*;
import java.util.List;

@ExtendWith(ApplicationExtension.class)
class CrystalButtonTest
{
  private static final Crystal RED = new Crystal(Color.RED);
  private static final Crystal GREEN = new Crystal(Color.GREEN);
  private static final Crystal BLUE = new Crystal(Color.BLUE);

  private CrystalButton nullButton;
  private List<CrystalButton> buttons;

  @Start
  void start(Stage stage) throws Exception
  {
    nullButton = new CrystalButton(null,1,1);

    buttons = new LinkedList<>();
    buttons.add(new CrystalButton(RED,1,0));
    buttons.add(new CrystalButton(BLUE,0,0));
    buttons.add(new CrystalButton(GREEN,0,1));
    buttons.add(nullButton);

    VBox root = new VBox(buttons.toArray(new Node[0]));
    stage.setTitle("CrystalButton Test");
    stage.setScene(new Scene(root));
    stage.show();
  }

  @Test
  void testButtonSizes()
  {
    buttons.forEach(button -> Assertions.assertEquals(buttons.get(0).getWidth(), button.getWidth()));
    buttons.forEach(button -> Assertions.assertEquals(buttons.get(0).getHeight(), button.getHeight()));
  }

  @Test
  void testFire()
  {
    buttons.forEach(button ->
    {
      Border oldBorder = button.getBorder();
      button.fire();
      Border newBorder = button.getBorder();

      if(button == nullButton)
      {
        Assertions.assertNull(oldBorder);
        Assertions.assertNull(newBorder);
      }
      else
      {
        Assertions.assertEquals(oldBorder.getStrokes().size(), newBorder.getStrokes().size());
        oldBorder.getStrokes().stream()
            .map(newBorder.getStrokes()::contains)
            .forEach(Assertions::assertTrue);

        Assertions.assertEquals(oldBorder.getStrokes().get(0), newBorder.getStrokes().get(1));
        Assertions.assertEquals(oldBorder.getStrokes().get(1), newBorder.getStrokes().get(0));
        Assertions.assertEquals(oldBorder.getStrokes().get(2), newBorder.getStrokes().get(3));
        Assertions.assertEquals(oldBorder.getStrokes().get(3), newBorder.getStrokes().get(2));
      }
    });
  }
}
