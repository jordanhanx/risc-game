package edu.duke.ece651.team7.client.Model;

//import edu.duke.ece651.team7.client.Controller.gameBeginController;

import java.util.List;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;

/*
public class GameModel {
    public List<GameDto> responseObj;

    public GameModel(String userName, String passWord){
        // create a RestTemplate object
        RestTemplate restTemplate = new RestTemplate();

        // create the request body as a MultiValueMap
        MultiValueMap<String, String> loginRequestBody = new LinkedMultiValueMap<>();
        loginRequestBody.add("username", userName);
        loginRequestBody.add("password", passWord);

        // set the Content-Type header to application/x-www-form-urlencoded
        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // create the request entity with the headers and request body
        HttpEntity<MultiValueMap<String, String>> loginRequestEntity = new HttpEntity<>(loginRequestBody, loginHeaders);

        // send the login request and get the response
        ResponseEntity<String> loginResponse = restTemplate.exchange("http://vcm-30706.vm.duke.edu:8080/api/login",
                HttpMethod.POST,
                loginRequestEntity, String.class);

        // get the session cookie from the response headers
        String sessionCookie = loginResponse.getHeaders().getFirst("Set-Cookie");

        // create a headers object with the session cookie
        HttpHeaders queryHeaders = new HttpHeaders();
        queryHeaders.add("Cookie", sessionCookie);

        // send a request with the session cookie in the headers and get the response
        HttpEntity<String> euqryRequestEntity = new HttpEntity<>(queryHeaders);
        ResponseEntity<List<GameDto>> response = restTemplate.exchange(
                "http://vcm-30706.vm.duke.edu:8080/api/riscgame/all",
                HttpMethod.GET,
                euqryRequestEntity, new ParameterizedTypeReference<List<GameDto>>() {
                });

        // get the response body
        this.responseObj = response.getBody();
    }

}


*/
