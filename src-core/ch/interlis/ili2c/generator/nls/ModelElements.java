package ch.interlis.ili2c.generator.nls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Class that the data is kept
 * */

@XmlRootElement(name="IliModelElements")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelElements implements Iterable<TranslationElement> {
    private List<TranslationElement> element=new ArrayList<TranslationElement>();
	/**
	 * it gives to the list of records.
	 * 
	 * @return List of records
	 * */	
    //@Override
	public Iterator<TranslationElement> iterator() {
		return element.listIterator();
	}
	/**
	 * Adds records to the ArrayList.
	 * 
	 * @return Result of the process. if occurs a problem in Save then it returns false 
	 * */
	public boolean add(TranslationElement ele) {
		return element.add(ele);
	}
	
	public void sort() {
	    Collections.sort(element, new Comparator<TranslationElement>() { 
	        @Override
	        public int compare(TranslationElement ele1, TranslationElement ele2) {
	            return ele1.getScopedName().compareTo(ele2.getScopedName());
	        }
	    });
	}
}
