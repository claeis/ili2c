package ch.interlis.ili2c;

import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.metamodel.TransferDescription;
import java.util.ArrayList;

public class Ili2c {
	private Ili2c(){};
	public static ArrayList getIliLookupPaths(ArrayList ilifilev) 
	{
		return Main.getIliLookupPaths(ilifilev);
	}
	  static public String getIli2cHome()
	  {
		  return Main.getIli2cHome();
	  }
	  @Deprecated
	  static public TransferDescription runCompiler(Configuration config)
	  throws Ili2cFailure
	  {
		  TransferDescription ret=Main.runCompiler(config);
		  if(ret==null){
			  throw new Ili2cFailure("compiler failed");
		  }
		  return ret;
	  }
	  @Deprecated
	  public static String getVersion() {
		  return TransferDescription.getVersion();
	  }
	  static public TransferDescription compileIliModels(ArrayList modelv,ArrayList modeldirv)
	  throws Ili2cFailure
	  {
		  TransferDescription ret=Main.compileIliModels(modelv, modeldirv, null);
		  if(ret==null){
			  throw new Ili2cFailure("compiler failed");
		  }
		  return ret;
	  }
	  static public TransferDescription compileIliFiles(ArrayList filev,ArrayList modeldirv)
	  throws Ili2cFailure
	  {
		  TransferDescription ret=Main.compileIliFiles(filev, modeldirv, null);
		  if(ret==null){
			  throw new Ili2cFailure("compiler failed");
		  }
		  return ret;
	  }
	  static public void logIliFiles(ch.interlis.ili2c.config.Configuration config)
	  {
		  Main.logIliFiles(config);
	  }
	
}
