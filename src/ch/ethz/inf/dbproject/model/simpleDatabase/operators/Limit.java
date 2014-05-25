package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

/**
 * The LIMIT clause can be used to constrain the number of rows returned by the SELECT statement.
 * LIMIT takes one or two numeric arguments, which must both be nonnegative integer.
 * With two arguments, the first argument specifies the offset of the first row to return,
 * and the second specifies the maximum number of rows to return.
 * The offset of the initial row is 0 (not 1).  (Implementing mySQL specs)
 */
public class Limit extends Operator {

	private final Operator op;
	private final int offset;
	private final int max;
	private int offsetCounter, maxCounter;
	
	public Limit(
		final Operator op,
		final int offset,
		final int max
	) {
		this.op = op;
		this.offset = offset;
		this.max = max;
	}
	
	public Limit(
			final Operator op,
			final int max
		) {
			this.op = op;
			this.offset = 0;
			this.max = max;
		}

	@Override
	public boolean moveNext() {
		while(offset - offsetCounter > 0 && op.moveNext())
			offsetCounter++;
		while(op.moveNext() && max - offset - maxCounter > 0)
		{
			final Tuple t = this.op.current();
			current = t;
			maxCounter++;
			return true;
		}		
		return false;
	}

	
}
