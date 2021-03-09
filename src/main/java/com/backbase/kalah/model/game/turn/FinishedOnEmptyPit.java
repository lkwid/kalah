package com.backbase.kalah.model.game.turn;

import com.backbase.kalah.model.game.Game;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class FinishedOnEmptyPit implements WrapUpStrategy {

    @Override
    public void wrapUp(Game game) {

        log.info("Player finished on empty pit. Player collects all stones from the opposite pit and put them in his Kalah");

        Map<Integer, Integer> gameScore = game.getScore();
        int playerKalah = game.getActivePlayer() == 1 ? 7 : 14;
        int oppositePit = 14 - game.getFinalPit();

        gameScore.replace(playerKalah, gameScore.get(playerKalah) + gameScore.get(oppositePit));
        gameScore.replace(oppositePit, 0);

        gameScore.replace(playerKalah, gameScore.get(playerKalah) + gameScore.get(game.getFinalPit()));
        gameScore.replace(game.getFinalPit(), 0);

        game.setScore(gameScore);
        game.setActivePlayer((byte) (game.getActivePlayer() == 1 ? 2 : 1));

    }
}
