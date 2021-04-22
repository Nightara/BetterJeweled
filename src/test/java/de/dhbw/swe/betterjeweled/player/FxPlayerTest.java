package de.dhbw.swe.betterjeweled.player;

import de.dhbw.swe.betterjeweled.core.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.testfx.framework.junit5.*;

import java.util.stream.*;

@ExtendWith(ApplicationExtension.class)
class FxPlayerTest
{
  Stage stage;
  FxPlayer player;
  BorderPane root;
  GridPane grid;
  Label scoreBoard;

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

  @Start
  void start(Stage stage) throws Exception
  {
    FXMLLoader loader = new FXMLLoader(FxPlayer.class.getResource("Game.fxml"));
    root = loader.load();
    player = loader.getController();
    grid = (GridPane) root.getCenter();
    scoreBoard = (Label) root.getBottom();

    stage.setTitle("FxPlayer Test");
    stage.setScene(new Scene(root));
    this.stage = stage;
  }

  @ParameterizedTest
  @MethodSource("generateGridDimensions")
  void updateFxPlayer(int lenX, int lenY, Integer score)
  {
    player.handleChangeEvent(new CrystalEvent.Trigger(score, new Crystal[lenX][lenY], new Crystal[lenX][lenY]));

    Assertions.assertEquals(lenX * lenY, grid.getChildren().size());
    Assertions.assertTrue(scoreBoard.getText().contains(score.toString()));
  }

  @ParameterizedTest
  @MethodSource("generateMoveData")
  void readFxPlayerMove(int posXOne, int posYOne, int posXTwo, int posYTwo)
  {
    player.handleChangeEvent(new CrystalEvent.Fill(new Crystal[5][5], new Crystal[5][5]));

    Platform.runLater(() -> grid.getChildren().stream()
        .filter(CoordinateToggleButton.class::isInstance)
        .map(CoordinateToggleButton.class::cast)
        .filter(button -> (button.getPosX() == posXOne && button.getPosY() == posYOne)
            || (button.getPosX() == posXTwo && button.getPosY() == posYTwo))
        .forEach(ToggleButton::fire));

    Assertions.assertEquals(new Move(posXOne, posYOne, posXTwo, posYTwo,null), player.getNextMove());
  }
}
