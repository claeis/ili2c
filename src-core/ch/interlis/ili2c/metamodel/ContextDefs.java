package ch.interlis.ili2c.metamodel;

import java.util.ArrayList;
import java.util.Collection;

public class ContextDefs extends Container<ContextDef>{
    protected String name;

    public ContextDefs(String name) {
        checkNameSanity(name, /* empty names acceptable? */ false);
        this.name = name;

    }
    @Override
    public String getName()
    {
      return name;
    }
    @Override
    protected Collection<ContextDef> createElements() {
        return new ArrayList<ContextDef>();
    }
}
