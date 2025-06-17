package finalmission.controller;

import static org.assertj.core.api.Assertions.assertThat;

import finalmission.common.cache.MonthlyHolidayCache;
import finalmission.common.ui.JwtProvider;
import finalmission.entity.Customer;
import finalmission.entity.Reservation;
import finalmission.repository.CustomerRepository;
import finalmission.repository.ReservationRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MonthlyHolidayCache cache;

    @BeforeEach
    void setUp() {
        cache.refreshMonthlyHolidays();
    }


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
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("로그인한 유저가 공휴일에 예약을 생성할 때 400을 반환한다")
    @Test
    void 로그인한_사용자의_공휴일_예약_실패_테스트() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("date", String.valueOf(cache.getCachedHolidays().getFirst()));
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
                .statusCode(400);
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

    @DisplayName("로그인한 사용자의 예약 삭제")
    @Test
    void 로그인한_사용자의_예약_삭제_테스트() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("date", String.valueOf(LocalDate.now().plusDays(1)));
        params.put("time", "16:00");
        Customer saveCustomer = customerRepository.save(customer);

        JwtProvider jwtProvider = new JwtProvider();
        String userToken = jwtProvider.createToken(saveCustomer);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", userToken)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", userToken)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("사용자 본인의 예약 삭제의 경우 400을 던진다")
    @Test
    void 로그인한_사용자의_예약_삭제_실패_테스트() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("date", String.valueOf(LocalDate.now().plusDays(1)));
        params.put("time", "16:00");
        Customer firstCustomer = customerRepository.save(customer);
        Customer secondCustomer = customerRepository.save(new Customer("aaa@naver.com", "sosos"));

        reservationRepository.save(new Reservation(secondCustomer, LocalDate.now(), LocalTime.now()));

        JwtProvider jwtProvider = new JwtProvider();
        String userToken = jwtProvider.createToken(firstCustomer);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", userToken)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(400);
    }
}
