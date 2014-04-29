package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.Comparator;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;


/**
 * Selection in relational algebra. Only returns those tuple for which the given
 * column matches the value.
 * 
 * This class is a generic to allow comparing any types of values.
 * 
 * i.e. SELECT * FROM USERS WHERE USER_ID=1
 * would require these operators: 
 * 
 * Scan usersScanOperator = new Scan(filename, columnNames);
 * Select<Integer> selectOp = new Select<Integer>(
 * 		usersScanOperator, "user_id", 1);
 * 
 * Similarly, this query:
 * 
 * SELECT * FROM USERS WHERE USENAME=john
 * would require these operators:
 * 
 * Scan usersScanOperator = new Scan(filename, columnNames);
 * Select<String> selectOp = new Select<String>(
 * 		usersScanOperator, "username", "john");
 * 
 */
public class Select<T> extends Operator {

	private final Operator op;
	private final String column;
	private final T compareValue;

	/**
	 * Contructs a new selection operator.
	 * @param op operator to pull from
	 * @param column column name that gets compared
	 * @param compareValue value that must be matched
	 */
	public Select(
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
		
		if (tuple.getString(columnIndex).equals(this.compareValue.toString())) {
			return true;
		} else {
			return false;
		}
		
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
