package game;

import domain.WordDictionary;

public class TextPizza {

	// Main method that creates an instance of WordDictionary and runs it's init method
	public static void main(String[] args) throws Exception {
		WordDictionary w = new WordDictionary();
		w.initalize();
		System.out.println(w.getResult("ABANDONING"));
		//initializes the program
	}

}
