package io.kloyd.soundbinaryclassifierdownloader.util;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import io.kloyd.soundbinaryclassifierdownloader.models.Train;
import io.kloyd.soundbinaryclassifierdownloader.repositories.OrganizationRepository;
import io.kloyd.soundbinaryclassifierdownloader.repositories.TrainRepository;

import org.apache.http.Header;
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
	
	public static String downloadData(String shareLink, String id) {
		
		HttpClient client = HttpClientBuilder.create().build();
		String[] temp = shareLink.split("/");
		String fileID = temp[5];
		
		String url = "https://drive.google.com/uc?export=download&id="+fileID;
		
		try {
			
			HttpGet request = new HttpGet(url);
			
			RequestConfig requestConfig = RequestConfig.custom()
					  .setSocketTimeout(10*60*1000)
					  .setConnectTimeout(10*60*1000)
					  .setConnectionRequestTimeout(10*60*1000)
					  .build();
			
			request.setConfig(requestConfig);
			
			HttpResponse response = client.execute(request);
			//check the status code
	        int status = response.getStatusLine().getStatusCode();
	        
	        Header[] headers = response.getHeaders("Content-Type");
	        Header header = headers[0];
	        String str = header.getValue();
	        
		    File file = new File("/mydata/soundbinaryclassifier_project/data/"+id);
		    file.mkdir();
	        
		    //if initial GET request doesn't successfully get back the zip file
	        if (str.equals("text/html; charset=utf-8")) {
	        	
			    HttpEntity entity = response.getEntity();
			    
		       	String output = entity!= null ? EntityUtils.toString(entity) : null;
		       	String[] arr = output.split(";");
		       	output = arr[18];
		       	arr = output.split("&");
		       	output = arr[0];
		       	
		       	url += "&"+output;
		       	
		       	request = new HttpGet(url);
		       	request.setConfig(requestConfig);
		       	
		       	response = client.execute(request);
		       	
		       	
		        status = response.getStatusLine().getStatusCode();
		        
		        headers = response.getHeaders("Content-Disposition");
		        str = headers[0].getValue();
		        String[] list = str.split("\"");
		        String filename = list[1];
		        
			    entity = response.getEntity();
			    
			    InputStream instream = entity.getContent();
			    File targetFile = new File("/mydata/soundbinaryclassifier_project/data/"+id+"/"+filename);
			    OutputStream outStream = new FileOutputStream(targetFile);
			    
			    int val = instream.read();
			    outStream.write(val);
			    while (val!=-1) {
			    	val = instream.read();
				    outStream.write(val);
			    }
			    
			    instream.close();
			    outStream.close();
			    
			    return filename.split("\\.")[0];
	        }
	        
	        //if the initial GET request was successful
	        else {
		        headers = response.getHeaders("Content-Disposition");
		        str = headers[0].getValue();
		        String[] list = str.split("\"");
		        String filename = list[1];
		        
	        	HttpEntity entity = response.getEntity();
			    InputStream instream = entity.getContent();
			    
			    File targetFile = new File("/mydata/soundbinaryclassifier_project/data/"+id+"/"+filename);
			    OutputStream outStream = new FileOutputStream(targetFile);
			    
			    int val = instream.read();
			    outStream.write(val);
			    while (val!=-1) {
			    	val = instream.read();
				    outStream.write(val);
			    }
			    
			    instream.close();
			    outStream.close();
			    return filename.split("\\.")[0];
	        	
	        }
	       	
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static void main(String[] args) {

	}

}
