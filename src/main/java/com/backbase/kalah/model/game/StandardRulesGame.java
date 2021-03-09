package com.backbase.kalah.model.game;

import java.util.HashMap;

public class StandardRulesGame extends Game {

    public StandardRulesGame() {
        id = instanceCounter.addAndGet(1);
        status = true;
        activePlayer = 1;

        score = new HashMap<>();
        score.put(1, 6);
        score.put(2, 6);
        score.put(3, 6);
        score.put(4, 6);
        score.put(5, 6);
        score.put(6, 6);
        score.put(7, 0);
        score.put(8, 6);
        score.put(9, 6);
        score.put(10, 6);
        score.put(11, 6);
        score.put(12, 6);
        score.put(13, 6);
        score.put(14, 0);
    }

}
