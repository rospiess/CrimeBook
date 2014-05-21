package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;


/**
 * Like is a special case of Select, where not equality but if compareValue is contained is checked.
 * 
 */
public class Like extends Operator {

	private final Operator op;
	private final String[] columns;
	private final String compareValue;

	/**
	 * Contructs a new selection operator.
	 * @param op operator to pull from
	 * @param column column name that gets compared
	 * @param compareValue value that must be matched
	 */
	public Like(
		final Operator op, 
		final String column, 
		final String compareValue
	) {
		this.op = op;
		this.columns = new String[]{column};
		this.compareValue = compareValue;
	}
	
	public Like(
			final Operator op, 
			final String[] columns, 
			final String compareValue
		) {
			this.op = op;
			this.columns = columns;
			this.compareValue = compareValue;
		}


	private final boolean accept(final Tuple tuple)
	{
		for(int i = 0; i < columns.length;i++)
		{
			int columnIndex = tuple.getSchema().getIndex(columns[i]);
			if ( tuple.getString(columnIndex).contains(this.compareValue))
				return true;
		}
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
