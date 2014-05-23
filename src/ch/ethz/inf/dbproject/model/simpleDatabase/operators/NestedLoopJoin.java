package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.ArrayList;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;

public class NestedLoopJoin extends Operator {

	private final Operator op1, op2;
	private final String column;//We join on this column
	private TupleSchema schema1, schema2;
	private final String[] columns;//The columns of the newly joined Relation
	private final ArrayList<Tuple> innerLoopBuffer;
	private int offset;

	public NestedLoopJoin(//The Order is very important! If the Relation is 1:n, the 1 Relation has to be first. e.g. Address & Case
			//The Case determines the Address, therefore Address has to be the first argument
		final Operator OneRelation,	final Operator nRelation, final String column, final String[] columns
	) {
		this.op1 = OneRelation;
		this.op2 = nRelation;
		this.column = column;
		this.columns = columns;
		this.innerLoopBuffer = new ArrayList<Tuple>();
	}
	
	@Override
	public boolean moveNext() {
			while(op1.moveNext())//Go through the Small Relation
			{
				Tuple t1 = op1.current();
				if(schema1 == null)
					schema1 = t1.getSchema();
				
				if(innerLoopBuffer.isEmpty()){
					while(op2.moveNext())
						innerLoopBuffer.add(op2.current());}
				offset = 0;
				
				while(innerLoopBuffer.size()>0 && offset<innerLoopBuffer.size())//Go through the Large Relation and take matching tuple
				{
					Tuple t2 = innerLoopBuffer.get(offset);
					offset++;
					if(schema2 == null)
						schema2 = t2.getSchema();
					if(t2.getString(schema2.getIndex(column)).equals(t1.getString(schema1.getIndex(column))))//Build new Tuple from the 2 old ones
					{
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
			}
		return false;		
	}
}
