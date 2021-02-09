package api;

import configuration.DataRepository;
import configuration.LocatorsRepository;
import configuration.ProjectConfiguration;
import configuration.SessionManager;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reporting.ReporterManager;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class BaseAPIClient {

    private static final Logger logger = LoggerFactory.getLogger(BaseAPIClient.class);
    public final static ReporterManager reporter = ReporterManager.Instance;
    public final static DataRepository dataRepository = DataRepository.Instance;
    private static String authToken = "";
    public static final HashMap<String,String> PARAMETERS =  getParameters();
    public static String defaultURL = PARAMETERS.get("API_DEFAULT_URL");


    private static HashMap<String,String> getParameters() {
        return  dataRepository.getDefaultParametersForTest("Api");
    }

    /**
     * Send GET request
     * @param url String
     * @return response object
     */
    public static Response runGetRequest(String url){
            logger.info("run request "+url);
        if (authToken.equals(""))
            authToken = APIAuthorization.getAccessToken();

        String finalQuery =url;

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

}
