package de.dhbw.swe.betterjeweled.core;

public interface Player {

    Move getMove();
    void pushChanges(Crystal[][] grid, ModifierType type, int score);
}
