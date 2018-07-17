/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teem.loginapp.serviceImpl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import teem.loginapp.pojo.ADResponse;
import teem.loginapp.service.ActiveDirectoryService;

/**
 *
 * @author nikos
 */
@Service
public class ActiveDirectoryServiceImpl implements ActiveDirectoryService {

    private final String USER_AGENT = "Mozilla/5.0";

    private final static Logger log = LoggerFactory.getLogger(ActiveDirectoryServiceImpl.class);

    @Value("${ad.microserv}")
    private String adMicroUrl;

    public boolean createUser(String userEmail) throws MalformedURLException, IOException {
        String url = adMicroUrl + "/register?email=" + userEmail; //"http://localhost:8000/register?email="+userEmail;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        log.info("\nSending 'GET' request to URL : " + url);
        log.info("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        //print result
        log.info(response.toString());

        return false;
    }

    @Override
    public ADResponse createUser(String displayName, String mailNickname, String givenName, String surname, 
            String userPrincipalName, String password, String eId) throws MalformedURLException, IOException {
        String url = adMicroUrl + "/createUser";

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("displayName", displayName);
        params.put("mailNickname", mailNickname);
        params.put("givenName", givenName);
        params.put("surname", surname);
        params.put("userPrincipalName", userPrincipalName);
        params.put("password", password);
        params.put("eId",eId);
        String response = writeParamsAndSendPost(params, url);
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(response.toString(), ADResponse.class);
    }

    @Override
    public ADResponse createGroup(String displayName, String mailNickname) throws MalformedURLException, IOException {
        String url = adMicroUrl + "/createGroup";
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("displayName", displayName);
        params.put("mailNickname", mailNickname);
        String response = writeParamsAndSendPost(params, url);
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(response.toString(), ADResponse.class);
    }

    @Override
    public ADResponse createTeam(String groupId) throws MalformedURLException, IOException {
        String url = adMicroUrl + "/createTeam";

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("groupId", groupId);
        String response = writeParamsAndSendPost(params, url);
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(response.toString(), ADResponse.class);
    }

    @Override
    public ADResponse sendInvite(String userEmail, String redirectURL, String invitedUserDisplayName) throws MalformedURLException, IOException {
        String url = adMicroUrl + "/sendInvite";
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("userEmail", userEmail);
        params.put("redirectURL", redirectURL);
        params.put("invitedUserDisplayName", invitedUserDisplayName);

        String response = writeParamsAndSendPost(params, url);
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(response.toString(), ADResponse.class);
    }

    @Override
    public ADResponse add2Group(String userId, String groupName, boolean isOwner) throws MalformedURLException, IOException {
        String url = adMicroUrl + "/add2Group";
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("userId", userId);
        params.put("groupName", groupName);
        params.put("isOwner", isOwner);

        String response = writeParamsAndSendPost(params, url);
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(response.toString(), ADResponse.class);
    }

    @Override
    public ADResponse updateUserAttribute(String userId, String attributeName, String attributeValue) throws MalformedURLException, IOException {
        String url = adMicroUrl + "/updateUser";
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("userId", userId);
        params.put("attributeName", attributeName);
        params.put("attributeValue", attributeValue);

        String response = writeParamsAndSendPost(params, url);
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(response.toString(), ADResponse.class);
    }

    /**
     * Writes the provides map of parameters as post parameters in the
     * connection body sends the POST request to the url and returns the
     * response
     *
     * @param params
     * @param con
     */
    private String writeParamsAndSendPost(Map<String, Object> params, String url) throws UnsupportedEncodingException,
            ProtocolException, IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        StringBuilder postData = new StringBuilder();
        params.forEach((key, value) -> {
            try {
                if (postData.length() != 0) {
                    postData.append('&');
                }
                postData.append(URLEncoder.encode(key, "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                log.info("ERROR" + ex.getMessage());
            }
        });
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        con.setRequestMethod("POST");
        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        con.setDoOutput(true);
        con.getOutputStream().write(postDataBytes);

        int responseCode = con.getResponseCode();

        log.info("\nSending 'POST' request to URL : " + url);
        log.info("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        log.info(response.toString());
        return response.toString();
    }

}
