package com.goeuro.search.reports;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.goeuro.search.config.PropertyLoader;

public class CSVSearchFileGenerator {

	private static final Logger LOGGER = Logger.getLogger( CSVSearchFileGenerator.class.getName() );
	
	private static final String GOEURO_CSV_FILE_NAME_PREFIX = "csvFileNamePrefix";
	
	private static final String GOEURO_JSON_LOCATION_ID_KEY = "_id";
	private static final String GOEURO_JSON_LOCATION_NAME_KEY = "name";
	private static final String GOEURO_JSON_LOCATION_TYPE_KEY = "type";
	private static final String GOEURO_JSON_LOCATION_GEOPOSITION_KEY = "geo_position";
	private static final String GOEURO_JSON_LOCATION_LATITUDE_KEY = "latitude";
	private static final String GOEURO_JSON_LOCATION_LONGITUDE_KEY = "longitude";
	private static final String CSV_FILES_DIR_NAME = "GoEuro_CSV_SearchResult_Files";
	
	// Delimiter used in CSV file
	private static final String NEW_LINE_SEPARATOR = "\n";

	// CSV file header
	private static final Object[] FILE_HEADER = { "ID", "Name", "Type","Latitude", "Longitude" };
		
	public static void generate(JSONArray jsonSearchResults, String userInput) throws IOException {
		
		/*
		 * 4- Write Result to CSV File
		 */
		
		// Create a Directory, if failed, write to the current folder
		
		if( new File(CSV_FILES_DIR_NAME).isDirectory() ) {
			System.out.println("CSV Directory found");
			LOGGER.log(Level.FINEST, "CSV Directory found");
		}
		else {
			boolean success = (new File(CSV_FILES_DIR_NAME)).mkdirs();
			if (!success) {
				System.out.println("Directory Creation Failed OR Directory Already Exists, Files to be written in currenct directory");
				LOGGER.log(Level.FINEST, "Directory Creation Failed OR Directory Already Exists, Files to be written in currenct directory");
			}
		}
		
		FileWriter fileWriter = null;
		CSVPrinter csvFilePrinter = null;
		
		if(jsonSearchResults != null && jsonSearchResults.size() > 0 ) {
			
			// Create the CSVFormat object with "\n" as a record delimiter
			CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
	
			try {
				// Initialise FileWriter object
				StringBuffer csvFilePrefix = new StringBuffer(PropertyLoader.get(GOEURO_CSV_FILE_NAME_PREFIX));
				String csvFileName = csvFilePrefix.append(userInput).append(".CSV").toString();
				fileWriter = new FileWriter( CSV_FILES_DIR_NAME+"\\"+csvFileName );
	
				// Initialise CSVPrinter object
				csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
	
				// Create CSV file header
				csvFilePrinter.printRecord(FILE_HEADER);
	
				// Write a new location object list to the CSV file
	
				java.util.List<Object> goEuroLocationRecord = null;
				for (int i = 0; i < jsonSearchResults.size(); i++) {
	
					goEuroLocationRecord = new ArrayList<>();
					LOGGER.log(Level.FINEST, "Looping on item ("+i+") of JSON result elements");
					System.out.println("Looping on item ("+i+") of JSON result elements");
					
					LOGGER.log(Level.FINE, "Response ="+jsonSearchResults.get(i));
					
					JSONObject object = (JSONObject) jsonSearchResults.get(i);
	
					String id = object.get(GOEURO_JSON_LOCATION_ID_KEY).toString();
					String name = object.get(GOEURO_JSON_LOCATION_NAME_KEY).toString();
					String type = object.get(GOEURO_JSON_LOCATION_TYPE_KEY).toString();
					
					JSONObject geoPositionObject = (JSONObject) object.get(GOEURO_JSON_LOCATION_GEOPOSITION_KEY);
					String latitude = null;
					String longitude = null;
					
					Object latitudeObj = null;
					Object longitudeObj = null;
					
					if(geoPositionObject != null) {
					
						if( (latitudeObj = geoPositionObject.get(GOEURO_JSON_LOCATION_LATITUDE_KEY) ) !=null ) {
							latitude =latitudeObj.toString();
						}
						
						if( (longitudeObj = geoPositionObject.get(GOEURO_JSON_LOCATION_LONGITUDE_KEY) ) !=null ) {
							longitude = longitudeObj.toString();
						}
					}
					
					LOGGER.log(Level.FINEST, "Value of ID = "+id);
					LOGGER.log(Level.FINEST, "Value of Name = "+name);
					LOGGER.log(Level.FINEST, "Value of Type = "+type);
					LOGGER.log(Level.FINEST, "Value of Latitude = "+latitude);
					LOGGER.log(Level.FINEST, "Value of Longtuide = "+longitude);
					
					System.out.println("ID -- " + object.get("_id"));
					System.out.println("Name -- " + object.get("name"));
					System.out.println("Type -- " + object.get("type"));
					System.out.println("Latitude -- " + geoPositionObject.get("latitude"));
					System.out.println("Longitude -- " + geoPositionObject.get("longitude"));
	
					
	
					// Prepare List of objects to be written to the CVS File
					goEuroLocationRecord.add(id);
					goEuroLocationRecord.add(name);
					goEuroLocationRecord.add(type);
					goEuroLocationRecord.add(latitude);
					goEuroLocationRecord.add(longitude);
					csvFilePrinter.printRecord(goEuroLocationRecord);
				}
	
				LOGGER.log(Level.FINER,"CSV File created and records have been written, file name: "+csvFileName);
				System.out.println("CSV file was created successfully with name: "+csvFileName);
	
			
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Failed writting to CSV File");
				LOGGER.log(Level.SEVERE, e.toString(), e);
				System.out.println("Failed writting to CSV File");
				//e.printStackTrace();
				
				throw new IOException(e);
			} finally {
				try {
					fileWriter.flush();
					fileWriter.close();
					csvFilePrinter.close();
				} catch (IOException e) {
					System.out.println("Error while flushing/closing fileWriter/csvPrinter");
					LOGGER.log(Level.SEVERE, "Error while flushing/closing fileWriter/csvPrinter");
					LOGGER.log(Level.SEVERE, e.toString(), e);
					//e.printStackTrace();
					throw new IOException(e);
				}
			}
		}
		else {
			LOGGER.log(Level.FINE, "No Results Found");
			System.out.println("No Results Found");
		}
	}
}
