package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

/**
 * Return the row with the max in column `column'
 */
public class Min extends Operator {

	private final Operator op;
	private final String column;
	
	public Min(
			final Operator op,
			final String column
		) {
			this.op = op;
			this.column = "";
		}

	@Override
	public boolean moveNext() {
		final Sort sort = new Sort(op, column, true);
		if(sort.moveNext())
		{
			final Tuple t = sort.current();
			current = t;
			return true;
		}		
		return false;
	}
}