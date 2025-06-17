package finalmission.common.exception;

import org.springframework.http.HttpStatus;

public class HolidayException extends CustomException {
    public HolidayException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
