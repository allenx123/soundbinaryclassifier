package io.kloyd.soundbinaryclassifier.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Util {
	
	public static int callOS(String[] command) {
		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
			return process.exitValue();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	//code = 4%2F0AY0e-g7SIoxYtH9EOVGf2G3iiht6TzXAqiF4w3vRDR5_AgDvx8Na4lcRqtR-vsC7XXTJdA
	public static String getUserInfo(String accessToken) {
		
		HttpClient client = HttpClientBuilder.create().build();
		String url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json";
		
		try {
			
			HttpGet request = new HttpGet(url);
			request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
			request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+accessToken);

			
			RequestConfig requestConfig = RequestConfig.custom()
					  .setSocketTimeout(10*60*1000)
					  .setConnectTimeout(10*60*1000)
					  .setConnectionRequestTimeout(10*60*1000)
					  .build();
			
			request.setConfig(requestConfig);
			
			HttpResponse response = client.execute(request);
			//check the status code
	        int status = response.getStatusLine().getStatusCode();
		    HttpEntity entity = response.getEntity();
	       	String output = entity!= null ? EntityUtils.toString(entity) : null; 
	       	JSONParser parser = new JSONParser();
			JSONObject jobj = (JSONObject) parser.parse(output);
			String id = (String) jobj.get("id");
			String email = (String) jobj.get("email");
			
			if (id==null || email==null) {
				return null;
			}
	       	
	       	return "{\"id\":\""+id+"\",\"email\":\""+email+"\"}";
	        
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static String getAccessToken(String code) {

		HttpClient client = HttpClientBuilder.create().build();
		String url = "https://oauth2.googleapis.com/token?";
		url += "client_id=694757928711-2oa5reovr0gfu5plbaqac2g46ct49hs5.apps.googleusercontent.com&";
		url += "client_secret=XXXXXXXXXXXXXXXXXXXXXXXX&";
		url += "redirect_uri=http%3A%2F%2Fwww.e2college.com&";
		url += "grant_type=authorization_code&";
		url += "code="+code;
		
		//execute the http get request
		try {
			
			HttpPost request = new HttpPost(url);
			request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
			
			RequestConfig requestConfig = RequestConfig.custom()
					  .setSocketTimeout(10*60*1000)
					  .setConnectTimeout(10*60*1000)
					  .setConnectionRequestTimeout(10*60*1000)
					  .build();
			
			request.setConfig(requestConfig);
			
			HttpResponse response = client.execute(request);
			//check the status code
	        int status = response.getStatusLine().getStatusCode();
		    HttpEntity entity = response.getEntity();
	       	String output = entity!= null ? EntityUtils.toString(entity) : null; 
	       	JSONParser parser = new JSONParser();
			JSONObject jobj = (JSONObject) parser.parse(output);
			String accessToken = (String) jobj.get("access_token");
	       	
	       	return accessToken;
	        
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
       	return null;
		
	}
	
	public static void main(String[] args) {
		/*
		Scanner sc = new Scanner(System.in);
		System.out.println("Input code: ");
		String code = sc.nextLine();
		String accessToken = getAccessToken(code);
		System.out.println(accessToken);
		String id = getUserInfo(accessToken);
		System.out.println(id);
		*/
	}

}
