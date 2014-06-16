package edu.uttyler.gwallace4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Glen
 *
 */
public class Tokenizer {
	
		
	//Finite State Automata for determining the type of the variables.
	private static int[][] TYPE_FSA = {
//		 N  A  .  -    \t
		{1, 2, 3, 3, 0, 4},//Starting state
		{1, 6, 5, 6, 1, 4},//Integer State
		{6, 6, 6, 6, 6, 4},//Char State
		{6, 6, 3, 3, 3, 4},//Null State
		{4, 4, 4, 4, 4, 4},//Terminal State
		{5, 6, 6, 6, 5, 4},//Float State
		{6, 6, 6, 6, 6, 4} //Varchar2 State
		/*
		 * N = Number
		 * A = Alphabet
		 * . = '.'
		 * - = '-'
		 *   = ' '
		 * /t = '\t'
		 */
	};
	
	private HashMap<String, VARIABLE_TYPE> attributeTypeMap;
	private BufferedReader reader;
	
	/**
	 * @param filename
	 */
	public Tokenizer(String filename)
	{
		attributeTypeMap = new HashMap<String, VARIABLE_TYPE>();
		try {
			reader = new BufferedReader(new FileReader(filename));
			String currentLine = reader.readLine();
			String [] attributes = currentLine.split("\t");
			
			for(String attribute:attributes)
			{
				attributeTypeMap.put(attribute, VARIABLE_TYPE.NULL);
			}
			
			//Read the next line to actually parse the variable types
			currentLine = reader.readLine();
			
			String[] values = currentLine.split("\t");
			determineTableAttributeTypes(values, attributes);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * @param values
	 * @param attributes
	 */
	public void determineTableAttributeTypes(String[] values, String[] attributes)
	{
		if(values.length == attributes.length)
		{
			for(int i = 0; i < values.length; ++i)
			{
				int state = tokenizeString(values[i]);
				setAttributeTypeFromState(state, attributes[i]);
			}
		}
	}
	
	/** DO NOT USE
	 * @param rawString
	 * @param attributes
	 * @deprecated Moved to a completely different method of tokenizing
	 */

	private void tokenizeString_(String rawString, String[] attributes)
	{
		int previousState = 0;//TODO this should be removed
		int currentState = 0;
		int currentAttribute = 0;
		int currentColumn = 0;
		
		for(int pos = 0; pos < rawString.length(); ++pos)
		{
			currentColumn = getColumn(rawString.charAt(pos));
			currentState = TYPE_FSA[currentState][currentColumn];
			
			//TODO whole if/else statement should be removed, and instead after the for loop ends return currentState
			if(currentState == 4)
			{
				//Set the type of the attribute from the previous state of the FSM and then increment the attribute counter
				//FIXME: For some reason ITEMS3 is not reaching this portion... The loop is terminating before reaching this
				setAttributeTypeFromState(previousState, attributes[currentAttribute++]);
				currentState = 0;
				previousState = 0;
			}
			else
			{
				previousState = currentState;
			}
		}
	}
	
	/**
	 * @param rawString
	 * @return
	 */
	private int tokenizeString(String rawString)
	{
		int currentState = 0;
		int currentColumn = 0;
		
		for(int pos = 0; pos < rawString.length(); ++pos)
		{
			currentColumn = getColumn(rawString.charAt(pos));
			currentState = TYPE_FSA[currentState][currentColumn];
		}
		
		return currentState;
	}
	
	/**
	 * @param state
	 * @param attributeString
	 */
	private void setAttributeTypeFromState(int state, String attributeString)
	{
		switch(state)
		{
		case 1:
			attributeTypeMap.put(attributeString, VARIABLE_TYPE.INT);
			break;
		case 2:
			attributeTypeMap.put(attributeString, VARIABLE_TYPE.CHAR);
			break;
		case 3:
			attributeTypeMap.put(attributeString, VARIABLE_TYPE.NULL);
			break;
		case 5:
			attributeTypeMap.put(attributeString, VARIABLE_TYPE.FLOAT);
			break;
		case 6:
			attributeTypeMap.put(attributeString, VARIABLE_TYPE.VARCHAR2);
			break;
			
		}
	}
	
	/**
	 * @return
	 */
	public HashMap<String, VARIABLE_TYPE> getAttributeTypeMap()
	{
		return attributeTypeMap;
	}
	
 	/**
 	 * @param input
 	 * @return
 	 */
 	private int getColumn(char input)
	{
		if(input >= 0x30 && input <= 0x39)
		{
			//If the inputted character is a number
			return 0;
		}
		else if ((input >= 0x41 && input <= 0x5A) || (input >= 0x61 && input <= 0x7A))
		{
			//If the input is a character
			return 1;
		}
		else if(input == '.')
		{
			return 2;
		}
		else if(input == '-')
		{
			return 3;
		}
		else if(input == ' ')
		{
			return 4;
		}
		else if(input == '\t') 
		{
			return 5;
		}
		return 0;
	}
	
	/**
	 * @throws IOException
	 */
	public void closeReader() throws IOException
	{
		reader.close();
	}
	
	/**
	 * 
	 */
	public void closeWriter()
	{
		
	}
	
	/**
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		closeReader();
		closeWriter();
	}
}
