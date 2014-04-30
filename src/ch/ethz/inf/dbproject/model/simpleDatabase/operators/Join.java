package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

public class Join extends Operator {

	private final Operator op;

	public Join(
		final Operator op
	) {
		this.op = op;
	}

	
	@Override
	public boolean moveNext() {

		return false;
		
	}

}
