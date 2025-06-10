package finalmission.controller;

import finalmission.common.JwtProvider;
import finalmission.entity.Customer;
import finalmission.repository.CustomerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @Autowired
    private CustomerRepository customerRepository;
    Customer customer = new Customer("user@email.com", "사용자");


    @DisplayName("로그인한 유저의 정보를 찾을 수 없다면 예약을 생성할 때 404에러를 반환한다")
    @Test
    void 로그인한_사용자의_예약_생성_테스트() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("date", String.valueOf(LocalDate.now().plusDays(1)));
        params.put("time", "16:00");
        Customer saveCustomer = customerRepository.save(customer);

        JwtProvider jwtProvider = new JwtProvider();
        String userToken = jwtProvider.createToken(saveCustomer);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", userToken)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);
    }
}
