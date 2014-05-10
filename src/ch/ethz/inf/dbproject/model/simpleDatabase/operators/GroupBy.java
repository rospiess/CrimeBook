package ch.ethz.inf.dbproject.model.simpleDatabase.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.ethz.inf.dbproject.model.simpleDatabase.Tuple;
import ch.ethz.inf.dbproject.model.simpleDatabase.TupleSchema;

public class GroupBy extends Operator {

	private final Operator op;
	private final String column, column2;
	private HashMap<String,List<Tuple>> map = new HashMap<String,List<Tuple>>();
	private final Function.Type type;
	private TupleSchema schema1;
	private List<String> keys = new ArrayList<String>();
	int offset, length = -1;

	public GroupBy(	final Operator op, final String column, final Function.Type type)
	 {
		this.op = op;
		this.column = column;
		this.type = type;
		column2 = "";
	}
	
	public GroupBy(	final Operator op, final String column, final Function.Type type, int length)
	 {
		this.op = op;
		this.column = column;
		this.type = type;
		column2 = "";
		this.length = length;
	}
	
	public GroupBy(	final Operator op, final String column, final Function.Type type, final String column2)
	 {
		this.op = op;
		this.column = column;
		this.type = type;
		this.column2 = column2;
	}
	public GroupBy(	final Operator op, final String column, final Function.Type type, final String column2, int length)
	 {
		this.op = op;
		this.column = column;
		this.type = type;
		this.column2 = column2;
		this.length = length;
	}
	
	

	
	@Override
	public boolean moveNext() {
		if(map.isEmpty()){
			while(op.moveNext())
			{
				Tuple t = op.current();
				if(schema1 == null)
					schema1 = t.getSchema();
				
				String key = t.getString(schema1.getIndex(column));
				if(key !=null){
					if(length != -1)
						key = key.substring(0, length);
				
					if(!map.containsKey(key))
					{
						ArrayList<Tuple> l = new ArrayList<Tuple>();
						l.add(t);
						map.put(key,l);
					}
					else
						map.get(key).add(t);
				}
			}
			for(String s: map.keySet())
				keys.add(s);
			
			offset = 0;
		}
		while(offset < keys.size())
		{
			String s = type.getName(), key = keys.get(offset);
			String value = Function.evaluate(map.get(key), type, column2);

			current = new Tuple(new TupleSchema(new String[]{column,s}),
					new String[]{key, value});
			offset++;
			return true;
		}
		return false;
	}
}
