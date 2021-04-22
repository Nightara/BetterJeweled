package de.dhbw.swe.betterjeweled.player;

import com.sun.javafx.collections.*;
import de.dhbw.swe.betterjeweled.core.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lombok.*;

import java.net.*;
import java.util.*;
import java.util.concurrent.*;

@Getter(AccessLevel.PRIVATE)
public class FxPlayer implements Initializable, Player
{
  @FXML
  private GridPane gameField;
  @FXML
  private Label scoreBoard;

  private ObservableList<CoordinateToggleButton> triggeredButtons;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle)
  {
    triggeredButtons = new ObservableListWrapper<>(Collections.synchronizedList(new LinkedList<>()));
  }

  @SneakyThrows
  @Override
  public Move getNextMove()
  {
    while(getTriggeredButtons().size() < 2)
    {}

    synchronized(getTriggeredButtons())
    {
      CoordinateToggleButton b1 = getTriggeredButtons().remove(0);
      b1.setSelected(false);
      CoordinateToggleButton b2 = getTriggeredButtons().remove(0);
      b2.setSelected(false);

      return new Move(b1.getPosX(), b1.getPosY(), b2.getPosX(), b2.getPosY(),null);
    }
  }

  @Override
  @SneakyThrows
  public void handleChangeEvent(CrystalEvent changeEvent)
  {
    CountDownLatch finished = new CountDownLatch(1);
    Platform.runLater(() ->
    {
      getGameField().getChildren().clear();
      for(int x = 0; x < changeEvent.getUpdatedGrid().length; x++)
      {
        for(int y = 0; y < changeEvent.getUpdatedGrid()[x].length; y++)
        {
          getGameField().add(generateCrystalNode(x, y, changeEvent.getUpdatedGrid()[x][y]), x, y);
        }
      }

      scoreBoard.setText("Current score: " + changeEvent.getScoreDelta());
      finished.countDown();
    });

    finished.await();
  }

  private CoordinateToggleButton generateCrystalNode(int posX, int posY, Crystal crystal)
  {
    CoordinateToggleButton button = new CoordinateToggleButton(crystal == null ? "-/-" : crystal.toString(), posX, posY);
    button.setOnAction(a -> toggleCrystal(button));

    return button;
  }

  @Synchronized
  private void toggleCrystal(CoordinateToggleButton button)
  {
    if(button.isSelected() && !getTriggeredButtons().contains(button))
    {
      getTriggeredButtons().add(button);
    }
    else
    {
      getTriggeredButtons().remove(button);
    }
  }

  @Override
  public void run()
  {}
}
