package com.backbase.kalah.model.game;

import com.backbase.kalah.model.game.turn.WrapUpStrategy;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
@Data
public class Game {

    /**
     * A counter used to increment game ID
     */
    static AtomicLong instanceCounter = new AtomicLong(0);

    Long id;

    /**
     * Tracks game status -> true if running false if finished
     */
    Boolean status;

    /**
     * This is a map representation of Kalah board -> [pit ID] : [number of stones]
     */
    Map<Integer, Integer> score;

    /**
     * Which players takes a turn -> 1 for player 1, 2 for player 2
     */
    Byte activePlayer;

    /**
     * A strategy determining wrap-up action ->
     * - NextTurn: no action required, next turn can start
     * - FinishedOnKalah: player gets another turn
     * - FinishedOnEmptyPit: when finished on the empty player's pit, captures all stones from the opposite pit
     * - GameOver: player has no more stones in his pits
     */
    WrapUpStrategy wrapUpStrategy;

    /**
     * Stores the info about the pit where the last stone has been put.
     */
    Integer finalPit;

}
