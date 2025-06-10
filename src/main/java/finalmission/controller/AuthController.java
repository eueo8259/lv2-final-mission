package finalmission.controller;

import finalmission.dto.login.LoginRequest;
import finalmission.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> userLogin(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        String token = authService.loginAndGenerateToken(loginRequest);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return Map.of("message", "로그인에 성공하였습니다.");
    }
}
