package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.io.BufferedReader;
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
	private boolean closed = false;

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
			if (!closed){
				String input = reader.readLine();
				if(input != null)
				{
					String[] values = input.split(",");
					process_values(values);
					current = new Tuple(schema, values);
					return true;
				}			
				else{
					closed = true;
					reader.close();
				}
			}
			return false;
			
		} catch (final IOException e) {
			throw new RuntimeException("could not read: " + this.reader + 
				". Error is " + e);
			
		}
		
	}
	
	private void process_values(String[] values){
		//Converts comma tokens back to commas
		for(int i = 0; i < values.length; i++){
			if (values[i] != null)
				values[i] = values[i].replace("$COMMA$",",");
				values[i] = values[i].replace("$LINEFEED$","\r\n");
				values[i] = values[i].replace("$RETURN$","\r");
				values[i] = values[i].replace("$NEWL$","\n");
				values[i] = values[i].replace("/$","$");
		}
	}

}
