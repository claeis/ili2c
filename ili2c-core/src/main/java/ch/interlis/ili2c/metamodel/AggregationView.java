package ch.interlis.ili2c.metamodel;


public class AggregationView extends UnextendableView
{
	private ViewableAlias base=null;
	// columns may be null == ALL OF
	private UniqueEl columns=null;
    private Table aggregates=null;
	public AggregationView(ViewableAlias base)
	{
		this.base=base;
	}
        public void setEqual(UniqueEl cols)
        {
          columns=cols;
        }
        public UniqueEl getEqual()
        {
          return columns;
        }
	public ViewableAlias getBase()
	{
		return base;
	}
    public Table getAggregates(){
      if(aggregates==null){
        try {
          aggregates=new Table();
          aggregates.setIdentifiable(false);
          aggregates.setName("_AGGREGATES_"+base.getName());
          // copy attributes from base
          java.util.Iterator attrs = base.getAliasing().getAttributes ();
          while (attrs.hasNext ())
          {
            AttributeDef attr = (AttributeDef) attrs.next();
            LocalAttribute pa = new LocalAttribute ();
              pa.setName (attr.getName ());
              pa.setDomain (attr.getDomain ());
              aggregates.add (pa);
          }
        } catch (Exception ex) {
          throw new java.lang.IllegalStateException(ex.getLocalizedMessage());
        }

      }
      return aggregates;
    }
}
