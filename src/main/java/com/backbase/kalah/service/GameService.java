package com.backbase.kalah.service;

import com.backbase.kalah.exceptions.GameNotFoundException;
import com.backbase.kalah.exceptions.IllegalMoveException;
import com.backbase.kalah.model.game.Game;
import com.backbase.kalah.model.game.StandardRulesGame;
import com.backbase.kalah.model.game.turn.FinishedOnEmptyPit;
import com.backbase.kalah.model.game.turn.FinishedOnKalah;
import com.backbase.kalah.model.game.turn.GameOver;
import com.backbase.kalah.model.game.turn.NextTurn;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

// TODO: 02/02/2021 add javadoc to the whole service
@Service
public class GameService {

    public static final Map<Long, Game> runningGames = new HashMap<>();

    public Game createNewGame() {
        Game game = new StandardRulesGame();
        runningGames.put(game.getId(), game);
        return game;
    }

    public Game makeMove(long gameId, int selectedPit) {
        Game game = runningGames.get(gameId);

        checkForIllegalMove(game, selectedPit);

        int remainingStones = game.getScore().get(selectedPit);

        calculateScore(game, selectedPit, remainingStones);
        setWrapUpStrategy(game);
        wrapUpTurn(game);

        return game;

    }

    // TODO: 02/02/2021 add test cases for these exceptions
    private void checkForIllegalMove(Game game, int selectedPit) {
        if (Objects.isNull(game)) {
            throw new GameNotFoundException("Cannot find requested game.");
        }
        if (List.of(7, 14).contains(selectedPit)) {
            throw new IllegalMoveException("Cannot select stones from Kalah. Please try again");
        }
        if (game.getScore().get(selectedPit) == 0) {
            throw new IllegalMoveException("Pit is empty. Please select another pit");
        }
    }

    private void calculateScore(Game game, int selectedPit, int remainingStones) {
        pickUpStonesFromSelectedPit(game, selectedPit);
        sowStones(game, selectedPit, remainingStones);
    }

    private void pickUpStonesFromSelectedPit(Game game, int selectedPit) {
        game.getScore().replace(selectedPit, 0);
    }

    private int sowStones(Game game, int selectedPit, int remainingStones) {
        Map<Integer, Integer> gameScore = game.getScore();
        AtomicInteger usedStones = new AtomicInteger();
        AtomicInteger currentPit = new AtomicInteger();

        IntStream.rangeClosed(++selectedPit, 14)
                .filter(pit -> game.getActivePlayer() == 1 ? pit != 14 : pit != 7)
                .forEach(pit -> {
                    if (usedStones.get() < remainingStones) {
                        gameScore.replace(pit, gameScore.get(pit) + 1);
                        usedStones.getAndIncrement();
                        currentPit.set(pit);
                    }
                });

        if (usedStones.get() < remainingStones) {
            int startingPit = game.getActivePlayer() == 1 ? 1 - 1 : 8 - 1; // starting pit must be one less due to selected itereation
            currentPit.set(sowStones(game, startingPit, (remainingStones - usedStones.get())));
        }

        game.setScore(gameScore);
        game.setFinalPit(currentPit.get());

        return currentPit.get();
    }

    private void setWrapUpStrategy(Game game) {

        if (playerRunsOfStones(game)) {
            game.setWrapUpStrategy(new GameOver());
        } else if (playerFinishedOnKalah(game)) {
            game.setWrapUpStrategy(new FinishedOnKalah());
        } else if (playerFinishedOnEmptyPit(game)) {
            game.setWrapUpStrategy(new FinishedOnEmptyPit());
        } else {
            game.setWrapUpStrategy(new NextTurn());
        }

    }

    private boolean playerFinishedOnKalah(Game game) {
        return game.getActivePlayer() == 1 && game.getFinalPit() == 7 || game.getActivePlayer() == 2 && game.getFinalPit() == 14;
    }

    private boolean playerFinishedOnEmptyPit(Game game) {
        return game.getScore().get(game.getFinalPit()) == 1;
    }

    private boolean playerRunsOfStones(Game game) {
        return IntStream.rangeClosed(
                    game.getActivePlayer() == 1 ? 1 : 8,
                    game.getActivePlayer() == 1 ? 6 : 13)
                .map(game.getScore()::get)
                .sum() == 0;
    }

    private void wrapUpTurn(Game game) {
        game.getWrapUpStrategy().wrapUp(game);
    }

}
