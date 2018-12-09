package ch.interlis.ili2c.generator.nls;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class StringAdapter extends XmlAdapter<String, String>{

    @Override
    public String unmarshal(String v) throws Exception {
        if(v!=null && v.equals("-")) {
            return null;
        }
        return v;
    }

    @Override
    public String marshal(String v) throws Exception {
       if(v==null || v.equals("")) {
           return "-";
       }
       return v;
    }
}