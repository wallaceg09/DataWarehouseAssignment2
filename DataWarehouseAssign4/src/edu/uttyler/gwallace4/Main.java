package edu.uttyler.gwallace4;

import java.io.IOException;



/**
 * @author Glen
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Tokenizer tok = new Tokenizer("hw2 data.txt");
		SQLGenerator gen = new SQLGenerator(tok);
		
		String[] primaryKeys = new String[]{"CAMPUS"};
		
		gen.generateCreationScript("Schools", primaryKeys, System.out);
		try {
			tok.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
