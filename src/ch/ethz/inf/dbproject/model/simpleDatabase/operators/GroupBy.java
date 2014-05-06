package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

/**
 * An empty sort operator
 * For the purposes of the project, you can consider only string comparisons of
 * the values.
 */
public class GroupBy extends Operator implements Comparator<Tuple> {

	private final Operator op;
	private final String column;
	private String prev_value;
	private final ArrayList<Tuple> sortBuffer;
	private int offset;
	private int columnid;
	
	public GroupBy(
		final Operator op,
		final String column
	) {
		this.op = op;
		this.column = column;
		this.sortBuffer = new ArrayList<Tuple>();
	}
	
	@Override
	public final int compare(
		final Tuple l, 
		final Tuple r
	) {
		
		final int columnIndex = l.getSchema().getIndex(this.column);
		if(l.getString(columnIndex)==null)
			return 1;
		if(r.getString(columnIndex)==null)
			return -1;
		final int result = 
			l.getString(columnIndex).compareToIgnoreCase(r.getString(columnIndex));
		
		return result;
	}

	@Override
	public boolean moveNext() {
		
		if(sortBuffer.isEmpty()){
			while(op.moveNext())
				sortBuffer.add(op.current());
			Collections.sort(this.sortBuffer, this);
			
			if (sortBuffer.size()>0){
				current = sortBuffer.get(0);
				offset = 1;
				columnid = current.getSchema().getIndex(column);
				prev_value = current.getString(columnid);
				return true;
			}
			else
				return false;
		}
		if(offset<sortBuffer.size())
		{
			current = sortBuffer.get(offset);
			String cur_value = current.getString(columnid);
			if (cur_value.equals(prev_value)){
				offset++;
				return true;
			}
			else
			{
				prev_value = cur_value;
				return false;
			}
		}		
		return false;
	}

	
}
