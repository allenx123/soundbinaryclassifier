package io.kloyd.soundbinaryclassifier.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.kloyd.soundbinaryclassifier.models.Organization;
import io.kloyd.soundbinaryclassifier.models.Train;
import io.kloyd.soundbinaryclassifier.repositories.OrganizationRepository;
import io.kloyd.soundbinaryclassifier.repositories.TrainRepository;
import io.kloyd.soundbinaryclassifier.util.Util;

import org.springframework.stereotype.Controller;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@RestController
public class UIController {
	//these parameters will be taken from application.properties
	@Value("${systemDir}")
	String systemDir;	
	@Value("${isProd}")
	boolean isProd=false;
	@Value("${targetFilePath}")
	String targetFilePath;
	@Value("${predictFilePath}")
	String predictFilePath;
	
	@Autowired(required = true)
    DataSource dataSource;
	@Autowired(required = true)
	OrganizationRepository organizationRepository;
	@Autowired(required = true)
	TrainRepository trainRepository;

	
	private static final Logger logger = LoggerFactory.getLogger(UIController.class);
	private void log(String s) {
		logger.debug(s);
		//System.out.println(s);		
	}
	private void log(Exception e) {
		logger.error(e.toString(), e);
		//e.printStackTrace();;		
	}
	
	/**
	 * user calls this api to get binary prediction on .wav file they send
	 * user must specify: email, model name, and public google drive link to .wav file
	 * @param ipAddress
	 * @param input
	 * @return
	 */
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/predict", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
	public ResponseEntity<String> getPrediction(
		    @RequestHeader(value="X-Forwarded-For", required=false) String ipAddress,
			@RequestBody String input) {  
		
		System.out.println(ipAddress);                    
		try {
			
			JSONParser parser = new JSONParser();
			JSONObject jobj = (JSONObject) parser.parse(input);
			
			String shareLink = (String) jobj.get("shareLink");
			String modelName = (String) jobj.get("model");
			String email = (String) jobj.get("email");
			
			Organization org = organizationRepository.findByEmail(email);
			String id = org.getOrgID();
			
			HttpClient client = HttpClientBuilder.create().build();
			String[] temp = shareLink.split("/");
			String fileID = temp[5];
			
			String url = "https://drive.google.com/uc?export=download&id="+fileID;
			
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
			
        	HttpEntity entity = response.getEntity();
		    InputStream instream = entity.getContent();
		    
		    String filename = modelName+"_predict.wav";
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
		    
			///////call Python training file////////////////////
			String[] cmds = new String[4];
			cmds[0] = "python";
			cmds[1] = "/mydata/soundbinaryclassifier_project/python/predict.py";
			cmds[2] = id;
			cmds[3] = modelName;
			int result = Util.callOS(cmds);
			
			
			cmds = new String[2];
			cmds[0] = "rm";
			cmds[1] = "/mydata/soundbinaryclassifier_project/data/"+id+"/"+modelName+"_predict.wav";
			Util.callOS(cmds);
			
			////////////////////////////////////////////////////
			
			return new ResponseEntity<String>(""+result, HttpStatus.OK);

		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
		}
    }
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/models", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
	public ResponseEntity<String> getUserModels(
		    @RequestHeader(value="X-Forwarded-For", required=false) String ipAddress,
			@RequestBody String input) {  
		
		System.out.println(ipAddress);                    
		try {
			
			JSONParser parser = new JSONParser();
			JSONObject jobj = (JSONObject) parser.parse(input);
			
			String email = (String) jobj.get("email");
			
			Organization org = organizationRepository.findByEmail(email);
			String id = org.getOrgID();
			
			List<Train> modelList = trainRepository.findByOrgID(id);
			
			String output = "";
			for (int i=0; i<modelList.size(); i++) {
				output = output + modelList.get(i).getShareLink() + "\n";
			}
			
			return new ResponseEntity<String>(output, HttpStatus.OK);

		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
		}
    }

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/auth", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
	public ResponseEntity<String> getUserGoogleID(
		    @RequestHeader(value="X-Forwarded-For", required=false) String ipAddress,
			@RequestBody String input) {  
		
		System.out.println(ipAddress);                    
		try {
			
			JSONParser parser = new JSONParser();
			JSONObject jobj = (JSONObject) parser.parse(input);
			
			String code = (String) jobj.get("code");
			
			String accessToken = Util.getAccessToken(code);
			String userinfo = Util.getUserInfo(accessToken);
			
			if (userinfo==null) {
				return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
			}
			
			jobj = (JSONObject) parser.parse(userinfo);
			String id = (String) jobj.get("id");
			String email = (String) jobj.get("email");
			
			Organization org = new Organization();
			org.setEmail(email);
			org.setOrgID(id);
			
			organizationRepository.save(org);
			
			return new ResponseEntity<String>(userinfo, HttpStatus.OK);

		}
		catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
		}
    }
	
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/api/train", method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=utf-8")
	public ResponseEntity<String> getTrainingData(
		    @RequestHeader(value="X-Forwarded-For", required=false) String ipAddress,
			@RequestBody String input) {  
		
		System.out.println(ipAddress);                    
		try {
			
			JSONParser parser = new JSONParser();
			JSONObject jobj = (JSONObject) parser.parse(input);
			
			String shareLink = (String) jobj.get("shareLink");
			String email = (String) jobj.get("email");
			Organization org = organizationRepository.findByEmail(email);
			String orgID = org.getOrgID();
			String uuid = UUID.randomUUID().toString();
			
			
			Train train = new Train();
			
			train.setDownload(false);
			train.setOrgid(orgID);
			train.setShareLink(shareLink);
			train.setUuid(uuid);
			
			trainRepository.save(train);
			
			return new ResponseEntity<String>("", HttpStatus.OK);

		}
		catch (Exception e) {
			return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
		}
    }

}
