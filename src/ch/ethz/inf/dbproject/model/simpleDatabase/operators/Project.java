package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
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
	 * 
	 * @param op
	 *            operator to pull from
	 * @param column
	 *            single column name that will be projected
	 */
	public Project(final Operator op, final String column) {
		this(op, new String[] { column });
	}

	/**
	 * Constructs a new projection operator.
	 * 
	 * @param op
	 *            operator to pull from
	 * @param columns
	 *            column names that will be projected
	 */
	public Project(final Operator op, final String[] columns) {
		this.op = op;
		this.columns = columns;
		this.newSchema = new TupleSchema(columns);
	}

	@Override
	public boolean moveNext() {

		if (this.op.moveNext()) {
			final Tuple t = this.op.current();
			String[] values = new String[columns.length];
			for (int i = 0; i < columns.length; i++)
				values[i] = t.getString(newSchema.getIndex(columns[i]));
			final Tuple t2 = new Tuple(newSchema, values);
			this.current = t2;
			return true;
		}
		return false;
	}
}
