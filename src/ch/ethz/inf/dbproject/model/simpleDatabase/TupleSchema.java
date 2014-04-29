package ch.ethz.inf.dbproject.model.simpleDatabase;

import java.util.HashMap;

/**
 * The schema contains meta data about a tuple. So far we only store the name of
 * each column. Other meta data, such cardinalities, indexes, etc. could be
 * specified here.
 */
public class TupleSchema {

	private final String[] columnNames;
	private final HashMap<String, Integer> columnNamesMap;
	
	/**
	 * Constructs a new tuple schema.
	 * @param columnNames column names
	 */
	public TupleSchema(
		final String[] columnNames
	) {
		this.columnNames = columnNames;
		
		this.columnNamesMap = new HashMap<String, Integer>();
		for (int i = 0; i < columnNames.length; ++i) {
			this.columnNamesMap.put(this.columnNames[i].toUpperCase(), i);
		}
	}

	/**
	 * Given the name of a column, returns the index in the respective tuple.
	 * 
	 * @param column column name
	 * @return index of column in tuple
	 */
	public int getIndex(final String column) {

		final Integer index = this.columnNamesMap.get(column.toUpperCase());
		if (index == null) {
			return -1; // error
		} else {
			return index;
		}
		
	}
	
}
