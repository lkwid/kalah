package com.backbase.kalah.model.game.turn;

import com.backbase.kalah.model.game.Game;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NextTurn implements WrapUpStrategy {

    @Override
    public void wrapUp(Game game) {

        log.info("Player {} finished his turn", game.getActivePlayer());

        game.setActivePlayer((byte) (game.getActivePlayer() == 1 ? 2 : 1));
    }

}
