
package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.HashSet;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;

public final class Minus extends Operator {

	private final Operator op1, op2;
	private HashSet<String> map = new HashSet<String>();
	private TupleSchema schema1, schema2;
	private final String column;//the key of both operands

	public Minus(final Operator op1, final Operator op2, final String column) {//No Typechecking
		this.op1 = op1;
		this.op2 = op2;
		this.column = column;
	}



	@Override
	public boolean moveNext() {
		if(map.isEmpty())
			while(op2.moveNext())
			{
				Tuple t = op2.current();
				if(schema1 == null)
					schema1 = t.getSchema();
				map.add(t.getString(schema1.getIndex(column)));
				
			}
		while(op1.moveNext())
		{
			Tuple t = op1.current();
			if(schema2 == null)
				schema2 = t.getSchema();
			if(!map.contains(t.getString(schema2.getIndex(column))))
			{
				current = t;
				return true;
			}
		}
		return false;
	}
}
