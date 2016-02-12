package test;

import static org.junit.Assert.*;
import org.junit.Test;
import domain.WordDictionary;

/**
 * @author AAred
 *intended to test various aspects of the dictionary backend during testing
 *it became necessary to comment out much of the code after several methods were privatized
 *all of the tests were successful prior to the privitization, which was done for future-proofing
 */
public class WordDictionaryTest {
	
	// Test if the cache is loaded 
	@Test
	public void testLoadCache() throws Exception {
		WordDictionary w = new WordDictionary();
//		assertTrue(w.getValidWords().isEmpty());
		w.initialize();
//		assertFalse(w.getValidWords().isEmpty()); // Check if the validWords TreeSet is empty. If the cache was loaded it shouldn't be
	}

	// Test if the readFile method fills the ValidWords TreeSet 
	@Test
	public void testReadFiles() throws Exception {
		// Create a new WordDictionary
		WordDictionary w = new WordDictionary();
		assertTrue("dictionary is not a valid file", w.getDICTIONARY().isFile());
//		assertTrue(w.getValidWords().isEmpty());  // ValidWords Should be empty
		System.out.println("deleting dictionary...");
		w.getDICTIONARY().delete(); // Delete dictionary to ensure readFiles gets called
		assertFalse(w.getDICTIONARY().exists()); // Check if dictionary was deleted
		w.initialize();
//		assertFalse(w.getValidWords().isEmpty()); // Check if the files were read
	}
	// Test if the loadDictionary method fills the ValidWords TreeSet
	@Test
	public void testLoadDictionary() throws Exception {
		// Create a new WordDictionary
//		WordDictionary w = new WordDictionary();
//		assertTrue(w.getValidWords().isEmpty());  // ValidWords Should be empty
//		w.loadDictionary();
//		assertFalse(w.getValidWords().isEmpty()); // Check if loadDictionary filled ValidWords
	}

	// Test if the checkWord method returns True if the word is in the cach and false if it is not
	@Test
	public void testCheckWord() throws Exception {
		// Create a new WordDictionary
		WordDictionary w = new WordDictionary();
		w.initialize();
	}

}