package finalmission.common.ui;

import finalmission.common.annotation.LoginCustomerId;
import finalmission.dto.login.LoginInfo;
import finalmission.common.exception.NotFountException;
import finalmission.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginCustomerResolver implements HandlerMethodArgumentResolver {

    private final AuthService authService;

    public LoginCustomerResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        LoginCustomerId auth = parameter.getParameterAnnotation(LoginCustomerId.class);
        return auth != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies).orElse("");
        Optional<LoginInfo> byToken = authService.findByToken(token);
        if (byToken.isEmpty()){
            throw new NotFountException("존재하지 않는 유저정보입니다");
        }
        return byToken.get();
    }

    private Optional<String> extractTokenFromCookie(Cookie[] cookies) {
        if (cookies == null){
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return Optional.ofNullable(cookie.getValue());
            }
        }
        return Optional.empty();
    }
}
