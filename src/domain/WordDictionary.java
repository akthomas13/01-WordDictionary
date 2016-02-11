package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.TreeSet;
import org.apache.commons.io.FileUtils;

/**
 * The Dictionary component of the game backend
 * Able to read dictionary files and compile their contents into one cache file
 * Is then able to read the cached file and use that as a source
 * @author AAred
 */

public class WordDictionary {

	// TreeSet used because it prevents duplicates and automatically sorts words
	TreeSet<String> validWords;
	private final File TXTFOLDER = new File ("./txt"); 
	private final File DICTIONARY = new File ("./dict/dictionary.txt");
	private final String SOURCEERROR = "SOURCE FILES NOT FOUND";
	private final String DICTIONARYERROR = "FAILED TO CACHE DICTIONARY";

	public WordDictionary() throws Exception{
		validWords = new TreeSet<String>();
	}

	// Method that validates and loads the cache if a Dictionary already exist. Or creates a dictionary cache from the source files
	public void initialize(){
		if(getDICTIONARY().exists()){
			System.out.println("Cached dictionary found! Validating Cache...");
			validateCache();
			loadDictionary();
		}
		else{
			System.out.println("Cached dictionary not found. Creating now from source files...");
			readFiles();
			createDictionary();
		}
	}

	//Method used to determine if the dictionary cache is out of date. If it is out of date it will be updated
	private void validateCache() {
		boolean changed = false;
		for (File file : TXTFOLDER.listFiles()) {
			if (file.isFile() && file.getName().endsWith(".txt")) {
				long currentFileTime = file.lastModified();
				long dictionaryTime = getDICTIONARY().lastModified();
				if (currentFileTime > dictionaryTime) {
					changed=true;
				}
			}
		}
		if (changed==true){
			System.out.println("Cached dictionary out of date. Updating now from source files...");
			readFiles();
			createDictionary();
		}
		else{
			System.out.println("Cached dictionary is up to date");
		}
	}
	
	// Method that reads a Dictionary file and adds all words to a cache
	public void loadDictionary(){
		String fileContents = "";
		try {
			fileContents = FileUtils.readFileToString(getDICTIONARY());
		} catch (IOException e) {
			System.out.print(DICTIONARYERROR);
		}
		String[] allWords = fileContents.split("\\s+");
		for(String word : allWords){
			if (!word.matches(".*\\d.*")){
				validWords.add(word);
			}
		}
		System.out.println("Loaded " +validWords.size() + " valid words");
	}
	
	// Method that reads .txt files for unique words with more than one letter and no numbers. These unique words are saved to a TreeSet
	private void readFiles(){
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
				for(String word : allWords){
					totalWords++;
					if (!word.matches(".*\\d.*") && word.length()>1){
						validWords.add(word.toLowerCase());
					}
				}
			}
		}
		System.out.println("Found " +numberOfFiles+ " source files. "
				+"Detected " +validWords.size() + " valid words out of " +totalWords+ " total words");
	}
	
	// Method that writes to a .txt file all the words currently in the cache. The total number of words is printed on the first line
	private void createDictionary(){
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
	}
	// Method to check if the word passed in is currently in the cache
	private boolean checkWord(String word){
		word = word.trim();
		word = word.toLowerCase();
		return validWords.contains(word);
	}//checks if a specified word exists within a dictionary file, throws an exception if it is not

	// Method that returns a string with the answer to the CheckWord method above
	public String getResult(String word){
		if(checkWord(word)==true){
			return (word+ " is a valid word! :D");
		}
		else{
			return (word+ " is invalid D:");
		}
	}

	// DICTIONARY getter method
	public File getDICTIONARY() {
		return DICTIONARY;
	}
}
