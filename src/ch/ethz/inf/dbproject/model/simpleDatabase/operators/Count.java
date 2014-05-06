package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;

public final class Count extends Operator {

	private final Operator op;
	private final String[] columns;

	public Count(final Operator op, final String column) {
		this.op = op;
		
		this.columns = new String[] {"count",column};
	}



	public boolean moveNext() {
		int cur_count = 0;
		String cur_value = "";
		if (op.moveNext()){
			//Loop unrolling, in order to only set cur_value once
			cur_value = op.current().getString(op.current().getSchema().getIndex(columns[1]));
			cur_count = 1;
			while(op.moveNext())
			{
				cur_count += 1;
			}
		}
		
		if (cur_count == 0){
			return false;
		}
		else{
			current = new Tuple(new TupleSchema(columns), new String[] {Integer.toString(cur_count), cur_value});
			return true;
		}
	}
}
