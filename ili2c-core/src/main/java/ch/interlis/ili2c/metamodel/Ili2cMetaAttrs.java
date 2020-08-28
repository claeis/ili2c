package ch.interlis.ili2c.metamodel;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;

public class Ili2cMetaAttrs {
	/** iliQName for "global" metaattrs
	 */
	public static final String PARAMETER = "PARAMETER";
	public static final String ILI2C_TRANSLATION_OF="ili2c.translationOf"; 
	public static final String ILI2C_CONSTRAINT_NAME = "name";
	public static final String ILI2C_CRS = "CRS";
	public static final String ILI2C_ILI23XSD_ADDALLINTERLISTYPESDEFAULT="ili2c.ili23xsd.addAllInterlisTypesDefault";
	public static final String ILI2C_ILI23XSD_ADDALIASTABLEDEFAULT="ili2c.ili23xsd.addAliasTableDefault";
	public static final String ILI2C_ILI23XML_SUPPORTPOLYMORPHICREAD="ili2c.ili23xml.supportPolymorphicRead";
	public static final String ILI2C_ILI23XML_SUPPORTINCONSISTENTTRANSFER="ili2c.ili23xml.supportInconsistentTransfer";
	public static final String ILI2C_ILI23XML_SUPPORTINCRMENTALTRANSFER="ili2c.ili23xml.supportIncrementalTransfer";
	public static final String ILI2C_ILI23XML_SUPPORTSOURCEBASKETID="ili2c.ili23xml.supportSourceBasketId";
	public static final String ILIMODELSXML_TECHNICAL_CONTACT = "technicalContact";
	public static final String ILIMODELSXML_ID_GEO_IV = "IDGeoIV";
	public static final String ILIMODELSXML_FURTHER_INFORMATION = "furtherInformation";
	public static final String ILIMODELSXML_TAGS = "tags";
	public static final String ILIMODELSXML_PRECURSOR_VERSION = "precursorVersion";
	public static final String ILIMODELSXML_FURTHER_METADATA = "furtherMetadata";
	public static final String ILIMODELSXML_ORIGINAL = "Original";
	public static final String ILI2C_TEXTMINIMALCHARSET="ili2c.textMinimalCharset";
	private HashMap<String,HashMap<String,String>> data=new HashMap<String,HashMap<String,String>>(); // Set<String iliQName,Set<String metaAttr,String metaAttrValue>>
	public Set<String> getIliQnames() {
		return new HashSet<String>(data.keySet());
	}
	public Set<String> getMetaAttrs(String iliQName) {
		HashMap<String,String> modelele=null;
		if(data.containsKey(iliQName)){
			modelele=data.get(iliQName);
		}
		if(modelele==null){
			return null;
		}
		return new HashSet<String>(modelele.keySet());
	}



	public String getMetaAttrValue(String iliQName, String metaAttr) {
		HashMap<String,String> modelele=null;
		if(data.containsKey(iliQName)){
			modelele=data.get(iliQName);
		}
		if(modelele==null){
			return null;
		}
		return modelele.get(metaAttr);
	}
	public void setMetaAttrValue(String iliQName, String metaAttr,String value) {
		HashMap<String,String> modelele=null;
		if(data.containsKey(iliQName)){
			modelele=data.get(iliQName);
		}else{
			modelele=new HashMap<String,String>();
			data.put(iliQName, modelele);
		}
		modelele.put(metaAttr, value);
	}
}