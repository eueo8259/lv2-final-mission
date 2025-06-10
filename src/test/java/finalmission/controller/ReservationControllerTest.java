package finalmission.controller;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.common.JwtProvider;
import finalmission.entity.Customer;
import finalmission.repository.CustomerRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
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
    void 로그인한_사용자의_예약_생성_실패_테스트() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("date", String.valueOf(LocalDate.now().plusDays(1)));
        params.put("time", "16:00");
        Customer customer = new Customer(2L, "202020@gmail.com", "add");

        JwtProvider jwtProvider = new JwtProvider();
        String userToken = jwtProvider.createToken(customer);

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", userToken)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }


    @DisplayName("로그인한 유저의 정보를 찾을 수 있다면 예약을 생성할 때 200을 반환한다")
    @Test
    void 로그인한_사용자의_예약_생성_성공_테스트() {
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
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("로그인한 사용자의 전체 예약 조회")
    @Test
    void 로그인한_사용자의_예약_조회_테스트() {
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

        // when
        ExtractableResponse<Response> extract = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", userToken)
                .when().get("/reservations/mine")
                .then().log().all()
                .statusCode(200)
                .extract();

        // then
        List<?> reservations = extract.body().as(List.class);
        assertThat(reservations).hasSize(1);
    }
}
