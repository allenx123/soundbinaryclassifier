package io.kloyd.soundbinaryclassifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ConfigurableApplicationContext;

import io.kloyd.soundbinaryclassifier.controllers.UIController;
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

	private static final Logger logger = LoggerFactory.getLogger(App.class);
	
	@Autowired(required = true)
	private UIController uiController;
	
	@Autowired
    private ConfigurableApplicationContext context;
	
	@Autowired(required = true)
    DataSource dataSource;

	public static void main(String[] args) {
 		SpringApplication application = new SpringApplication(App.class);
        application.run(args);
	}

	@Override
    public void run(String... args) throws Exception {
		while(true)
			Thread.sleep(10000);
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
