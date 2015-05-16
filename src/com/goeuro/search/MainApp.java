package com.goeuro.search;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;

import com.goeuro.search.api.JSONSearch;
import com.goeuro.search.reports.CSVSearchFileGenerator;

/**
 * 
 * @author EG004950
 * @version 0.1
 * 
 */
/*
 * Main class to running the GoEuro Location Search
 */
public class MainApp {

	private static final Logger LOGGER = Logger.getLogger( MainApp.class.getName() );
	private static final String LOG_APP_STARTED = "GoEuroTest MainApp Started";
	private static final String LOG_APP_ENDED = "GoEuroTest MainApp Ended";
	
	public static void main(String[] args){

		LOGGER.log(Level.FINE, LOG_APP_STARTED);
		System.out.println(LOG_APP_STARTED);
		
		StringBuffer userInput = null;
		
		if(args != null && args.length > 0 && args[0] != null && !args[0].trim().equals("") ) {
			
			userInput = new StringBuffer();
			for (int i = 0; i < args.length; i++) {
				userInput.append(args[i]).append(" ");
			}
			
			LOGGER.log(Level.FINE, "User Input: "+userInput);
			System.out.println("User Input: "+userInput);
			
			try {
				/*
				 * Connect to GoEuro API, Search for user input, return result to be wrriten to CSV File
				 */
				JSONArray jsonSearchResults = JSONSearch.saerch(userInput.toString());
				
				/*
				 * Write Results to CSV File
				 */
				if(jsonSearchResults != null ) {
					CSVSearchFileGenerator.generate(jsonSearchResults, userInput.toString());
				}
			}catch (Exception e) {
				System.out.println("Exception Occured in Application, Check Logs for Details: "+e.getMessage());
			}
		}
		else {
			System.out.println("No Search Text Entered");
			LOGGER.log(Level.FINE, "No Search Text Entered");
		}
		
		LOGGER.log(Level.FINE, LOG_APP_ENDED);
		System.out.println(LOG_APP_ENDED);
	}
}
