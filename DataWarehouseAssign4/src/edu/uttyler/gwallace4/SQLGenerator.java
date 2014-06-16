package edu.uttyler.gwallace4;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


/**
 * @author Glen
 *
 */
public class SQLGenerator {
	
	private Tokenizer tok;
	
	/**
	 * @param tok
	 */
	public SQLGenerator(Tokenizer tok)
	{
		this.tok = tok;
	}
	
	
	/**
	 * @param tableName
	 * @param primaryKey
	 * @param os
	 */
	public void generateCreationScript(String tableName, String[] primaryKey, OutputStream os)
	{
		PrintWriter writer = new PrintWriter(os);
		HashMap<String, VARIABLE_TYPE> typeMap = tok.getAttributeTypeMap();
		
		//Generate the "create table" source
		writer.printf("create table %s ( ", tableName);
		
		//Create the source for each of the attributes
		for(Entry<String, VARIABLE_TYPE> attribute : typeMap.entrySet())
		{
			//If there was any problems interpreting the type of data, then the system assumes varchar2.
			//Also, since this system is a data warehouse, then size isn't considered an issue, ergo I
			//just assume a limit of 100 characters and do not worry about attempting to fine tune a 
			//dynamic limit on the varchars
			String type = "varchar2(100)";
			
			//Create the string version of the type
			switch(attribute.getValue())
			{
			case INT:
				type = "int";
				break;
			case CHAR:
				type = "char";
				break;
			case FLOAT:
				type = "float";
				break;
			default:
				//Unnecessary, but oh well. It might catch some unforeseen instances.
				type = "varchar2(100)";
				break;
			}
			//Generate the attribute's source
			writer.printf("%s %s, ", attribute.getKey(), type);
		}
		
		//Create the source for the primary key
		writer.printf("primary key (%s", primaryKey[0]);
		//Generate each subsequent primary key if applicable
		for(int i = 1; i < primaryKey.length; ++i)
		{
			writer.printf(", %s", primaryKey[i]);
		}
		writer.printf("));");
		writer.close();
	}
	

}
