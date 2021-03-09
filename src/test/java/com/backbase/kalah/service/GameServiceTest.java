package com.backbase.kalah.service;

import com.backbase.kalah.model.game.Game;
import com.backbase.kalah.model.game.StandardRulesGame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    private static Stream<Arguments> newGame() {
        return Stream.of(
                Arguments.of(new StandardRulesGame())
        );
    }

    private static Stream<Arguments> finishedOnEmptyPitScenario() {
        Game game = new StandardRulesGame();
        game.getScore().replace(1, 0);
        game.getScore().replace(6, 8);
        return Stream.of(
                Arguments.of(game)
        );
    }

    private static Stream<Arguments> gameOverScenario() {
        Game game = new StandardRulesGame();
        game.getScore().replace(1, 0);
        game.getScore().replace(2, 0);
        game.getScore().replace(3, 0);
        game.getScore().replace(4, 0);
        game.getScore().replace(5, 0);
        game.getScore().replace(6, 1);
        game.getScore().replace(7, 35);
        return Stream.of(
                Arguments.of(game)
        );
    }

    @ParameterizedTest
    @MethodSource("newGame")
    @DisplayName("Should create a new game with a standard set of rules (6 markers in each pit)")
    void testNewGameCreation(StandardRulesGame game) {
        assertAll(
                () -> assertEquals(12 * 6, game.getScore().entrySet().stream()
                        .filter(entry -> !List.of(7, 14).contains(entry.getKey()) || entry.getValue() != 6)
                        .mapToInt(Map.Entry::getValue)
                        .sum(), () -> "Wrong number of markers set"),
                () -> assertTrue(
                        game.getScore().get(7) == 0 && game.getScore().get(14) == 0,
                        "Kalah pits should be empty")
        );
    }

    @ParameterizedTest
    @MethodSource("newGame")
    @DisplayName("Finishing on Kalah should give player another turn")
    void testFinishingOnKalah(StandardRulesGame game) {
        GameService.runningGames.put(game.getId(), game);
        GameService service = new GameService();

        Game result = service.makeMove(game.getId(), 1);
        assertAll(
                () -> assertEquals(1, result.getScore().get(7), "No score in Kalah"),
                () -> assertEquals(1, (byte) result.getActivePlayer(), "Player 1 should get one more turn")

        );

    }

    @ParameterizedTest
    @MethodSource("finishedOnEmptyPitScenario")
    @DisplayName("Finishing on empty pit should collect stones from the opposite pit")
    void testFinishingOnEmptyPit(StandardRulesGame game) {
        GameService.runningGames.put(game.getId(), game);
        GameService service = new GameService();

        Game result = service.makeMove(game.getId(), 6);
        assertAll(
                () -> assertEquals(9, result.getScore().get(7), "Wrong score in Kalah"),
                () -> assertEquals(0, result.getScore().get(1), "Wrong score in the own, empty pit"),
                () -> assertEquals(0, result.getScore().get(13), "Wrong score in the opposite pit"),
                () -> assertEquals(2, (byte) result.getActivePlayer(), "It should be player 2 turn now")
        );
    }

    @ParameterizedTest
    @MethodSource("gameOverScenario")
    @DisplayName("Having no stones should end the game")
    void testGameEnd(StandardRulesGame game) {
        GameService.runningGames.put(game.getId(), game);
        GameService service = new GameService();

        Game result = service.makeMove(game.getId(), 6);
        assertAll(
                () -> assertEquals(36, result.getScore().get(7), "Player 1 score should be 36"),
                () -> assertEquals(36, result.getScore().get(14), "Player 2 score should be 36"),
                () -> assertFalse(result.getStatus())
        );

    }

}
