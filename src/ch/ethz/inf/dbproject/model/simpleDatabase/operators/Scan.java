package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;


/**
 * The scan operator reads tuples from a file. The lines in the file contain the
 * values of a tuple. The line a comma separated.
 */
public class Scan extends Operator {

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
			//IMPORTANT: Change the following path to your respective Directory
			//Unfortunately I havent found a solution yet
			String userhome_path = System.getProperty("user.home");
			reader = new BufferedReader(new FileReader(userhome_path+"/Downloads/CrimeBook/Tables/" + fileName));
		} catch (final FileNotFoundException e) {
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
			if(input == null)
				reader.close();
			else
			{
				String[] values = input.split(",");
				current = new Tuple(schema, values);
				return true;
			}
			return false;
			
		} catch (final IOException e) {
			
			throw new RuntimeException("could not read: " + this.reader + 
				". Error is " + e);
			
		}
		
	}

}
