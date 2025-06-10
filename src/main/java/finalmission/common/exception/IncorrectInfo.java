package finalmission.common.exception;

import org.springframework.http.HttpStatus;

public class IncorrectInfo extends CustomException{
    public IncorrectInfo(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
