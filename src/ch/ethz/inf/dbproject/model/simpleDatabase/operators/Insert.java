package ch.ethz.inf.dbproject.model.simpleDatabase.operators;
import java.io.*;

public class Insert {
	
	static final String absolutePath = Operator.absolutePath; // visible because it is in the same package

	public static void insertInto(String fileName, String[] values)
	{
		String s = "\n";
		
		for(int i = 0; i< values.length-1;i++)
			s = s.concat(values[i]+",");
		
		s = s.concat(values[values.length-1]);		
		
		try {
	    FileWriter fw = new FileWriter(absolutePath + fileName,true);
	    fw.write(s);//appends the string to the file
	    fw.close();
	    fw.flush();
		}
		catch (IOException e) {
		}
	}
	
	public static int insertIntoGenerateKey(String fileName, String[] values)
	{
		BufferedReader reader = null;
		int key=-3;
		try {
			reader = new BufferedReader(new FileReader(absolutePath + fileName));
			
				String input = reader.readLine();
				String previousLine="-2";
				while(input != null)
					 {
						previousLine= input;
						input = reader.readLine();
					 }
				reader.close();
				key = Integer.parseInt(""+previousLine.charAt(0));
				key++;
			}
		catch(final IOException e) {}
		
		
		
		String s = "\n"+key+",";
		
		for(int i = 0; i< values.length-1;i++)
			s = s.concat(values[i]+",");
		
		s = s.concat(values[values.length-1]);		
				
		try {
	    FileWriter fw = new FileWriter(absolutePath + fileName,true);
	    fw.write(s);//appends the string to the file
	    fw.close();
	    fw.flush();
		}
		catch (IOException e) {
		}
		return key;
	}
}
