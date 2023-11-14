package ch.interlis.ili2c.metamodel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/** This class represents a set of actual runtime parameters (name-value pairs). 
 */
public class RuntimeParameters {
    
    
    
    // z.B. "usr/data/dm01.xtf"
	public static final String MINIMAL_RUNTIME_SYSTEM01_CURRENT_TRANSFERFILE = "MinimalRuntimeSystem01.CurrentTransferfile";
    // z.B. "2017-08-22T15:00:00.000"
    public static final String MINIMAL_RUNTIME_SYSTEM01_CURRENT_DATE_TIME = "MinimalRuntimeSystem01.CurrentDateTime";
    // z.B. "ceis"
    public static final String MINIMAL_RUNTIME_SYSTEM01_CURRENT_USER_NAME = "MinimalRuntimeSystem01.CurrentUserName";
    // z.B. "services.interlis.ch"
    public static final String MINIMAL_RUNTIME_SYSTEM01_HOST_NAME = "MinimalRuntimeSystem01.HostName";
    // z.B. "Windows 10"
    public static final String MINIMAL_RUNTIME_SYSTEM01_OPERATING_SYSTEM_NAME = "MinimalRuntimeSystem01.OperatingSystemName";
    // z.B. "1.0.0"
    public static final String MINIMAL_RUNTIME_SYSTEM01_RUNTIME_SYSTEM_VERSION = "MinimalRuntimeSystem01.RuntimeSystemVersion";
    // z.B. "ili2pdf"
    public static final String MINIMAL_RUNTIME_SYSTEM01_RUNTIME_SYSTEM_NAME = "MinimalRuntimeSystem01.RuntimeSystemName";
    private HashMap<String,Object> values=new HashMap<String,Object>();
	private ArrayList<String> orderedKeys=null;
	public RuntimeParameters()
	{
		this(false);
	}
	public RuntimeParameters(boolean keepOrderOfDefs)
	{
		if(keepOrderOfDefs){
			orderedKeys=new ArrayList<String>();
		}
	}
	public RuntimeParameters(RuntimeParameters src)
	{
		if(src!=null){
			java.util.Iterator<String> it=src.getNamesIterator();
			while(it.hasNext()){
				String name=it.next();
				Object obj=src.values.get(name);
				setValue(name,obj);
			}
		}
		
	}
	static public void initDefaultValues(RuntimeParameters src)
	{
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date today = new Date();
        String dateOut = dateFormatter.format(today);
	    // z.B. "usr/data/dm01.xtf"
	    //src.setValue(MINIMAL_RUNTIME_SYSTEM01_CURRENT_TRANSFERFILE,"");
	    // z.B. "2017-08-22T15:00:00.000"
	    src.setValue(MINIMAL_RUNTIME_SYSTEM01_CURRENT_DATE_TIME,dateOut);
	    // z.B. "ceis"
	    src.setValue(MINIMAL_RUNTIME_SYSTEM01_CURRENT_USER_NAME,System.getProperty("user.name"));
	    // z.B. "services.interlis.ch"
	    //src.setValue(MINIMAL_RUNTIME_SYSTEM01_HOST_NAME,"");
	    // z.B. "Windows 10"
	    src.setValue(MINIMAL_RUNTIME_SYSTEM01_OPERATING_SYSTEM_NAME,System.getProperty("os.name"));
	    // z.B. "1.0.0"
	    //src.setValue(MINIMAL_RUNTIME_SYSTEM01_RUNTIME_SYSTEM_VERSION,"");
	    // z.B. "ili2pdf"
	    //src.setValue(MINIMAL_RUNTIME_SYSTEM01_RUNTIME_SYSTEM_NAME,"");
	    
	}
	/** gets a property value.
	 * @param name of property.
	 * @return value or null. Never returns an empty String.
	 */
	public Object getValue(String name) {
		Object value=values.get(name);
		return value;
	}
	public void setValue(String name,Object value) {
		if(value==null){
			if(orderedKeys!=null && orderedKeys.contains(name)){
				orderedKeys.remove(name);
			}
			values.remove(name);
		}else{
			if(orderedKeys!=null && !orderedKeys.contains(name)){
				orderedKeys.add(name);
			}
			values.put(name, value);
		}
	}
	/** get the list of property names.
	 * @return set<String valueName>
	 */
	public java.util.Set<String> getNames(){
		return values.keySet();
	}
	public java.util.Iterator<String> getNamesIterator(){
		if(orderedKeys!=null){
			return orderedKeys.iterator();
		}
		return values.keySet().iterator();
	}
	public String toString()
	{
		StringBuffer ret=new StringBuffer();
		java.util.Iterator<String> it=getNamesIterator();
		if(it.hasNext()){
			String sep="";
			ret.append("RuntimeParameters{");
			while(it.hasNext()){
				String name=it.next();
				Object obj=values.get(name);
				ret.append(sep+name+"="+obj.toString());
				sep=";";
			}
			ret.append("}");
		}
		return ret.toString();
	}
}