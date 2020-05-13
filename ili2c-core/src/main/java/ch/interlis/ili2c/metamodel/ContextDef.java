package ch.interlis.ili2c.metamodel;

public class ContextDef extends AbstractLeafElement{
    protected int nameIdx;
    private Domain generic=null;
    private Domain concretes[]=null;
    public ContextDef(int nameIdx,Domain generic,Domain concretes[]) {
        this.nameIdx = nameIdx;
        this.generic=generic;
        this.concretes=concretes;
    }
    @Override
    public String getName()
    {
        return "Context"+nameIdx;
    }
    public Domain getGeneric() {
        return generic;
    }
    public Domain[] getConcretes() {
        return concretes;
    }
}
