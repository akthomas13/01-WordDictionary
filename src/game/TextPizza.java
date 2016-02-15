package game;

import domain.WordDictionary;

public class TextPizza {

	// Main method that creates an instance of WordDictionary and runs it's init method
	public static void main(String[] args) throws Exception {
		WordDictionary w = new WordDictionary();
		w.initialize();
		//initializes the program
		System.out.println(w.getResult("ABANDONING"));
	}

}