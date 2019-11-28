package ch.interlis.ili2c.metamodel;

public class NoOid extends Domain {
    private static NoOid single=null;
    private NoOid() {};
    public static NoOid createNoOid() {
        if(single==null) {
            single= new NoOid();
        }
        return single;
    }

}
