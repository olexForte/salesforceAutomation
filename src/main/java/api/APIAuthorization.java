package api;
import configuration.ProjectConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.util.Base64;

public class APIAuthorization {

    public static String accessTokenURL ="https://test.salesforce.com/services/oauth2/token";
    public static String clientId = ProjectConfiguration.getConfigProperty("API_CLIENT_ID");
    private static String clientSecret = ProjectConfiguration.getConfigProperty("API_CLIENT_SECRET");
    private static String refreshToken = ProjectConfiguration.getConfigProperty("API_REFRESH_TOKEN");
    public static String grant_type = "refresh_token";

    private static String getToken() {

        Response resp =
                given()
                        .formParam("client_id", clientId)
                        .formParam("client_secret", clientSecret)
                        .formParam("refresh_token", refreshToken)
                        .formParam("grant_type", grant_type)
                        .post(accessTokenURL)
                        .then()
                        .extract()
                        .response();

        return parseForAccessToken(resp);
    }

    private static String parseForAccessToken(Response loginResponse) {
        return loginResponse.jsonPath().getString("access_token");
    }

    public static String getAccessToken(){
        return getToken();
    }

}