package api;

import configuration.ProjectConfiguration;
import datasources.JSONConverter;
import reporting.ReporterManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.config.RedirectConfig.redirectConfig;

/**
 * Basic REST client for API testing <br>
 *     based on RestAssured
 */
public class BaseRestClient {

    ReporterManager reporter = ReporterManager.Instance;

    /**
     * Send GET request
     * @param requestURL request URL
     * @param headers map of headers
     * @return response object
     */
    public Response getRequest(String requestURL, Object headers, Object cookies) {
        reporter.info("GET URL: " + requestURL);
        if(headers == null || headers.equals(""))
            headers = new HashMap<>();
        else
            if(headers instanceof String)
                headers = JSONConverter.toHashMapFromJsonString((String) headers);
        if(cookies == null || cookies.equals(""))
            cookies = new HashMap<>();
        else
            if(cookies instanceof String)
                cookies = JSONConverter.toHashMapFromJsonString((String) cookies);

        String creds = requestURL.replaceAll("(http|https)://(.*?:.*?)@.*", "$2");

        Response response;
        RequestSpecification responseSpecification;
        if(!creds.equals(requestURL)) // credentials were found
            responseSpecification = given().auth().basic(creds.split(":")[0], creds.split(":")[1]);
        else
            responseSpecification = given();

        if(headers instanceof Headers)
            responseSpecification = responseSpecification.headers((Headers)headers);
        else
            responseSpecification = responseSpecification.headers((HashMap<String,String>)headers);

        if(cookies instanceof Cookies)
            responseSpecification = responseSpecification.cookies((Cookies)cookies);
        else
            responseSpecification = responseSpecification.cookies((Map<String,String>)cookies);

        response = responseSpecification.when()
                    //.contentType(ContentType.JSON)
                    .get(requestURL)
                    .then()
                    .extract()
                    .response();

            return response;
    }

    /**
     * Send GET request
     * @param requestURL request URL
     * @param headers map of headers
     * @return response object
     */
    public Response getRequest(String requestURL, Map<String,String> headers) {
        reporter.info("GET URL: " + requestURL);
        if(headers == null)
            headers = new HashMap<>();
        String creds = requestURL.replaceAll("(http|https)://(.*?:.*?)@.*", "$2");
        if(!creds.equals(requestURL)){ // credentials were found
            Response response = given()
                    .auth().basic(creds.split(":")[0], creds.split(":")[1])
                    .headers(headers)
                    .when()
                    .contentType(ContentType.JSON)
                    .get(requestURL)
                    .then()
                    .extract()
                    .response();

            return response;
        } else {
            Response response = given()
                    .headers(headers)
                    .when()
                    .contentType(ContentType.JSON)
                    .get(requestURL)
                    .then()
                    .extract()
                    .response();

            return response;
        }
    }

    /**
     * Send GET request
     * @param requestURL request URL
     * @param headers map of headers
     * @return response object
     */
    public Response getRequest(String requestURL, Headers headers) {
        reporter.info("GET URL: " + requestURL);

        String creds = requestURL.replaceAll("(http|https)://(.*?:.*?)@.*", "$2");
        if(!creds.equals(requestURL)){ // credentials were found
            Response response = given()
                    .auth().basic(creds.split(":")[0], creds.split(":")[1])
                    .headers(headers)
                    .when()
                    .contentType(ContentType.JSON)
                    .get(requestURL)
                    .then()
                    .extract()
                    .response();

            return response;
        } else {
            Response response = given()
                    .headers(headers)
                    .when()
                    .contentType(ContentType.JSON)
                    .get(requestURL)
                    .then()
                    .extract()
                    .response();

            return response;
        }
    }

    /**
     * Send POST reques
     * @param requestURL request URL
     * @param body request body
     * @param headers map of headers
     * @return response object
     */
    public Response postRequest(String requestURL, String body, Map<String,String> headers) {
        String finalURL = requestURL;
        if (headers == null)
            headers = new HashMap<>();

        if (isJson(body)){ //json{

            reporter.info("POST URL: " + finalURL);
            Response response = given()
                    .headers(headers)
                    .body(body)
                    .when().redirects().follow(true)
                    .contentType(ContentType.JSON)
                    .config(RestAssured.config().redirect(redirectConfig().followRedirects(false)))
                    .post(finalURL)
                    .then()
                    .extract()
                    .response();
            return response;
        } else { // parameters
            reporter.info("POST URL (with parameters): " + finalURL);
            Map<String,String> parameters = new HashMap<>();
            Stream<String> s = Stream.of(body.split(";"));
            parameters = s.collect(Collectors.toMap(it -> it.split("=")[0], it -> it.split("=")[1]));
            Response response = given()
                    .headers(headers)
                    .params(parameters)
                    .when().redirects().follow(true)
                    .contentType(ContentType.JSON)
                    .config(RestAssured.config().redirect(redirectConfig().followRedirects(false)))
                    .post(finalURL)
                    .then()
                    .extract()
                    .response();
            return response;
        }

    }

    /**
     * Send POST request
     * @param requestURL request URL
     * @param body request body
     * @param headers map of headers
     * @param type content type
     * @return response object
     */
    public Response postRequest(String requestURL, String body, Map<String,String> headers, ContentType type) {
        if(headers == null)
            headers = new HashMap<>();
        reporter.info("POST URL: " + requestURL);
        Response response = given()
                .headers(headers)
                .body(body)
                .when()
                .contentType(type)
                .post(requestURL)
                .then()
                .extract()
                .response();
        return response;
    }

    /**
     * Send DELETE request
     * @param requestURL request URL
     * @param body request body
     * @param headers map of headers
     * @return response object
     */
    public Response deleteRequest(String requestURL, String body, Map<String,String> headers) {
        if(headers == null)
            headers = new HashMap<>();
        reporter.info("DELETE URL: " + requestURL);
        Response response = given()
                .headers(headers)
                .body(body)
                .when()
                .contentType(ContentType.JSON)
                .delete(requestURL)
                .then()
                .extract()
                .response();
        return response;
    }

    /**
     * Upload POST request
     * @param requestURL request URL
     * @param file file
     * @param headers map of headers
     * @return response object
     */
    public Response uploadPostRequest(String requestURL, File file, Map<String,String> headers, Map<String,String> parameters) {
        String finalURL = requestURL;
        if(headers == null)
            headers = new HashMap<>();
        if(parameters == null)
            parameters = new HashMap<>();

        reporter.info("POST URL: " + finalURL);
        Response response = given()
                .formParams(parameters)
                .headers(headers)
                .when()
                .multiPart(file)
                .post(finalURL)
                .then()
                .extract()
                .response();
        return response;
    }

    /**
     * check if request data looks like JSON
     * @param data
     * @return
     */
    private boolean isJson(String data) {
        return data.startsWith("{");
    }

//TODO move to another class
    /**
     * Send post request to Login an process redirections
     * @param targetUrl
     * @return
     */
    public Map<String,String> loginToAdminAndGetCookies(String targetUrl) {
        //send initial request
        Response initialResponse = null;
        initialResponse = given().auth().basic(ProjectConfiguration.getConfigProperty("CLIENT_USER"), ProjectConfiguration.getConfigProperty("CLIENT_PASSWORD"))
                .get(targetUrl)
                .then()
                .extract()
                .response();

        String key = (String) initialResponse.htmlPath().get("**.findAll {it.@name=='form_key'}.@value");
        Map<String, String> cookies = initialResponse.getCookies();

        //send main request
        Response response = given().auth().basic(ProjectConfiguration.getConfigProperty("CLIENT_USER"), ProjectConfiguration.getConfigProperty("CLIENT_PASSWORD"))
                .formParam("login[username]", ProjectConfiguration.getConfigProperty("ADMIN_USER"))
                .formParam("login[password]", ProjectConfiguration.getConfigProperty("ADMIN_PASSWORD"))
                .formParam("form_key", key)
                .cookies(initialResponse.cookies())
                .post(targetUrl)
                .then()
                .extract()
                .response();


        //redirect to new location
        Response result = given().auth().basic(ProjectConfiguration.getConfigProperty("CLIENT_USER"), ProjectConfiguration.getConfigProperty("CLIENT_PASSWORD"))
                .cookies(response.cookies())
                .get(targetUrl)
                .then()
                .extract()
                .response();

        return result.getCookies();
    }
}
