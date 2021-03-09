package com.backbase.kalah.model.game.turn;

import com.backbase.kalah.model.game.Game;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FinishedOnKalah implements WrapUpStrategy {

    @Override
    public void wrapUp(Game game) {

        log.info("Player finished his turn on Kalah. Player gets another turn");

    }
}
