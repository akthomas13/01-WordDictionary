package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.TreeSet;
import org.apache.commons.io.FileUtils;

/**
 * The Dictionary component of the game backend
 * Able to read dictionary files and compile their contents into one cache file
 * Is then able to read the cached file and use that as a source
 * v1. initial version, fully functional
 * v2. implemented time calculation for each dictionary access method
 * v3. refactored some methods, cleaned up console output
 * @author Aaron Thomas
 */
public class WordDictionary {

	TreeSet<String> validWords;	//TreeSet prevents duplicates and automatically sorts words
	private final File TXTFOLDER, DICTIONARY;
	private final String SOURCEERROR, DICTIONARYERROR;

	public WordDictionary() throws Exception{
		validWords = new TreeSet<String>();
		
		TXTFOLDER = new File ("./txt");
		DICTIONARY = new File ("./dict/dictionary.txt");
		
		SOURCEERROR = "SOURCE FILES NOT FOUND";
		DICTIONARYERROR = "FAILED TO CACHE DICTIONARY";
	}

	/**
	 * Method that validates and loads the cache if a Dictionary already exist.
	 * Or creates a dictionary cache from the source files
	 */
	public void initialize(){
		if(getDICTIONARY().exists()){
			System.out.println("Cached dictionary found! Validating Cache...\n");
			validateCache();
			loadDictionary();
		}
		else{
			System.out.println("Cached dictionary not found. Creating now from source files...\n");
			readFiles();
			createDictionary();
		}
	}

	/**
	 * Method used to determine if the dictionary cache is out of date.
	 * If it is out of date it will be updated
	 */
	private void validateCache() {
		long start = System.nanoTime();
		if (checkDeprecation()){
			System.out.println("Cached dictionary out of date. Updating now from source files...");
			readFiles();
			createDictionary();
		}
		else{
			System.out.println("Cached dictionary is up to date");
		}
		long end = System.nanoTime();
		System.out.println("Validation took " +time(start,end) + " seconds.\n");
	}
	
	/**
	 * internal method to check timestamps, stops if any file is changed
	 * @return boolean of whether cache is out of date
	 */
	private boolean checkDeprecation(){
		for (File file : TXTFOLDER.listFiles()) {
			if (file.isFile() && file.getName().endsWith(".txt")) {
				long currentFileTime = file.lastModified();
				long dictionaryTime = getDICTIONARY().lastModified();
				if (currentFileTime > dictionaryTime) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method that reads a Dictionary file and adds all words to a cache
	 */
	private void loadDictionary(){
		long start = System.nanoTime();
		String fileContents = "";
		try {
			fileContents = FileUtils.readFileToString(getDICTIONARY());
		} catch (IOException e) {
			System.out.print(DICTIONARYERROR);
		}
		String[] allWords = fileContents.split("\\s+");
		//delimits by any whitespace
		for(String word : allWords){
			if (!word.matches(".*\\d.*")){
				//only adds words that do not contain a number
				//used when loading the dictionary to avoid loading the number of entries
				validWords.add(word);
			}
		}
		System.out.println("Dictionary contains " +validWords.size() + " valid words");
		long end = System.nanoTime();
		System.out.println("Loading took " +time(start,end) + " seconds.\n");
	}
	
	/**
	 * Method that reads .txt files for unique words with more than one letter and no numbers.
	 * These unique words are saved to a TreeSet
	 */
	private void readFiles(){
		long start = System.nanoTime();
		String fileContents = "";
		int numberOfFiles = 0;
		int totalWords = 0;
		for(File file : TXTFOLDER.listFiles()) {
			numberOfFiles++;
			if (file.isFile() && file.getName().endsWith(".txt")) {
				try {
					fileContents = FileUtils.readFileToString(file);
				} catch (IOException e) {
					System.out.println(SOURCEERROR);
				}
				String[] allWords = fileContents.split("\\s+");
				//delimits by any whitespace
				for(String word : allWords){
					totalWords++;
					if (!word.matches(".*\\d.*") && word.length()>1){
						//only adds words that do not contain a number and length>1
						validWords.add(word.toLowerCase());
					}
				}
			}
		}
		System.out.println("Found " +numberOfFiles+ " source files. "
				+"Detected " +validWords.size() + " valid words out of " +totalWords+ " total words");
		long end = System.nanoTime();
		System.out.println("Reading files took " +time(start,end) + " seconds.\n");
	}
	 
	/**
	 * Method that writes to a .txt file all the words currently in the cache.
	 * The total number of words is printed on the first line
	 */
	private void createDictionary(){
		long start = System.nanoTime();
		System.out.println("Caching dictionary...");
		PrintStream printer;
		try {
			printer = new PrintStream(new FileOutputStream(getDICTIONARY()));
			printer.print(validWords.size());
			for (String newWord : validWords){
				printer.print("\n"+ newWord);
			}
			printer.close();
		} catch (FileNotFoundException e) {
			System.out.println(DICTIONARYERROR);
		}
		System.out.println("Dictionary cached!");
		long end = System.nanoTime();
		System.out.println("Creating dictionary took " +time(start,end) + " seconds.\n");
	}
	
	/**
	 * Method to check if the word passed in is currently in the cache
	 * exists separately from getResult in case that future game requires booleans
	 * @param word word that needs to be checked
	 * @return whether the word was found
	 */
	private boolean checkWord(String word){
		word = word.trim();
		word = word.toLowerCase();
		return validWords.contains(word);
	}

	/**
	 * Method that returns a string with the answer to the CheckWord method above
	 * @param word that needs to be checked
	 * @return String result of whether the word was found
	 */
	public String getResult(String word){
		long start = System.nanoTime();
		String result = "";
		System.out.println("Checking validity of " +word+ "...");
		if(checkWord(word)==true){
			result = (word+ " is a valid word! :D");
		}
		else{
			result = (word+ " is invalid D:");
		}
		long end = System.nanoTime();
		System.out.println("Checking word took " +time(start,end) + " seconds.\n");
		return result;
	}

	/**
	 * internal method used to format the display format of the time
	 * @param start time of a method
	 * @param end time of a method
	 * @return formatted double
	 */
	private double time(long start, long end){
		DecimalFormat df = new DecimalFormat("#.00000");
		return Double.parseDouble(df.format((double)(end-start) / 1000000000.0));
	}
	
	/**
	 * DICTIONARY getter method, currently only used for JUnit Testing
	 * @return cached dictionary
	 */
	public File getDICTIONARY() {
		return DICTIONARY;
	}
}