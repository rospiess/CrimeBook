package ch.ethz.inf.dbproject.model.simpleDatabase.operators;
import java.io.*;

public class Update {
	
	static final String absolutePath = Operator.absolutePath; // visible because it is in the same package
	
	public static void update(String fileName, int[] keys, String[] newvalues)//newvalues contains the new Values or null if we want to keep the old one
	{
		BufferedReader reader = null;
		StringBuilder s= new StringBuilder();
		
		//Replace commas
		Insert.prepare_values(newvalues);
		
		try {
			reader = new BufferedReader(new FileReader(absolutePath + fileName));
			
				String input = reader.readLine();
				while(input != null)//Copies all Lines, except the one we want to update
					 {
						boolean mismatch = false;
						String[] values = input.split(",");
						for(int i = 0; i< keys.length; i++)
							if(keys[i]!=Integer.parseInt(values[i]))
								mismatch = true;
						if(mismatch)
							s.append(input+"\n");
						else
						{
							for(int i = 0; i< values.length; i++)
							{
								if(newvalues[i] != null)//Actual Update
									s.append(newvalues[i]+",");
								else//Copy old value
									s.append(values[i]+",");
							}
							s.deleteCharAt(s.length()-1);//delete last ,
							s.append("\n");
						}
						input = reader.readLine();
					 }
				reader.close();
			}
		catch(final IOException e) {}
				
		try {
	    PrintWriter fw = new PrintWriter(absolutePath + fileName);
	    fw.write(s.substring(0,s.length()-1));//Overwrite existing file, remove the last \n
	    fw.close();
	    fw.flush();
		}
		catch (IOException e) {
		}
	}
	
	public static void update(String fileName, int key, String[] newvalues)
	{
		update(fileName,key+"", newvalues);
	}
	
	
	//update for String as key
	public static void update(String fileName, String key, String[] newvalues)//newvalues contains the new Values or null if we want to keep the old one
	{
		BufferedReader reader = null;
		StringBuilder s= new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(absolutePath + fileName));
			
				String input = reader.readLine();
				while(input != null)//Copies all Lines, except the one we want to update
					 {
						boolean mismatch = false;
						String[] values = input.split(",");
						if(!key.equals(values[0]))
								mismatch = true;
						if(mismatch)
							s.append(input+"\n");
						else
						{
							for(int i = 0; i< values.length; i++)
							{
								if(newvalues[i] != null)//Actual Update
									s.append(newvalues[i]+",");
								else//Copy old value
									s.append(values[i]+",");
							}
							s.deleteCharAt(s.length()-1);//delete last ,
							s.append("\n");
						}
						input = reader.readLine();
					 }
				reader.close();
			}
		catch(final IOException e) {}
				
		try {
	    PrintWriter fw = new PrintWriter(absolutePath + fileName);
	    fw.write(s.substring(0,s.length()-1));//Overwrite existing file, remove the last \n
	    fw.close();
	    fw.flush();
		}
		catch (IOException e) {
		}
	}
	
}
