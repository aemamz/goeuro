package com.goeuro.search.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.goeuro.search.config.PropertyLoader;

public class JSONSearch {

	private static final Logger LOGGER = Logger.getLogger( JSONSearch.class.getName() );
	private static final String GOEURO_POSITION_API_URI = "positionAPIURI";
	
	public static JSONArray saerch(String userInput) throws IOException, ParseException, URISyntaxException {
		/*
		 * 1- Load Go Euro Position API URI
		 */
	    HttpURLConnection request = null;
	    StringBuffer sURL = null;
	    JSONArray jsonSearchResults = null;
	    
	    System.out.println("User Input : "+userInput);
	   
	    if(userInput != null && !userInput.trim().equals("")) {
	    	
	    	sURL = new StringBuffer(PropertyLoader.get(GOEURO_POSITION_API_URI));
	    	sURL.append(userInput);
	    	
			
		  //  LOGGER.log(Level.FINE, "GoEuro Location API ="+uri.toURL());
			//System.out.println("GoEuro Location API ="+uri.toURL());
			
			/*
			 * 2- Connect to the URL using java's native library
			 */
	    	
	    	URL url= new URL(sURL.toString());
	    	URI uri = null;
			try {
				uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
			} catch (URISyntaxException e2) {
				System.out.println("Invalid URI");
				LOGGER.log(Level.SEVERE, "Invalid URI", e2);
				e2.printStackTrace();
				throw new URISyntaxException("Invalid URI", e2.getMessage());
			}
	    			    
			System.out.println("GoEuro API URI: "+uri.toASCIIString());
			LOGGER.log(Level.FINE, "GoEuro API URI:"+uri.toASCIIString());
			
			try {
				url = new URL(sURL.toString());
				request = (HttpURLConnection) uri.toURL().openConnection();
				request.connect();
			} catch (IOException e1) {
				LOGGER.log( Level.SEVERE, "Failed to connect to GoEuro Location API URI, Check Configuration File, Connectivity or Check API server", e1 );
				LOGGER.log( Level.SEVERE, e1.toString(), e1 );
				System.out.println("Failed to connect to GoEuro Location API URI, Check Configuration File, Connectivity or Check API server");
				//e1.printStackTrace();
				throw new IOException(e1);
			}
			
			/*
			 * 3- Parse JSON results returned from the Request
			 */
			JSONParser parser = new JSONParser();
			Object obj = null;
			InputStream inputStream = null;
			try {
				inputStream = (InputStream) request.getContent();
				obj = parser.parse(new InputStreamReader(inputStream));
				jsonSearchResults =(JSONArray)obj;
			} catch (IOException  e1) {
				LOGGER.log( Level.SEVERE, "Failed to read result from GoEuro API", e1 );
				LOGGER.log( Level.SEVERE, e1.toString(), e1 );
				
				System.out.println("Failed to read result from GoEuro API");
				throw new IOException(e1);
				//e1.printStackTrace();
			} catch (ParseException e) {
				
				LOGGER.log( Level.SEVERE, "Failed to parse JSON Result", e );
				LOGGER.log( Level.SEVERE, e.toString(), e );
				
				System.out.println("Failed to parse JSON Result");
				throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
			}
	    }
	    else {
	    	System.out.println("No Results Found");
	    	LOGGER.log( Level.SEVERE, "No Results Found");
	    }
		return  jsonSearchResults;
	}
}
