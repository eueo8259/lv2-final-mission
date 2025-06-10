package finalmission.service;

import finalmission.common.JwtProvider;
import finalmission.dto.login.LoginInfo;
import finalmission.dto.login.LoginRequest;
import finalmission.common.exception.UnauthorizedException;
import finalmission.repository.CustomerRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final JwtProvider jwtProvider;


    public String loginAndGenerateToken(LoginRequest request) {
        return customerRepository.findByEmail(request.email())
                .map(jwtProvider::createToken)
                .orElseThrow(() -> new UnauthorizedException("존재하지 않는 email 혹은 틀린 password 입니다."));
    }

    public Optional<LoginInfo> findByToken(String token) {
        return Optional.ofNullable(jwtProvider.getSubjectFromToken(token))
                .flatMap(customerRepository::findById)
                .map(customer -> new LoginInfo(customer.getId()));
    }
}
