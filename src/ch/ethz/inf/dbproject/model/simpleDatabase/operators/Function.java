package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.List;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;

public class Function {

	public enum Type {
		COUNT(1, "count"), AVERAGE(2, "average"), SUM(3, "sum");
		private int type;
		private String name;

		private Type(int type, String name) {
			this.type = type;
			this.name = name;
		}

		public int getType() {
			return type;
		}

		public String getName() {
			return name;
		}
	}

	public static String evaluate(List<Tuple> list, Type type, String column) {

		if (type.getType() == 1)// count
			return count(list)+"";

		else if (type.getType() == 2) // average
			return average(list,column)+"";
		

		else if (type.getType() == 3) // sum
			return sum(list,column)+"";
					
		else
			return null;
	}
	
	public static int count(List<Tuple> list )
	{
		return list.size();
	}
	
	public static int sum(List<Tuple> list, String column)
	{
		int sum = 0, col = list.get(0).getSchema().getIndex(column);
		for (Tuple t : list)
			sum += t.getInt(col);

		return sum;
	}
	
	public static int average(List<Tuple> list, String column)
	{
		return sum(list,column) / list.size();
	}

}
