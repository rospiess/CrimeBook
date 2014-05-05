package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

public final class Count {

	private final Operator op;

	public Count(final Operator op) {
		this.op = op;
	}



	public int nextValue() {
		int cur_count = 0;

		while(op.moveNext())
		{
			cur_count += 1;
		}
		
		return cur_count;
	}
}
