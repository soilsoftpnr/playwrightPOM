package automate.api.methods;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.entity.ContentType;

public class ApiMethods {

	
	
	
	public static JsonNode POSTAPI(String endPoint,String payload,String...headers) {

		ObjectMapper objectMapper = new ObjectMapper();
	    JsonNode responseNode=null;
        // Create a custom request configuration with timeout settings
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000) // Connection timeout
                .setConnectionRequestTimeout(5000) // Connection request timeout
                .setSocketTimeout(5000) // Socket timeout
                .build();

        // Create an HTTP client with the custom request configuration
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
    
        try {
            // Create an HTTP POST request
            HttpPost postRequest = new HttpPost(endPoint);

            // Set the request body and content type
            StringEntity entity = new StringEntity(payload, ContentType.APPLICATION_JSON);
            postRequest.setEntity(entity);
        	for (int header = 0; header < headers.length; header++) {
				
				postRequest.setHeader("Bearer",headers[header]);
			}
            // Execute the request
            HttpResponse response = httpClient.execute(postRequest);

            // Check the response
            int statusCode = response.getStatusLine().getStatusCode();
            byte[] responseBytes = EntityUtils.toByteArray(response.getEntity());

            // Convert the byte array to a string (assuming the response content is in UTF-8 encoding)
            String responseString = new String(responseBytes, "UTF-8");

        	responseNode = objectMapper.readTree(responseString);
            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response: " + responseBody);
            } else {
                System.err.println("Request failed with code: " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return responseNode;
	}
	
	

  
        
        public static JsonNode GETAPI(String endPoint,String payload,String...headers) { 
        	
        	
        	
        	ObjectMapper objectMapper = new ObjectMapper();
    	    JsonNode responseNode=null;
        	
        	RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000) // Connection timeout
                .setConnectionRequestTimeout(5000) // Connection request timeout
                .setSocketTimeout(5000) // Socket timeout
                .build();

        // Create an HTTP client with the custom request configuration
        HttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        try {
            // Create an HTTP GET request
            HttpGet getRequest = new HttpGet(endPoint);

            // Execute the request
            HttpResponse response = httpClient.execute(getRequest);

            // Check the response
            int statusCode = response.getStatusLine().getStatusCode();
            byte[] responseBytes = EntityUtils.toByteArray(response.getEntity());

            // Convert the byte array to a string (assuming the response content is in UTF-8 encoding)
            String responseString = new String(responseBytes, "UTF-8");

        	responseNode = objectMapper.readTree(responseString);
       
           
            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response: " + responseBody);
            } else {
                System.err.println("Request failed with code: " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            }
        return responseNode;
        }
}
