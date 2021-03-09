package com.backbase.kalah.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IllegalMoveException extends RuntimeException {

    public IllegalMoveException(String message) {
        super(message);
    }

}
