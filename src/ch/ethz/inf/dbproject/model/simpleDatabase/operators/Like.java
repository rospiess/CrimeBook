package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.Arrays;
import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;


/**
 * Like is a special case of Select, where not equality but if compareValue is contained is checked.
 * 
 */
public class Like extends Operator {

	private final Operator op;
	private final String[] columns;
	private final String compareValue;
	private final String[] requiredMatches;

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
		requiredMatches = this.compareValue.split(" ");
	}
	
	public Like(
			final Operator op, 
			final String[] columns, 
			final String compareValue
		) {
			this.op = op;
			this.columns = columns;
			this.compareValue = compareValue;
			requiredMatches = this.compareValue.split(" ");
		}


	private final boolean accept(final Tuple tuple)
	{
		String[] tmpRequiredMatches = new String[requiredMatches.length];
		tmpRequiredMatches = requiredMatches.clone();
		for(int i = 0; i < columns.length;i++)
		{
			int columnIndex = tuple.getSchema().getIndex(columns[i]);
			if (tuple.getString(columnIndex).toLowerCase().contains(this.compareValue.toLowerCase())) {
				return true;
			}
			if (this.compareValue.contains(" ")){
				for(int j=0; j < tmpRequiredMatches.length; j++){
					if(tmpRequiredMatches[j] != null && tuple.getString(columnIndex).toLowerCase().contains(tmpRequiredMatches[j].toLowerCase())){
						tmpRequiredMatches[j] = null;
					}
				}
			}
			String[] empty = new String[tmpRequiredMatches.length];
			if(Arrays.equals(tmpRequiredMatches,empty)){
				return true;
			}
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
