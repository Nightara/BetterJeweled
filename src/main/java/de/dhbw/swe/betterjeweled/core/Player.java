package de.dhbw.swe.betterjeweled.core;

public interface Player extends Runnable {

    Move getMove();
    void pushChanges(Crystal[][] grid, ModifierType type, int score);
}
