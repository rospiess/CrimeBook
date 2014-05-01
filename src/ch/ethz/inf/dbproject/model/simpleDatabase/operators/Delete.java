package ch.ethz.inf.dbproject.model.simpleDatabase.operators;
import java.io.*;

public class Delete {
	
	static final String absolutePath = Operator.absolutePath; // visible because it is in the same package
	
	public static void deleteFrom(String fileName, int[] keys)
	{
		BufferedReader reader = null;
		StringBuilder s= new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(absolutePath + fileName));
			
				String input = reader.readLine();
				while(input != null)//Copies all Lines, except the one we want to delete
					 {
						boolean mismatch = false;
						String[] values = input.split(",");
						for(int i = 0; i< keys.length; i++)
							if(keys[i]!=Integer.parseInt(values[i]))//if not all keys match, its not the right line
								mismatch = true;
						if(mismatch)
							s.append(input+"\n");
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
