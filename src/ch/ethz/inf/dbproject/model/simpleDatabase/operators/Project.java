package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;

/**
 * Projection in relational algebra. Returns tuples that contain on projected
 * columns. Therefore the new tuples conform to a new schema.
 */
public final class Project extends Operator {

	private final Operator op;
	private final String[] columns;
	private final TupleSchema newSchema;

	/**
	 * Constructs a new projection operator.
	 * @param op operator to pull from
	 * @param column single column name that will be projected
	 */
	public Project(
		final Operator op, 
		final String column
	) {
		this(op, new String[] { column });
	}

	/**
	 * Constructs a new projection operator.
	 * @param op operator to pull from
	 * @param columns column names that will be projected
	 */
	public Project(
		final Operator op, 
		final String[] columns
	) {
		this.op = op;
		this.columns = columns;
		this.newSchema = new TupleSchema(columns);
	}

	@Override
	public boolean moveNext() {
		// TODO
		// get next tuple from child operator
		// create new tuple by copying the appropriate columns
		// return if we were able to advance to the next tuple
		
		return false;
	}
}
