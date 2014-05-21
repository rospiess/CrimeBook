package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.HashMap;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;

public class Join extends Operator {

	private final Operator op1, op2;
	private final String column;//We join on this column
	private HashMap<String,Tuple> map = new HashMap<String, Tuple>();
	private TupleSchema schema1, schema2;
	private final String[] columns;//The columns of the newly joined Relation

	public Join(//The Order is very important! If the Relation is 1:n, the 1 Relation has to be first. e.g. Address & Case
			//The Case determines the Address, therefore Address has to be the first argument
		final Operator OneRelation,	final Operator nRelation, final String column, final String[] columns
	) {
		this.op1 = OneRelation;
		this.op2 = nRelation;
		this.column = column;
		this.columns = columns;
	}

	
	@Override
	public boolean moveNext() {
		if(map.isEmpty())
			while(op1.moveNext())//Hash the Small Relation
			{
				Tuple t = op1.current();
				if(schema1 == null)
					schema1 = t.getSchema();
				map.put(t.getString(schema1.getIndex(column)), t);
				
			}		
		while(op2.moveNext())//Go through the Large Relation and take matching tuple
		{
			Tuple t2 = op2.current();
			if(schema2 == null)
				schema2 = t2.getSchema();
			String key = t2.getString(schema2.getIndex(column));
			if(map.containsKey(key))//Build new Tuple from the 2 old ones
			{
				Tuple t1 = map.get(key);
				String[] values = new String[columns.length];
				for(int i= 0; i< columns.length; i++)
				{
					//Data is in tuple1
					if(schema1.getIndex(columns[i])>= 0)
						values[i] = t1.getString(schema1.getIndex(columns[i]));
					
					//Data is in tuple 2
					else
						values[i] = t2.getString(schema2.getIndex(columns[i]));					
				}
				current = new Tuple(new TupleSchema(columns), values);
				return true;
			}
		}
		return false;		
	}
}
