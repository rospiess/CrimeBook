package ch.ethz.inf.dbproject.model.simpleDatabase;

import java.sql.Time;

/**
 * A tuple in our database. A tuple consists of a schema (describing the names
 * of the columns) and values. A tuple is created and modified by operators.
 * 
 * You can use String to store the values. In case you need a specific type,
 * you can use the additional getType methods.
 */
public class Tuple {

	private final TupleSchema schema;
	private final String[] values;

	public Tuple(
		final TupleSchema schema, 
		final String[] values
	) {
		this.schema = schema;
		this.values = values;
	}

	public final TupleSchema getSchema() {
		return this.schema;
	}

	public final String getString(final int index) {
		return this.values[index];
	}

	public final short getShort(final int index) {
		return Short.parseShort(this.values[index]);
	}
	
	public final int getInt(final int index) {
		return Integer.parseInt(this.values[index]);
	}
	
	public final float getFloat(final int index) {
		return Float.parseFloat(this.values[index]);
	}
	
	public final double getDouble(final int index) {
		return Double.parseDouble(this.values[index]);
	}
	
	public final String getDate(final int index) {
		return this.values[index];
//		Some unsuccessful tries to fix this goddamn Date
		/*
		try {
			return (new SimpleDateFormat("yyyy-MM-dd").parse(this.values[index]));
		} catch (ParseException e) {
			e.printStackTrace();
		}return null;
		
		String[] s = this.values[index].split("-");
		return new Date(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2]));
		*/

	}
	
	public final Time getTime(final int index) {
		if(this.values[index]==null)
			return null;
		String[] s = this.values[index].split(":");
		return new Time(Integer.parseInt(s[0]),Integer.parseInt(s[1]),Integer.parseInt(s[2]));

	}
	
	public final boolean getBoolean(final int index) {
		return Boolean.parseBoolean(this.values[index]);
	}
	
	public final String toString() {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			buf.append(values[i]);
			if (i < values.length - 1) {
				buf.append(",");
			}
		}
		return buf.toString();
	}

}
