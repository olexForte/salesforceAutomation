package api;

import configuration.LocatorsRepository;
import configuration.SessionManager;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reporting.ReporterManager;

import static io.restassured.RestAssured.given;

public class BaseAPIClient {

    private static final Logger logger = LoggerFactory.getLogger(BaseAPIClient.class);

    public final static ReporterManager reporter = ReporterManager.Instance;
    public final static LocatorsRepository locatorsRepository = LocatorsRepository.Instance;

    String authToken = "";

    public Response runQuery(String query){

        if (authToken.equals(""))
            authToken = APIAuthorization.getAccessToken();

        String finalQuery = locatorsRepository.getURL("apiURLs", "API_QUERY_URL") + query;

        Response response = given()
                .auth().oauth2(authToken)
                .when()
                //.contentType(ContentType.JSON)
                .get(finalQuery)
                .then()
                .extract()
                .response();

        return response;
    }

    public String getValueByJsonPath(Response resp, String expected_json_path) {
        return resp.jsonPath().getString(expected_json_path);
    }
}
