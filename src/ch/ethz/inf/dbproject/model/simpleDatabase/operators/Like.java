package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.Comparator;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;


/**
 * Like is a special case of Select, where not equality but if compareValue is contained is checked.
 * 
 */
public class Like<T> extends Operator {

	private final Operator op;
	private final String column;
	private final T compareValue;

	/**
	 * Contructs a new selection operator.
	 * @param op operator to pull from
	 * @param column column name that gets compared
	 * @param compareValue value that must be matched
	 */
	public Like(
		final Operator op, 
		final String column, 
		final T compareValue
	) {
		this.op = op;
		this.column = column;
		this.compareValue = compareValue;
	}


	private final boolean accept(final Tuple tuple)
	{

		final int columnIndex = tuple.getSchema().getIndex(this.column);

		if ( tuple.getString(columnIndex).contains(this.compareValue.toString()))
			return true;
		else 
			return false;
		
	}
	
	@Override
	public boolean moveNext() {
		
		while(this.op.moveNext())
		{
			final Tuple t = this.op.current();
			if (this.accept(t)) {
				this.current = t;
				return true;
			}
		}
		return false;
		
	}

}
