package ch.interlis.ili2c.metamodel;

import java.util.Comparator;

public class TranslatedElementNameComparator implements Comparator<Element> {
    @Override
    public int compare(Element o1, Element o2) {
        o1=o1.getTranslationOfOrSame();
        o2=o2.getTranslationOfOrSame();
        return o1.getScopedName().compareTo(o2.getScopedName());
    }
}