package api;

import configuration.DataRepository;
import configuration.LocatorsRepository;
import configuration.SessionManager;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reporting.ReporterManager;

import java.util.List;

import static io.restassured.RestAssured.given;

public class BaseAPIClient {

    private static final Logger logger = LoggerFactory.getLogger(BaseAPIClient.class);

    public final static ReporterManager reporter = ReporterManager.Instance;
    public final static DataRepository dataRepository = DataRepository.Instance;

    private static String authToken = "";

    public static Response runQuery(String query){

        if (authToken.equals(""))
            authToken = APIAuthorization.getAccessToken();

        authToken="00D7f0000000r4e!AQ4AQIl3amuov26909ZGseN9M7d1QKkZ1.0RbWfUNa0lA6Cty2xg19ywVRMg5Q8Hg2tAp_69Mo6iD5VvZIozdMe9ct9wPBwK";
        String finalQuery =query;// (locatorsRepository.getURL("apiURLs", "API_QUERY_URL") + query);

        Response response = given()
                .auth().oauth2(authToken)
                .when()
                .get(finalQuery)
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();

       String test= response.body().asString();

        return response;

    }



//  // public String getValueByJsonPath(Response resp, String expected_json_path) {
//
//        return resp.jsonPath().getString(expected_json_path);
//
//
//    }

    public List<String> getListValueByJsonPath(Response resp, String expected_json_path)
    {
        return  resp.jsonPath().getList(expected_json_path);
    }
}
