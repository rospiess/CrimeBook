package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

public final class Union extends Operator {

	private final Operator op1, op2;

	public Union(final Operator op1, final Operator op2) {//No Typechecking
		this.op1 = op1;
		this.op2 = op2;
	}



	@Override
	public boolean moveNext() {
		if(op1.moveNext())
		{
			current = op1.current();
			return true;
		}
		if(op2.moveNext())
		{
			current = op2.current();
			return true;
		}
		return false;
	}
}
