import BunkerApi.Models.AuthDto;
import BunkerApi.Models.ErrorResponse;
import BunkerApi.Models.SecurityTokenDto;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static BunkerApi.Steps.RequestBuilder.getAuthHeader;
import static BunkerApi.Validations.RequestValidations.checkTokenFormat;
import static Utility.AssertExtensions.aggregate;
import static io.restassured.RestAssured.given;

/* Вопросы к заданию
    1) Спецификация не содержит вариантов ответов, поэтому все Asserts содержат сравнения с "пустышками"
    2) Все тесты падают т.к. API возращает только 200 ответ. Судя по отсутствию examples в описании он и не может
    что-то другое вернуть. Если задание предполагает сделать свою копию на триал версии дополнив, то сообщите пожалуйста.
    3) Некоторые тесты можно было бы объеденить под @DataProvider, но нужны точные ответы от API
    4) Авторизационный токен вроде в документации указан что есть, а дальше есть комментарий что он вроде как и не
    используется. Проверить без ответов от API не получается, то написать тест и на это.
    5) Отдельный класс для тестов, моделей есть, а вот чем наполнять шаги и проверки не понятно из задачи.
    Проверки ответов в отдельном классе не вижу смысла, т.к. каждый тест в теории будем иметь свои уникальные ответы
    6) Allure не добавляю т.к есть большие сомнения что я вообще понимаю правильно это задание. Я готов расширить и
    поправить проект после уточнения требований, свяжитесь со мной пожалуйста.
    7) Тестов на ошибку 500 не написал, т.к. нужно смотреть в код продукта чтобы понять какие данные могут вызвать
    такой ответ. В реальности такиих вариантов быть не должно и такие тесты стоит делать на уровне Unit. На самом деле
    почти все написанные тесты это проверка контракта API что нужно опустить на уровень Unit,
     и только 3 теста ниже выполняют реальную интеграционную проверку.
     - PostLogin_NoExistUser_Unauthorized
     - PostLogin_InvalidPassword_UnauthorizedOrBadRequest
     - PostLogin_CorrectUser_ReturnOk


 */

public class LoginTests {

    private static final String sutUrl = "https://virtserver.swaggerhub.com/roga88/bunker/1.0.0/login";
    private static RequestSpecification loginRequest;

    @BeforeClass
    public static void ClassInitialize(){
        loginRequest = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(sutUrl)
                .addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter())
                .build();
    }

    @Test
    public void PostLogin_LoginNotSet_BadRequest(){
        // Arrange
        AuthDto body = new AuthDto();
        body.password = "password";
        String token = "token";

        // Act
        ErrorResponse response = given()
                .spec(loginRequest)
                .header(getAuthHeader(token))
                .body(body)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .body().as(ErrorResponse.class);

        // Assert
        aggregate(new Runnable[]{
                () -> Assert.assertEquals(response.error, "Error not expected"),
                () -> Assert.assertEquals(response.message, "Message not expected"),
                () -> Assert.assertEquals(response.status, HttpStatus.SC_BAD_REQUEST),
                () -> Assert.assertEquals(response.timestamp, "?"),
        });
    }

    @Test
    public void PostLogin_PasswordNotSet_BadRequest(){
        // Arrange
        AuthDto body = new AuthDto();
        body.login = "login";
        String token = "token";

        // Act
        ErrorResponse response = given()
                .spec(loginRequest)
                .header(getAuthHeader(token))
                .body(body)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .body().as(ErrorResponse.class);

        // Assert
        aggregate(new Runnable[]{
                () -> Assert.assertEquals(response.error, "Error not expected"),
                () -> Assert.assertEquals(response.message, "Message not expected"),
                () -> Assert.assertEquals(response.status, HttpStatus.SC_BAD_REQUEST),
                () -> Assert.assertEquals(response.timestamp, "?"),
        });
    }

    @Test
    public void PostLogin_NoBody_BadRequest(){
        // Arrange
        String token = "token";

        // Act
        ErrorResponse response = given()
                .spec(loginRequest)
                .header(getAuthHeader(token))
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .extract()
                .body().as(ErrorResponse.class);

        // Assert
        aggregate(new Runnable[]{
                () -> Assert.assertEquals(response.error, "Error not expected"),
                () -> Assert.assertEquals(response.message, "Message not expected"),
                () -> Assert.assertEquals(response.status, HttpStatus.SC_BAD_REQUEST),
                () -> Assert.assertEquals(response.timestamp, "?"),
        });
    }

    @Test
    public void PostLogin_NoAuthHeader_Unauthorized(){
        // Arrange
        AuthDto body = new AuthDto();
        body.login = "login";
        body.password = "password";

        // Act
        ErrorResponse response = given()
                .spec(loginRequest)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract()
                .body().as(ErrorResponse.class);

        // Assert
        aggregate(new Runnable[]{
                () -> Assert.assertEquals(response.error, "Error not expected"),
                () -> Assert.assertEquals(response.message, "Message not expected"),
                () -> Assert.assertEquals(response.status, HttpStatus.SC_BAD_REQUEST),
                () -> Assert.assertEquals(response.timestamp, "?"),
        });
    }

    @Test
    public void PostLogin_AuthHeaderInvalidValue_Unauthorized(){
        // Arrange
        AuthDto body = new AuthDto();
        body.login = "login";
        body.password = "password";
        String token = "invalid value token";

        // Act
        ErrorResponse response = given()
                .spec(loginRequest)
                .header(getAuthHeader(token))
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract()
                .body().as(ErrorResponse.class);

        // Assert
        aggregate(new Runnable[]{
                () -> Assert.assertEquals(response.error, "Error not expected"),
                () -> Assert.assertEquals(response.message, "Message not expected"),
                () -> Assert.assertEquals(response.status, HttpStatus.SC_BAD_REQUEST),
                () -> Assert.assertEquals(response.timestamp, "?"),
        });
    }

    @Test
    public void PostLogin_NoExistUser_Unauthorized(){
        // Arrange
        AuthDto body = new AuthDto();
        body.login = "login of user that not exist";
        body.password = "password";
        String token = "token";

        // Act
        ErrorResponse response = given()
                .spec(loginRequest)
                .header(getAuthHeader(token))
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract()
                .body().as(ErrorResponse.class);

        // Assert
        aggregate(new Runnable[]{
                () -> Assert.assertEquals(response.error, "Error not expected"),
                () -> Assert.assertEquals(response.message, "Message not expected"),
                () -> Assert.assertEquals(response.status, HttpStatus.SC_BAD_REQUEST),
                () -> Assert.assertEquals(response.timestamp, "?"),
        });
    }

    @Test
    public void PostLogin_InvalidPassword_UnauthorizedOrBadRequest(){
        // Arrange
        AuthDto body = new AuthDto();
        body.login = "login";
        body.password = "not user password";
        String token = "token";

        // Act
        ErrorResponse response = given()
                .spec(loginRequest)
                .header(getAuthHeader(token))
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .extract()
                .body().as(ErrorResponse.class);

        // Assert
        aggregate(new Runnable[]{
                () -> Assert.assertEquals(response.error, "Error not expected"),
                () -> Assert.assertEquals(response.message, "Message not expected"),
                () -> Assert.assertEquals(response.status, HttpStatus.SC_BAD_REQUEST),
                () -> Assert.assertEquals(response.timestamp, "?"),
        });
    }

    @Test
    public void PostLogin_CorrectUser_ReturnOk(){
        // Arrange
        AuthDto body = new AuthDto();
        body.login = "login";
        body.password = "password";
        String token = "token";

        // Act
        SecurityTokenDto response = given()
                .spec(loginRequest)
                .header(getAuthHeader(token))
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .body().as(SecurityTokenDto.class);

        // Assert
        aggregate(new Runnable[]{
                () -> Assert.assertEquals(response.login, body.login),
                () -> Assert.assertTrue(checkTokenFormat(response.token)),
                () -> Assert.assertTrue(checkTokenFormat(response.refreshToken)),
                () -> Assert.assertEquals(response.expiredAt, "?"),
        });
    }
}