package com.backbase.kalah.model.game.turn;

import com.backbase.kalah.model.game.Game;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
public class GameOver implements WrapUpStrategy {

    @Override
    public void wrapUp(Game game) {

        log.info("No more stones in player {} pits. The game is over", game.getActivePlayer());

        game.setScore(calculateScore(game.getScore()));
        game.setStatus(false);

    }

    private Map<Integer, Integer> calculateScore(Map<Integer, Integer> gameScore) {
        int playerOneScore = IntStream.rangeClosed(1, 7)
                .map(gameScore::get)
                .sum();
        int playerTwoScore = IntStream.rangeClosed(8, 14)
                .map(gameScore::get)
                .sum();

        gameScore.replaceAll(( k, v ) -> v = 0);
        gameScore.replace(7, playerOneScore);
        gameScore.replace(14, playerTwoScore);

        return gameScore;
    }
}
