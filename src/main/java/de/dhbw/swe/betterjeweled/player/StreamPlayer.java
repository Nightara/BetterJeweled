package de.dhbw.swe.betterjeweled.player;

import de.dhbw.swe.betterjeweled.core.*;
import lombok.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;

@Data
public class StreamPlayer implements Player
{
  public static final Pattern INPUT_PATTERN = Pattern.compile("^(\\d+)(?>[,\\s])\\s*(\\d+)\\s*(?>[|\\s])" +
      "\\s*(\\d+)(?>[,\\s])\\s*(\\d+)$");

  private final List<CrystalPair> moves = Collections.synchronizedList(new LinkedList<>());
  @Getter(AccessLevel.PRIVATE)
  private final InputStream in;
  @Getter(AccessLevel.PRIVATE)
  private final OutputStream out;

  @Setter(AccessLevel.PRIVATE)
  private int score = 0;
  @Setter(AccessLevel.PRIVATE)
  private Crystal[][] grid = null;

  @Override
  @SneakyThrows
  public synchronized CrystalPair getNextMove()
  {
    while(getMoves().isEmpty())
    {
      this.wait();
    }
    return getMoves().remove(0);
  }

  @Override
  @SneakyThrows
  public void handleGameUpdate(GameUpdate gameUpdate)
  {
    setGrid(gameUpdate.getUpdatedGrid());
    setScore(gameUpdate.getScoreDelta());

    getOut().write(renderGrid().getBytes());
    getOut().flush();
  }

  @Override
  public void run()
  {
    try(Scanner scanner = new Scanner(getIn()))
    {
      while(scanner.hasNextLine())
      {
        synchronized(this)
        {
          Matcher matcher = INPUT_PATTERN.matcher(scanner.nextLine());
          if(matcher.matches())
          {
            CrystalPair nextMove = new CrystalPair(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)), null);
            getOut().write(("Supplied move: " + nextMove + "\n").getBytes(StandardCharsets.UTF_8));
            getMoves().add(nextMove);
            this.notifyAll();
          }
          else
          {
            getOut().write("invalid input\n".getBytes(StandardCharsets.UTF_8));
          }
          getOut().flush();
        }
      }
    }
    catch(IOException ex)
    {
      ex.printStackTrace();
    }
  }

  private String renderGrid()
  {
    Crystal[][] gridCopy = getGrid();
    int lenX = gridCopy.length;
    int lenY = gridCopy[0].length;
    StringBuilder rendered = new StringBuilder();

    rendered.append("+-".repeat(lenX)).append("+\n");
    for(int y = 0; y < lenY; y++)
    {
      for(Crystal[] crystals : gridCopy)
      {
        rendered.append("|").append(crystals[y] == null ? ' ' : (char) ((crystals[y].getColor().getRGB() % 26) + 90));
      }
      rendered.append("|\n");
      rendered.append("+-".repeat(lenX)).append("+\n");
    }

    return rendered.toString();
  }
}
