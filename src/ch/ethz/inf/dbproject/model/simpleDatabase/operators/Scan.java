package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;


/**
 * The scan operator reads tuples from a file. The lines in the file contain the
 * values of a tuple. The line a comma separated.
 */
public class Scan extends Operator {
	
	final String absolutePath = "C:/Users/Lukas/Downloads/CrimeBook/Tables/";
	

	private final TupleSchema schema;
	private final BufferedReader reader;

	/**
	 * Contructs a new scan operator.
	 * @param fileName file to read tuples from
	 */
	public Scan(
		final String fileName, 
		final String[] columnNames
	) {
		
		// create schema
		this.schema = new TupleSchema(columnNames);

		// read from file
		BufferedReader reader = null;

		try {
			//IMPORTANT: Add Tables to your build path as a source (Right click on Tables -> Build Path -> Use as source folder
//			reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/"+fileName)));
			reader = new BufferedReader(new FileReader(absolutePath + fileName));
		} catch (final Exception e) {
			throw new RuntimeException("could not find file " + fileName);
		}
		this.reader = reader;
	}

	/**
	 * Constructs a new scan operator (mainly for testing purposes).
	 * @param reader reader to read lines from
	 * @param columns column names
	 */
	public Scan(
		final Reader reader, 
		final String[] columnNames
	) {
		this.reader = new BufferedReader(reader);
		this.schema = new TupleSchema(columnNames);
	}

	@Override
	public boolean moveNext() {
		
		try {
			String input = reader.readLine();
			if(input != null)
			{
				String[] values = input.split(",");
				current = new Tuple(schema, values);
				return true;
			}			
//			else
//				reader.close();
			return false;
			
		} catch (final IOException e) {
			
			throw new RuntimeException("could not read: " + this.reader + 
				". Error is " + e);
			
		}
		
	}

}
