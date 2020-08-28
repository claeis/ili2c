package ch.interlis.ili2c.metamodel;

import java.io.*;

public class Trace {
	private static PrintStream trace=null;
	public static PrintStream getTraceStream()
	{
		if(trace==null){
			try{
				trace=new PrintStream(new FileOutputStream("trace.txt"));
			}catch(FileNotFoundException ex){
			}
		}
		return trace;
	}

}

