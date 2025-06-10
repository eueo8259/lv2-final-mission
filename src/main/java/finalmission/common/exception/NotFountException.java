package finalmission.common.exception;

import org.springframework.http.HttpStatus;

public class NotFountException extends CustomException{
    public NotFountException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
