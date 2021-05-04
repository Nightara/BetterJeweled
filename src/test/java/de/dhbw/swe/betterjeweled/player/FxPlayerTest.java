package de.dhbw.swe.betterjeweled.player;

import de.dhbw.swe.betterjeweled.core.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.*;
import lombok.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.testfx.framework.junit5.*;
import org.testfx.util.*;

import java.awt.*;
import java.util.stream.*;

@ExtendWith(ApplicationExtension.class)
class FxPlayerTest
{
  private static final Crystal RED = new Crystal(Color.RED);
  private static final Crystal GREEN = new Crystal(Color.GREEN);
  private static final Crystal BLUE = new Crystal(Color.BLUE);

  private GridPane grid;
  private FxPlayer player;
  private Label scoreBoard;

  private static Stream<Arguments> generateGridDimensions()
  {
    return Stream.of(
        Arguments.of(5, 5, 100),
        Arguments.of(3, 8, 700),
        Arguments.of(7, 1, 99)
    );
  }

  private static Stream<Arguments> generateMoveData()
  {
    return Stream.of(
        Arguments.of(0, 1, 0, 2),
        Arguments.of(1, 2, 2, 0),
        Arguments.of(3, 0, 1, 4),
        Arguments.of(2, 4, 3, 1)
    );
  }

  private static Stream<Crystal[][]> generateFilledGrids()
  {
    return Stream.of(
        new Crystal[][]
        {
            new Crystal[]{RED, GREEN, RED, RED},
            new Crystal[]{GREEN, BLUE, null, RED},
            new Crystal[]{GREEN, GREEN, BLUE, GREEN},
            new Crystal[]{BLUE, RED, RED, BLUE}
        },
        new Crystal[][]
            {
                new Crystal[]{GREEN, GREEN, BLUE, RED},
                new Crystal[]{BLUE, null, RED, GREEN},
                new Crystal[]{GREEN, GREEN, RED, BLUE}
            }
    );
  }

  @Start
  void start(Stage stage) throws Exception
  {
    FXMLLoader loader = new FXMLLoader(FxPlayer.class.getResource("Game.fxml"));
    BorderPane root = loader.load();
    player = loader.getController();
    grid = (GridPane) root.getCenter();
    scoreBoard = (Label) root.getBottom();

    stage.setTitle("FxPlayer Test");
    stage.setScene(new Scene(root));
    stage.show();
  }

  @ParameterizedTest
  @MethodSource("generateGridDimensions")
  void updateFxPlayer(int lenX, int lenY, Integer score)
  {
    player.handleGameUpdate(new GameUpdate.Trigger(score, new Crystal[lenX][lenY], new Crystal[lenX][lenY]));

    Assertions.assertEquals(lenX * lenY, grid.getChildren().size());
    Assertions.assertTrue(scoreBoard.getText().contains(score.toString()));
  }

  @SneakyThrows
  @ParameterizedTest
  @MethodSource("generateFilledGrids")
  void fillFxPlayer(Crystal[][] crystalGrid)
  {
    player.handleChangeEvent(new CrystalEvent.Fill(new Crystal[0][0], crystalGrid));

    grid.getChildren().stream()
        .filter(CrystalButton.class::isInstance)
        .map(CrystalButton.class::cast)
        .forEach(button -> Assertions.assertSame(crystalGrid[button.getPosX()][button.getPosY()], button.getCrystal()));
  }

  @ParameterizedTest
  @MethodSource("generateMoveData")
  void readFxPlayerMove(int posXOne, int posYOne, int posXTwo, int posYTwo)
  {
    player.handleGameUpdate(new GameUpdate.Fill(new Crystal[5][5], new Crystal[5][5]));

    Platform.runLater(() -> grid.getChildren().stream()
        .filter(CoordinateToggleButton.class::isInstance)
        .map(CoordinateToggleButton.class::cast)
        .filter(button -> (button.getPosX() == posXOne && button.getPosY() == posYOne)
            || (button.getPosX() == posXTwo && button.getPosY() == posYTwo))
        .forEach(ToggleButton::fire));

    Assertions.assertEquals(new CrystalPair(posXOne, posYOne, posXTwo, posYTwo,null), player.getNextMove());
  }

  @ParameterizedTest
  @MethodSource("generateMoveData")
  @SuppressWarnings("java:S2925")
  void readFxPlayerMoveWithDelay(int posXOne, int posYOne, int posXTwo, int posYTwo)
  {
    player.handleGameUpdate(new GameUpdate.Fill(new Crystal[5][5], new Crystal[5][5]));

    Platform.runLater(() ->
    {
      try
      {
        Thread.sleep(100);
      }
      catch(InterruptedException ignored)
      {}

      grid.getChildren().stream()
          .filter(CoordinateToggleButton.class::isInstance)
          .map(CoordinateToggleButton.class::cast)
          .filter(button -> (button.getPosX() == posXOne && button.getPosY() == posYOne)
              || (button.getPosX() == posXTwo && button.getPosY() == posYTwo))
          .forEach(ToggleButton::fire);
    });

    Assertions.assertEquals(new CrystalPair(posXOne, posYOne, posXTwo, posYTwo,null), player.getNextMove());
  }

  @Test
  void testDoubleTrigger()
  {
    player.handleChangeEvent(new CrystalEvent.Fill(new Crystal[5][5], new Crystal[5][5]));

    Platform.runLater(() -> grid.getChildren().stream()
          .filter(CoordinateToggleButton.class::isInstance)
          .map(CoordinateToggleButton.class::cast)
          .forEach(button ->
          {
            button.fire();
            button.fire();
            Assertions.assertTrue(player.getTriggeredButtons().isEmpty());
          }));
  }
}
