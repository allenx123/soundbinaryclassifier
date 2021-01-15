package io.kloyd.soundbinaryclassifierdownloader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ConfigurableApplicationContext;

import io.kloyd.soundbinaryclassifierdownloader.models.Train;
import io.kloyd.soundbinaryclassifierdownloader.repositories.OrganizationRepository;
import io.kloyd.soundbinaryclassifierdownloader.repositories.TrainRepository;
import io.kloyd.soundbinaryclassifierdownloader.util.Util;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
//{ 
//un-comment if you want to run this application as command line application
//you can both keep the command line and have it listen on port 8090
//as completing your own logic in the run function does not exit the application
implements CommandLineRunner {
	
	@Autowired(required = true)
    DataSource dataSource;
	@Autowired(required = true)
	OrganizationRepository organizationRepository;
	@Autowired(required = true)
	TrainRepository trainRepository;
	
	@Value("${cmd1}")
	String cmd1;
	@Value("${cmd2}")
	String cmd2;
	@Value("${cmd3}")
	String cmd3;

	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
	@Autowired
    private ConfigurableApplicationContext context;

	public static void main(String[] args) {
 		SpringApplication application = new SpringApplication(App.class);
        application.run(args);
	}

	@Override
    public void run(String... args) throws Exception {
		while(true) {
			System.out.println(trainRepository.findByDownload().size());
			if (trainRepository.findByDownload().size()>0) { //check if there are any zips that need to be downloaded
				Train train = trainRepository.findByDownload().get(0); //gets first available zip with status download = false
				String shareLink = train.getShareLink();
				String id = train.getOrgid();
				String filename = Util.downloadData(shareLink, id); //download zip
				trainRepository.delete(train);
				train.setDownload(true);
				trainRepository.save(train); //set zip status to download = true
				System.out.println(trainRepository.findByDownload().size());
				
				
				//unzip -d /mydata/soundbinaryclassifier_project/data/109242947273433554126 cats_dogs.zip
				String[] cmds = new String[4]; 
				cmds[0] = "unzip";
				cmds[1] = "-d";
				cmds[2] = "/mydata/soundbinaryclassifier_project/data/"+id;
				cmds[3] = "/mydata/soundbinaryclassifier_project/data/"+id+"/"+filename+".zip";
				
				Util.callOS(cmds);
				/*
				///////////only for aws///////////////
				cmds = new String[3]; 
				cmds[0] = cmd1;
				cmds[1] = cmd2;
				cmds[2] = cmd3;
				Util.callOS(cmds);
				/////////////////////////////////////
				
				*/
				
				
				///////call Python training file////////////////////
				
				cmds = new String[4];
				cmds[0] = "python";
				//cmds[1] = "C:\\Users\\Allen Xu\\eclipse-workspace\\soundbinaryclassifierdownloader\\src\\main\\resources\\test.py";
				cmds[1] = "/mydata/soundbinaryclassifier_project/python/train.py";
				cmds[2] = id;
				cmds[3] = filename;
				Util.callOS(cmds);
				////////////////////////////////////////////////////
				
				cmds = new String[2];
				cmds[0] = "rm";
				cmds[1] = "/mydata/soundbinaryclassifier_project/data/"+id+"/"+filename+".zip";
				Util.callOS(cmds);
				
				cmds = new String[3];
				cmds[0] = "rm";
				cmds[1] = "-rf";
				cmds[2] = "/mydata/soundbinaryclassifier_project/data/"+id+"/"+filename;
				Util.callOS(cmds);
				
				
			}
			
			Thread.sleep(10000);
		}
			
    }	

	private void log(String s) {
		logger.debug(s);
		//System.out.println(s);		
	}
	private void log(Exception e) {
		logger.error(e.toString(), e);
		//e.printStackTrace();;		
	}
	
}
