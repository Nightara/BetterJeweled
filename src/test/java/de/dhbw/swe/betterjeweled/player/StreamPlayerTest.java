package de.dhbw.swe.betterjeweled.player;

import de.dhbw.swe.betterjeweled.core.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.awt.*;
import java.io.*;
import java.nio.charset.*;
import java.util.regex.*;
import java.util.stream.*;

class StreamPlayerTest
{
  private static Stream<Arguments> generateGrids()
  {
    Crystal[][] filledGrid = new Crystal[][]{new Crystal[]{new Crystal(Color.RED)}};

    return Stream.of(
        Arguments.of((Object) new Crystal[5][5]),
        Arguments.of((Object) new Crystal[3][4]),
        Arguments.of((Object) new Crystal[7][1]),
        Arguments.of((Object) filledGrid)
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {"1,2|2,3", "4,3 3,1", "0, 1|5, 5", "7,5 | 4,3", "9, 7 | 8, 6"})
  void readStreamPlayerMove(String param) throws InterruptedException
  {
    InputStream in = new ByteArrayInputStream((param + "\n").getBytes(StandardCharsets.UTF_8));
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    StreamPlayer player = new StreamPlayer(in, out);

    Matcher matcher = StreamPlayer.INPUT_PATTERN.matcher(param);
    Assertions.assertTrue(matcher.matches());

    int x1 = Integer.parseInt(matcher.group(1));
    int y1 = Integer.parseInt(matcher.group(2));
    int x2 = Integer.parseInt(matcher.group(3));
    int y2 = Integer.parseInt(matcher.group(4));

    Thread playerThread = new Thread(player);
    playerThread.start();
    playerThread.join();
    Assertions.assertEquals("Supplied move: Move(x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", source=null)\n",
        out.toString(StandardCharsets.UTF_8));
  }

  @ParameterizedTest
  @MethodSource("generateGrids")
  void updateStreamPlayer(Crystal[][] grid)
  {
    InputStream in = new ByteArrayInputStream(new byte[0]);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    StreamPlayer player = new StreamPlayer(in, out);
    new Thread(player).start();

    player.handleChangeEvent(new CrystalEvent.Trigger(100, grid, grid));
    String[] data = out.toString(StandardCharsets.UTF_8).split("\n");
    Assertions.assertEquals(2 * grid.length + 1, data[0].length());
    Assertions.assertEquals(2 * grid[0].length + 1, data.length);
  }
}
