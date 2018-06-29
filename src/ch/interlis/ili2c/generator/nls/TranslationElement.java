package ch.interlis.ili2c.generator.nls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Structure of the XML Document
 */

@XmlType(propOrder={
"scopedName",
"elementType",
"name_de",
"name_fr",
"name_it",
"name_en",
"documentation_de",
"documentation_fr",
"documentation_it",
"documentation_en"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class TranslationElement {
	private String scopedName;
	private String elementType;
	@XmlJavaTypeAdapter(StringAdapter.class)
	private String name_de;
    @XmlJavaTypeAdapter(StringAdapter.class)
	private String name_fr;
    @XmlJavaTypeAdapter(StringAdapter.class)
	private String name_it;
    @XmlJavaTypeAdapter(StringAdapter.class)
	private String name_en;
    @XmlJavaTypeAdapter(StringAdapter.class)
	private String documentation_de;
    @XmlJavaTypeAdapter(StringAdapter.class)
	private String documentation_fr;
    @XmlJavaTypeAdapter(StringAdapter.class)
	private String documentation_it;
    @XmlJavaTypeAdapter(StringAdapter.class)
	private String documentation_en;
	
	/**
	 * It gives back Name of the ElementTypes
	 * 
	 * @return Name of the Element type
	 * */
	public String getElementType() {
		return elementType;
	}
	
	/**
	 * It will be set the Value of Element Type from the Parameter
	 * 
	 * @param elementType Value of the Element Type
	 * */
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	/**
	 * It gives back Name of the Scope
	 * 
	 * @return Scope Name
	 * */
	public String getScopedName() {
		return scopedName;
	}
	/**
	 * It will be set the value of ScopeName from the Parameter
	 * 
	 * @param scopedName Scope Name
	 * */
	public void setScopedName(String scopedName) {
		this.scopedName = scopedName;
	}
	/**
	 * It gives back Value of language DE
	 * 
	 * @return Value of the Language DE
	 * */
	public String getName_de() {
		return name_de;
	}
	/**
	 * It will be set the value of NameDE from the Parameter
	 * 
	 * @param name_de Name of the Value with Language DE
	 * */
	public void setName_de(String name_de) {
		this.name_de = name_de;
	}
	/**
	 * It gives back Value of Language FR   
	 * 
	 * @return Value of the Language FR
	 * */
	public String getName_fr() {
		return name_fr;
	}
	/**
	 * It will be set the value of NameFR from the Parameter
	 * 
	 * @param name_fr Value of the Language FR
	 * */
	public void setName_fr(String name_fr) {
		this.name_fr = name_fr;
	}
	/**
	 * It gives back Value of language IT
	 * 
	 * @return Value of the Language IT
	 * */
	public String getName_it() {
		return name_it;
	}
	/**
	 * It will be set the value of NameIT from the Parameter
	 * 
	 * @param name_it Value of the Language IT
	 * */
	public void setName_it(String name_it) {
		this.name_it = name_it;
	}
	/**
	 * It gives back Name with language EN
	 * 
	 * @return Value of the Language EN
	 * */
	public String getName_en() {
		return name_en;
	}
	/**
	 * It will be set the value of NameEN from the Parameter
	 * 
	 * @param name_en Value of the Language EN
	 * */
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	/**
	 * It gives back Documentation with language DE
	 * 
	 * @return Value of the Documentation with Language DE
	 * */
	public String getDocumentation_de() {
		return documentation_de;
	}
	/**
	 * It will be set the value of DocumentationDE from the Parameter
	 * 
	 * @param documentation_de Value of the Documentation with Language DE
	 * */
	public void setDocumentation_de(String documentation_de) {
		this.documentation_de = documentation_de;
	}
	/**
	 * It gives back Documentation with language FR
	 * 
	 * @return Value of the Documentation with Language FR
	 * */
	public String getDocumentation_fr() {
		return documentation_fr;
	}
	/**
	 * It will be set the value of DocumentationFR from the Parameter
	 * 
	 * @param documentation_fr Value of the Documentation with Language FR
	 * */
	public void setDocumentation_fr(String documentation_fr) {
		this.documentation_fr = documentation_fr;
	}
	/**
	 * It gives back Documentation with language IT
	 * 
	 * @return Value of the Documentation with Language IT
	 * */
	public String getDocumentation_it() {
		return documentation_it;
	}
	/**
	 * It will be set the value of DocumentationIT from the Parameter
	 * 
	 * @param documentation_it Value with Language IT
	 * */
	public void setDocumentation_it(String documentation_it) {
		this.documentation_it = documentation_it;
	}
	/**
	 * It gives back Documentation with language EN
	 * 
	 * @return Value of the Documentation with Language EN
	 * */
	public String getDocumentation_en() {
		return documentation_en;
	}
	/**
	 * It will be set the value of DocumentationEN from the Parameter
	 * 
	 * @param documentation_en Value with Language EN
	 * */
	public void setDocumentation_en(String documentation_en) {
		this.documentation_en = documentation_en;
	}	
}
