package de.dhbw.swe.betterjeweled.core;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;

import java.util.stream.*;

class CrystalPairTest
{
  private static Stream<Arguments> generatePairs()
  {
    Player playerOne = Mockito.mock(Player.class);
    Player playerTwo = Mockito.mock(Player.class);

    Assertions.assertNotEquals(playerOne, playerTwo);

    return Stream.of(
        Arguments.of(new Move(0,1,2,3, playerOne), new Move(0,1,2,3, playerOne), true),
        Arguments.of(new Move(0,1,2,3, playerOne), new Move(0,1,2,3, playerTwo), true),
        Arguments.of(new Move(0,1,2,3, playerOne), new Move(2,3,0,1,null), true),
        Arguments.of(new Move(0,1,2,3, playerOne), new Move(2,3,0,1, playerOne), true),
        Arguments.of(new Move(0,1,-2,-3, playerOne), new Move(0,1,-2,-3, playerOne), true),

        Arguments.of(new Move(0,1,2,3, playerOne), new Move(0,1,2,2, playerOne), false),
        Arguments.of(new Move(0,1,2,3, playerOne), new Move(0,1,3,3, playerOne), false),
        Arguments.of(new Move(0,1,2,3, playerOne), new Move(0,0,2,3, playerOne), false),
        Arguments.of(new Move(0,1,2,3, playerOne), new Move(1,1,3,3, playerOne), false),
        Arguments.of(new Move(0,1,2,3, playerOne), new Move(0,-1,-2,-3, playerOne), false)
    );
  }

  @ParameterizedTest
  @MethodSource("generatePairs")
  void testEquals(Move pairOne, Move pairTwo, Boolean equal)
  {
    Assertions.assertEquals(pairOne.equals(pairTwo), pairTwo.equals(pairOne));
    Assertions.assertEquals(equal, pairOne.equals(pairTwo));
  }

  @ParameterizedTest
  @MethodSource("generatePairs")
  void testHashCode(Move pairOne, Move pairTwo, Boolean equal)
  {
    if(equal)
    {
      Assertions.assertEquals(pairOne.hashCode(), pairTwo.hashCode());
    }
  }
}