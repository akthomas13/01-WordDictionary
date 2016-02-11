package game;

import domain.WordDictionary;

public class TextPizza {

	// Main method that creates an instance of WordDictionary and runs it's init method
	public static void main(String[] args) throws Exception {
		WordDictionary w = new WordDictionary();
		w.init();
		System.out.println(w.getResult("ABANDONING"));
		//initializes the program
	}

}
